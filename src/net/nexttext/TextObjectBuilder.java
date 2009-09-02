/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.nexttext;

import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.property.Property;

import java.awt.Rectangle;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.*;

import processing.core.*;

/**
 * Factory class used for creating TextObjects and adding them to NextText.
 *
 * <p>A TextObjectBuilder provides methods which take Strings and build
 * hierarchies of TextObjects from them.  It can be configured in the following
 * ways: </p>
 *
 * <ul>
 *   <li>Attach created TextObjects to an existing TextObject.</li>
 *   <li>Specify properties to set in each created TextObject. </li>
 *   <li>Specify behaviours to attach to each created TextObject. </li>
 *   <li>Treat created TextObjectGroups and TextObjectGlyphs differently. </li>
 *   <li>Specify how the hierarchy is structured. </li>
 *   <li>Add TextObjectGlyphs to the SpatialList. </li>
 *   <li>Specify font information for created TextObjects. </li>
 *   <li>Specify a location on the screen for created TextObjects. </li>
 *   <li>Specify the relation of position to the group.  </li>
 * </ul>
 *
 * <p>Future facilities which would be useful if included: </p>
 *
 * <ul>
 *   <li>Specify rules for laying out text: spacing, kerning, and paragraph
 *   layout. </li>
 * </ul>
 */
/* $Id$ */
public class TextObjectBuilder {

    //////////////////////////////////////////////////////////////////////
    // Internal Members

    // The book is used for synchronisation of updates to the
    // TextObjectHierarchy.
    Book book;
    
    static Pattern pattern = Pattern.compile(".*\\W");

    //////////////////////////////////////////////////////////////////////
    // Constructors
    
    /**
     * Instantiates the TextObjectBuilder.
     * 
     * @param book the Book to build TextObjects into
     */
    public TextObjectBuilder(Book book) {
    	this.book = book;
    	setParent(book.getTextRoot());
    }
    
    /**
     * Instantiates the TextObjectBuilder.
     * 
     * @param book the Book
     * @param page the Page to build TextObjects into
     */
    public TextObjectBuilder(Book book, TextPage page) {
    	this.book = book;
    	setParent(page.getTextRoot());
    }

    //////////////////////////////////////////////////////////////////////
    // Configurable parameters

    PFont pfont;
    Font font;
    //Font font = new Font("courier", 0, 18);
    private PVector spaceOffset; // Width of a space character
    private PVector trackingOffset; // Space between two characters
    private PVector lineHeight;  // Height of a line
    public void setFont(PFont pf) { 
        Font f = Book.loadFontFromPFont(pf);
        setFont(pf, f);
    }
    public void setFont(PFont pf, Font f) { 
        pfont = pf;
        font = f;
        FontRenderContext frc = new FontRenderContext(null, false, false);
		GlyphVector sp = f.createGlyphVector( frc, " " );		
		spaceOffset = new PVector( (int)sp.getLogicalBounds().getWidth(), 0,0);        
		trackingOffset = new PVector(0, 0, 0);
		lineHeight = new PVector( 0,(int)sp.getLogicalBounds().getHeight(),0);
    }
    public PFont getFont() { return pfont; }


    PVector pos = new PVector(0,0,0);
    public void setPosition(PVector pos) { this.pos = pos; }
    public PVector getPosition() { return pos; }

    
    boolean addToSpatialList = false;
    /** If created objects should be added to the spatial list. */
    public void setAddToSpatialList(boolean addToSpatialList) {
        this.addToSpatialList = addToSpatialList;
    }


    int align = PConstants.LEFT;
    /** Set the horizontal alignment type of the group around the position. */
    public void setTextAlign(int align) {
        this.align = align;
    }


    /** Parent to attach new groups to, may be null. */
    TextObjectGroup parent = null;
    /** May be null to unset. */
    public void setParent(TextObjectGroup parent) { this.parent = parent; }
    /** null if there's none. */
    public TextObjectGroup getParent() { return parent; }


