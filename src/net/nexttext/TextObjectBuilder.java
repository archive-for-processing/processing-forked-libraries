//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext;

import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.property.Property;

import java.awt.Font;
import java.awt.Rectangle;
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

/**
 * Factory class used for creating TextObjects and adding them to NextText.
 *
 * <p>A TextObjectBuilder provides methods which take Strings and build
 * hierarchys of TextObjects from them.  It can be configured in the following
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
 * <p>Future facilities which would be useful inclued: </p>
 *
 * <ul>
 *   <li>Specify rules for laying out text: spacing, kerning, and paragraph
 *   layout. </li>
 * </ul>
 */

public class TextObjectBuilder {

	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/TextObjectBuilder.java,v 1.21.6.2 2007/09/25 21:34:22 elie Exp $";

    //////////////////////////////////////////////////////////////////////
    // Internal Members

    // The book is used for synchronization of updates to the
    // TextObjectHierarchy.
    Book book;
    
    static Pattern pattern = Pattern.compile(".*\\W");

    //////////////////////////////////////////////////////////////////////
    // Constructors
    
    /**
     * Instantiates the NTPTextObjectBuilder.
     * 
     * @param book the Book to build TextObjects into
     */
    public TextObjectBuilder(Book book) {
    	this.book = book;
    	setParent(book.getTextRoot());
    }
    
    /**
     * Instantiates the NTPTextObjectBuilder.
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

    Font font = new Font("courier", 0, 18);
    private Vector3 spaceOffset; // Width of a space character
    private Vector3 trackingOffset; // Space between two characters
    private Vector3 lineHeight;  // Height of a line
    public void setFont(Font font) { 
        this.font = font;
        
        FontRenderContext frc = new FontRenderContext(null, false, false);
		GlyphVector sp = this.font.createGlyphVector( frc, " " );		
		spaceOffset = new Vector3( (int)sp.getLogicalBounds().getWidth(), 0,0);        
		trackingOffset = new Vector3(0, 0, 0);
		lineHeight = new Vector3( 0,(int)sp.getLogicalBounds().getHeight(),0);
    }
    public Font getFont() { return font; }


    Vector3 pos = new Vector3(0,0,0);
    public void setPosition(Vector3 pos) { this.pos = pos; }
    public Vector3 getPosition() { return pos; }


    boolean addToSpatialList = false;
    /** If created objects should be added to the spatial list. */
    public void setAddToSpatialList(boolean addToSpatialList) {
        this.addToSpatialList = addToSpatialList;
    }


    boolean centerGroup = false;
    /** Center the group around position, or leave it as default layout. */
    public void setCenterGroup(boolean centerGroup) {
        this.centerGroup = centerGroup;
    }


    /** Parent to attach new groups to, may be null. */
    TextObjectGroup parent = null;
    /** May be null to unset. */
    public void setParent(TextObjectGroup parent) { this.parent = parent; }
    /** null if there's none. */
    public TextObjectGroup getParent() { return parent; }


    Set glyphBehaviours = new HashSet();
    /** Created glyphs will get this behaviour. */
    public void addGlyphBehaviour(AbstractBehaviour b) { glyphBehaviours.add(b); }
    /** Created glyphs will no longer get this behaviour. */
    public void removeGlyphBehaviour(AbstractBehaviour b) { glyphBehaviours.remove(b); }
    /** Created glyphs will no longer get any behaviours. */
    public void removeAllGlyphBehaviours() { glyphBehaviours.clear(); }
    /** Behaviours to be added to each glyph. */
    public Set getGlyphBehaviours() {
        return Collections.unmodifiableSet(glyphBehaviours);
    }


    Set groupBehaviours = new HashSet();
    /** Created groups will get this behaviour. */
    public void addGroupBehaviour(AbstractBehaviour b) { groupBehaviours.add(b); }
    /** Created groups will no longer get this behaviour. */
    public void removeGroupBehaviour(AbstractBehaviour b) { groupBehaviours.remove(b); }
    /** Created groups will no longer get any behaviours. */
    public void removeAllGroupBehaviours() { groupBehaviours.clear(); }
    /** Behaviours to be added to each group. */
    public Set getGroupBehaviours() {
        return Collections.unmodifiableSet(groupBehaviours);
    }


    Map glyphProperties = new HashMap();
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


    Map groupProperties = new HashMap();
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
    	return build(text, new Vector3(x, y));
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
    public TextObjectGroup build(String text, Vector3 pos) {
        
        TextObjectGroup newGroup = createGroup( text, pos );
        applyBuilderOptions(newGroup);
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
    	return buildSentence(text, new Vector3(x, y), lineLength);
    }
    