    Set<AbstractBehaviour> glyphBehaviours = new HashSet<AbstractBehaviour>();
    /** Created glyphs will get this behaviour. */
    public void addGlyphBehaviour(AbstractBehaviour b) { glyphBehaviours.add(b); }
    /** Created glyphs will no longer get this behaviour. */
    public void removeGlyphBehaviour(AbstractBehaviour b) { glyphBehaviours.remove(b); }
    /** Created glyphs will no longer get any behaviours. */
    public void removeAllGlyphBehaviours() { glyphBehaviours.clear(); }
    /** Behaviours to be added to each glyph. */
    public Set<AbstractBehaviour> getGlyphBehaviours() {
        return Collections.unmodifiableSet(glyphBehaviours);
    }


    Set<AbstractBehaviour> groupBehaviours = new HashSet<AbstractBehaviour>();
    /** Created groups will get this behaviour. */
    public void addGroupBehaviour(AbstractBehaviour b) { groupBehaviours.add(b); }
    /** Created groups will no longer get this behaviour. */
    public void removeGroupBehaviour(AbstractBehaviour b) { groupBehaviours.remove(b); }
    /** Created groups will no longer get any behaviours. */
    public void removeAllGroupBehaviours() { groupBehaviours.clear(); }
    /** Behaviours to be added to each group. */
    public Set<AbstractBehaviour> getGroupBehaviours() {
        return Collections.unmodifiableSet(groupBehaviours);
    }


    Map<String, Property> glyphProperties = new HashMap<String, Property>();
    /** Created glyphs will get this property. */
    public void addGlyphProperty(String name, Property p) {
        glyphProperties.put(name, p);
    }
    /** Created glyphs will no longer get this property. */
    public void removeGlyphProperty(String name) {
        glyphProperties.remove(name);
    }
    /** Created glyphs will no longer get any properties. */
    public void removeAllGlyphProperties() { glyphProperties.clear(); }


    Map<String, Property> groupProperties = new HashMap<String, Property>();
    /** Created groups will get this property. */
    public void addGroupProperty(String name, Property p) {
        groupProperties.put(name, p);
    }
    /** Created groups will no longer get this property. */
    public void removeGroupProperty(String name) {
        groupProperties.remove(name);
    }
    /** Created groups will no longer get any properties. */
    public void removeAllGroupProperties() { groupProperties.clear(); }


    //////////////////////////////////////////////////////////////////////
    // Core methods
    
    /**
     * Builds a tree of TextObjects from the given string, at the 
     * specified location.
     * 
     * <p>By default, this creates and returns a new TextObjectGroup, 
     * with a child TextObjectGlyph for each character in the string.</p>
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * 
     * @return the built TextObjectGroup
     */
    public TextObjectGroup build(String text, int x, int y) {
    	return build(text, new PVector(x, y));
    }

    /**
     * Build a tree of TextObjects from the given string.
     *
     * <p>This is the core method of this class.  Its behaviour is modified by
     * all of the available TextObjectBuilder configuration.  By default, it
     * creates and returns a new TextObjectGroup, with a child TextObjectGlyph
     * for each character in the string.</p>
     */

    public TextObjectGroup build(String text) {
        return build(text, pos);
    }

    /**
     * Build a tree of TextObjects at the specified location.
     */
    public TextObjectGroup build(String text, PVector pos) {
        
        TextObjectGroup newGroup = createGroup( text, pos );
        applyBuilderOptions(newGroup, false);
        return newGroup;
    }
    
    /**
     * Build a tree of TextObjects from the given string.
     *
     * <p>The string is parsed as a sentence, where whitespace characters are
     * treated as word delimiters.  The returned TextObjectGroup has a child
     * TextObjectGroup for each word in the string, and TextObjectGlyph
     * grandchildren.  Spaces between words are included as TextObjectGroups
     * containing a single TextObjectGlyph child.</p>
     *
     * @return a group containing a sub-group for each identified tokens.
     * Spaces are represented as groups containing one Space character.
     */
    public TextObjectGroup buildSentence( String text ) {
        return buildSentence( text, pos, Integer.MAX_VALUE );
    }
        
    /**
     * Builds a tree of TextObjects from the given sentence, at the
     * specified location.
     *
     * <p>The returned TextObject tree will be laid out so that no more than
     * lineLength characters appear on a single line.</p>
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * @param lineLength the max number of characters per line
     * 
     * @return the built TextObjectGroup
     */
    public TextObjectGroup buildSentence(String text, int x, int y, int lineLength) {
    	return buildSentence(text, new PVector(x, y), lineLength);
    }
    
    /**
     * Build a tree of TextObjects from the given string.
     *
     * <p>The returned TextObject tree will be laid out so that no more than
     * lineLength characters appear on a single line.  </p>
     */
    public TextObjectGroup buildSentence( String text, PVector pos, int lineLength ) {
    	//Make sure the lineLength is greater than 1 (space for the dash)
    	if (lineLength <= 1) {
    		PApplet.println("Warning: Line length must be greater than 1.");
    		lineLength = 2;
    	}
    	
    	// Pre-process the text
        text = preprocessMessage(text, lineLength);
        
        StringTokenizer st = new StringTokenizer(text," \n",true);
        
        TextObjectGroup newGroup = new TextObjectGroup(pos);
        PVector gOffset = new PVector(0,0,0);
        
        while ( st.hasMoreTokens() ) {
        	// Get each token
            String tokenStr = st.nextToken();
            
            // If the token is a \n begin a new line
            if (tokenStr.equals("\n")) {
            	gOffset.x = 0;
            	gOffset.add(lineHeight);
            	continue;
            }
            
            // display other words
            TextObjectGroup token = createGroup( tokenStr, gOffset );
            gOffset.add( new PVector(token.getBoundingPolygon().getBounds().width+trackingOffset.x, 0, 0) );
            
            newGroup.attachChild( token );
        }
        
        applyBuilderOptions(newGroup, true);        
        return newGroup;
    }
    
    /**
     * Format a message for display.
     *
     * <p>Whitespace is normalized, and lines are folded to fit within the
     * specified line length.  Words that are too long to fit on a single line
     * are split using hyphens and folded across lines.  </p>
     *
     * <p>The returned String will only use newline characters for line
     * termination, (no carriage returns), and spaces for spacing (no tabs).
     * </p>
     */
    private String preprocessMessage(String text, int lineLength) {
        // Replace carriage returns and tabs.
    	text = text.replaceAll("\r\n?", "\n");
    	text = text.replaceAll("\t", " ");
    	
        // The provided text is read word by word, and new text is written to
        // returnText.  numChars tracks the length of line currently being
        // written to returnText.
    	StringBuffer returnText = new StringBuffer();
    	StringTokenizer st = new StringTokenizer(text, " \n", true);
    	int numChars = 0;
    	
    	while(st.hasMoreTokens()) {
    		String tokenStr = st.nextToken();
    		
    		// Fold long words across multiple lines.
    		if (tokenStr.length() > lineLength) {
    			tokenStr = splitLongWord(tokenStr, lineLength-numChars, lineLength);
    			numChars = (tokenStr.length()-1) - tokenStr.lastIndexOf('\n');
    		}
    		else {
    			if (numChars == 0 && tokenStr.equals(" ")) continue;
    			numChars += tokenStr.length();
    			if (numChars > lineLength) {
    				//if the token that gets us over the edge is a space
    				//then remove it so that lines are all aligned
    				if (tokenStr.equals(" "))
    					tokenStr = "";
    				
    				//keep track of character count
    				numChars = tokenStr.length();
    				
    				//remove trailing spaces before adding the newline
    				//this makes sure right-aligned text is flush
    				while(returnText.charAt(returnText.length()-1) == ' ')
    					returnText = returnText.deleteCharAt(returnText.length()-1);
    				
    				//append the newline
    				tokenStr = "\n" + tokenStr;
    			}
    		}
    		
    		returnText.append(tokenStr);
    	}

        // Remove newlines from the beginning and end of the text.  One notable
        // case were the text will start with a newline is if the first word
        // was longer than the maximum line length.
    	if (returnText.indexOf("\n") == 0)
    		returnText.deleteCharAt(0);
    	if ((returnText.length() > 0)
            && (returnText.lastIndexOf("\n") == returnText.length() - 1))
    		returnText.deleteCharAt(returnText.length() - 1);
    	
    	return returnText.toString().replaceAll("\n{2,}", "\n");
    }