    /**
     * Build a tree of TextObjects from the given string.
     *
     * <p>The returned TextObject tree will be laid out so that no more than
     * lineLength characters appear on a single line.  </p>
     */
    public TextObjectGroup buildSentence( String text, Vector3 pos, int lineLength ) {
    	// Pre-process the text
        text = preprocessMessage(text, lineLength);
        
        StringTokenizer st = new StringTokenizer(text," \n",true);
        
        TextObjectGroup newGroup = new TextObjectGroup(pos);
        Vector3 gOffset = new Vector3(0,0,0);
        
        while ( st.hasMoreTokens() ) {
        	// Get each token
            String tokenStr = st.nextToken();
            
            // If the token is a \n begin a new line
            if (tokenStr.equals("\n")) {
            	gOffset.x = pos.x;
            	gOffset.add(lineHeight);
            	continue;
            }
            
            // display other words
            TextObjectGroup token = createGroup( tokenStr, gOffset );
            gOffset.add( new Vector3(token.getBoundingPolygon().getBounds().width+trackingOffset.x, 0, 0) );
            
            newGroup.attachChild( token );
        }
        
        applyBuilderOptions(newGroup);        
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
    			tokenStr = splitLongWord(tokenStr, lineLength);
    			numChars = 0;
    		}
    		else {
    			if (numChars == 0 && tokenStr.equals(" ")) continue;
    			numChars += tokenStr.length();
    			if (numChars > lineLength) {
    				tokenStr = "\n" + tokenStr;
    				numChars = tokenStr.length();
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
    private String splitLongWord(String longWord, int maxLength) {
		// the word is too long, split it
		StringBuffer temp = new StringBuffer("\n");
		Matcher matcher;
		
		while (longWord.length() > maxLength) {
			// find if there is non-word character with maxLength
			matcher = pattern.matcher(longWord.substring(0, maxLength - 1));
			int splitAt = (matcher.find())? matcher.end() : maxLength - 1;
			
			temp.append(longWord.substring(0, splitAt) + "-\n");
			longWord = longWord.substring(splitAt);
			
		}
		return temp.append(longWord + "\n").toString();
    }
    
    /**
     * This methods returns a TextObject group created using the given string,
     * positioned using the given Vector3.
     */
    private TextObjectGroup createGroup( String text, Vector3 pos ) {
        
        TextObjectGroup newGroup = new TextObjectGroup(groupProperties, pos);

        // Each glyph is offset by gOffset from the word location.
        Vector3 gOffset = new Vector3(0,0,0);
        for (int i = 0; i < text.length(); i++) {
            
            String glyph = text.substring(i,i+1);
            
            TextObjectGlyph to = 
                new TextObjectGlyph(glyph, font, glyphProperties, gOffset);

			gOffset.x += to.getLogicalBounds().getWidth()+trackingOffset.x;

            newGroup.attachChild(to);
        }
        return newGroup;
    }

    /**
     * Applies the builder's parameters. 
     */
    private void applyBuilderOptions( TextObjectGroup newGroup ) {
        
        if (centerGroup) {
            // Move children of the group to make the position its center,
            // by adding the offset from the center of the bounding box to
            // the group position, to the position of each glyph.
            Rectangle bb = newGroup.getBoundingPolygon().getBounds();
            Vector3 offset = newGroup.getPositionAbsolute();
            offset.sub(new Vector3(bb.getCenterX(), bb.getCenterY()));
            TextObject child = newGroup.getLeftMostChild();
            while (child != null) {
                child.getPosition().add(offset);
                child = child.getRightSibling();
            }
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

            Iterator i = groupBehaviours.iterator();
            while (i.hasNext()) {
                ((AbstractBehaviour) i.next()).addObject(newGroup);
            }

            TextObject child = newGroup.getLeftMostChild();
            while (child != null) {
            	if (child instanceof TextObjectGlyph) {
            		Iterator bI = glyphBehaviours.iterator();
            		while (bI.hasNext()) {
            			((AbstractBehaviour) bI.next()).addObject(child);
            		}
            	} else {
            		// we can assume grandChild is a TextObjectGlyph because of the way build() and buildSentence() work
            		TextObject grandChild = ((TextObjectGroup)child).getLeftMostChild();
            		while (grandChild != null) {
            			Iterator bI = glyphBehaviours.iterator();
                		while (bI.hasNext()) {
                			((AbstractBehaviour) bI.next()).addObject(grandChild);
                		}
                		grandChild = grandChild.getRightSibling();
            		}
            	}
                child = child.getRightSibling();
            }
        }
    }
    
    public void setLineHeight(double d) { lineHeight.y = d; }
    public double getLineHeight() { return lineHeight.y; }
    public void setTrackingOffset(double d) { trackingOffset.x = d; }
    public double getTrackingOffset() { return trackingOffset.x; }
}