    // Insert \n characters into the given string, so that no line of text is
    // greater than maxLength.  The `-' character is used to show that a word
    // continues on the next line.  If possible, the breaks will be done at
    // non-letter characters.
    private String splitLongWord(String longWord, int lineLength, int maxLength) {
		// the word is too long, split it
		//StringBuffer temp = new StringBuffer("\n");
    	StringBuffer temp = new StringBuffer();
		Matcher matcher;
		
		while (longWord.length() > lineLength) {
			// find if there is non-word character with maxLength
			matcher = pattern.matcher(longWord.substring(0, lineLength - 1));
			int splitAt = (matcher.find())? matcher.end() : lineLength - 1;
			
			temp.append(longWord.substring(0, splitAt) + "-\n");
			longWord = longWord.substring(splitAt);
			lineLength = maxLength;
		}
		return temp.append(longWord).toString();
    }
    
    /**
     * This methods returns a TextObject group created using the given string,
     * positioned using the given Vector3.
     */
    private TextObjectGroup createGroup( String text, PVector pos ) {
        
        TextObjectGroup newGroup = new TextObjectGroup(groupProperties, pos);

        // Each glyph is offset by gOffset from the word location.
        PVector gOffset = new PVector(0,0,0);
        for (int i = 0; i < text.length(); i++) {
            
            String glyph = text.substring(i,i+1);
            
            TextObjectGlyph to = 
                new TextObjectGlyph(glyph, pfont, glyphProperties, gOffset);

			gOffset.x += to.getLogicalBounds().getWidth()+trackingOffset.x;

            newGroup.attachChild(to);
        }
        return newGroup;
    }

    /**
     * Applies the builder's parameters. 
     * @param newGroup the TextObjectGroup to apply the parameters to
     * @param isSentence whether the passed group was built using buildSentence() or not
     */
    private void applyBuilderOptions(TextObjectGroup newGroup, boolean isSentence) {
        
        if (align != PConstants.LEFT) {
        	alignGroup(newGroup, isSentence);
        }

        synchronized (book) {
            if (parent != null) {
                parent.attachChild(newGroup);
            }
            if (addToSpatialList) {
                book.getSpatialList().add(newGroup);
            }

            // Behaviours are added after the data structure is created, so
            // they can be synchronized together, and in case they care about
            // the structure.

            Iterator<AbstractBehaviour> i = groupBehaviours.iterator();
            while (i.hasNext()) {
                i.next().addObject(newGroup);
            }

            TextObject child = newGroup.getLeftMostChild();
            while (child != null) {
            	if (!isSentence) {
            		// we can assume child is a TextObjectGlyph because of the way build() works
            		Iterator<AbstractBehaviour> bI = glyphBehaviours.iterator();
            		while (bI.hasNext()) {
            			bI.next().addObject(child);
            		}
            	} else {
            		// we can assume grandChild is a TextObjectGlyph because of the way buildSentence() works
            		TextObject grandChild = ((TextObjectGroup)child).getLeftMostChild();
            		while (grandChild != null) {
            			Iterator<AbstractBehaviour> bI = glyphBehaviours.iterator();
                		while (bI.hasNext()) {
                			bI.next().addObject(grandChild);
                		}
                		grandChild = grandChild.getRightSibling();
            		}
            	}
                child = child.getRightSibling();
            }
        }
    }
    

    
    /**
     * Move children of the group to make the position its center. This is done
     * by adding the offset from the center of the bounding box to the group 
     * position, to the position of each glyph.
     * 
     * @param newGroup the TextObjectGroup to center
     * @param isSentence whether the passed group was built using buildSentence() or not
     */
    private void alignGroup(TextObjectGroup newGroup, boolean isSentence) {
    	if (!isSentence) {
        	Rectangle bb = newGroup.getBoundingPolygon().getBounds();
        	PVector offset = newGroup.getPositionAbsolute();
        	if (align == PConstants.CENTER) {
        		offset.sub(new PVector((float)bb.getCenterX(), offset.y));
        	} else if (align == PConstants.RIGHT) {
        		offset.sub(new PVector((float)(bb.getX()+bb.getWidth()), offset.y));
        	}
        	TextObject child = newGroup.getLeftMostChild();
        	while (child != null) {
        		child.getPosition().add(offset);
        		child = child.getRightSibling();
        	}
        } else {
        	// When the group is a sentence, we need to check the y-pos of 
        	// each child group as they may be positioned on different lines.
        	TextObjectGroup firstInLine = (TextObjectGroup)newGroup.getLeftMostChild();
        	while (firstInLine != null) {
        		// start a new line and calculate a new bounding box
        		float currY = firstInLine.getPositionAbsolute().y;
        		Rectangle bb = firstInLine.getBounds();
        		TextObjectGroup sibling = (TextObjectGroup)firstInLine.getRightSibling();
        		if (sibling == null) {
        			// only one group in this sentence, center it
        			alignLine(firstInLine, sibling, bb);
                	// set first group to null to exit the loop
                	firstInLine = null;
        		}
            	while (sibling != null) {
            		// if the sibling is on the same line...
            		if (currY == sibling.getPositionAbsolute().y) {
            			// ...add its bounds to the line's bounding box
            			bb = bb.union(sibling.getBounds());
            			sibling = (TextObjectGroup)sibling.getRightSibling();
            			if (sibling == null) {
            				// we reached the end of the group, center the line
            	    		alignLine(firstInLine, sibling, bb);
                        	// set first group and sibling to null to exit the loop
                        	firstInLine = sibling = null;
            			}
            		// the sibling is on a new line
            		} else {
            			// center the previous line
            			alignLine(firstInLine, sibling, bb);
                    	// set the sibling as the new start group of the line
                    	firstInLine = sibling;
                    	sibling = null;
            		}
            	}
        	}
        }
    }
    
    private void alignLine(TextObjectGroup first, TextObjectGroup limit, Rectangle lineBounds) {
    	PVector offset = first.getPositionAbsolute();
    	if (align == PConstants.CENTER) {
    		offset.sub(new PVector((float)lineBounds.getCenterX(), (float)lineBounds.getCenterY()));
    	} else if (align == PConstants.RIGHT) {
    		offset.sub(new PVector((float)(lineBounds.getX()+lineBounds.getWidth()),
    							   (float)(lineBounds.getY()+lineBounds.getHeight())));
    	}
    	TextObjectGroup currChild = first;
    	while (currChild != limit) {
    		TextObject grandChild = currChild.getLeftMostChild();
    		while (grandChild != null) {
    			grandChild.getPosition().add(offset);
    			grandChild = grandChild.getRightSibling();
    		}
    		currChild = (TextObjectGroup)currChild.getRightSibling();
    	}
    }
    
    public void setLineHeight(float d) { lineHeight.y = d; }
    public float getLineHeight() { return lineHeight.y; }
    public void setTrackingOffset(float d) { trackingOffset.x = d; }
    public float getTrackingOffset() { return trackingOffset.x; }
}
