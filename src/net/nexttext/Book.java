//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;

import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.input.InputManager;
import net.nexttext.processing.FontManager;
import net.nexttext.processing.ProcessingMouse;
import net.nexttext.processing.renderer.Processing2DRenderer;
import net.nexttext.property.ColorProperty;
import net.nexttext.property.StrokeProperty;

/**
 * The container for the NextText for Processing data and window.
 * <p>The PApplet which uses NextText uses this as its primary point of
 * entry into the NextText data.  Most importantly, it contains the TextObjects
 * which hold the core text data, and the TextRenderer and window to actually
 * draw the stuff.  </p>
 * <p>The Book has its own step(), draw(), and stepAndDraw() methods which replace
 * the Simulator loop found in regular NextText applications.</p>
 * <p>Any updates to the TextObject tree must be synchronized on the Book. </p>
 */
 /* $Id$ */
public class Book {

	public static TextObjectBuilder toBuilder;
    public static ProcessingMouse mouse;
    
    private PApplet pApplet;   

    /**
     * The current frame count.
     */
    long frameCount = 0;
	
    /** The frame number, incremented each frame by the Simulator. */
    public long getFrameCount() { return frameCount; }

    /** The frame number, incremented each frame by the Simulator. */
    public void incrementFrameCount() { frameCount++; }
	
    protected LinkedHashMap pages;
    protected Processing2DRenderer renderer;
    protected List behaviourList;
    protected TextObjectRoot textRoot;	// the root of the TextObject hierarchy
    protected InputManager inputs;
    protected SpatialList spatialList;
    
    
    /**
     * Instantiates the Book.
     * 
     * @param pApplet the parent PApplet
     */
    public Book(PApplet pApplet) {
    	// initialize the core objects 
        renderer = new Processing2DRenderer(pApplet); 
    	pages = new LinkedHashMap();
    	behaviourList = new LinkedList();
    	textRoot = new TextObjectRoot(this);
        inputs = new InputManager(renderer.getCanvas());
        spatialList = new SpatialList();
        
        this.pApplet = pApplet;
        
        // create a default text page
        TextPage defaultTextPage = new TextPage(this);
        addPage("Default Text Page", defaultTextPage);
        
        // initialize the TextObjectBuilder
        toBuilder = new TextObjectBuilder(this, defaultTextPage);
        // initialize the ProcessingMouse
        mouse = new ProcessingMouse(pApplet);
    }
    
	///////////////////////////////////////////////////////////////////////////
	
	private Set objectsToRemove = new HashSet();

    /**
     * Remove a text object and any children from the tree.
     *
     * <p>This method will:</p>
     * <ul><li>Remove the objects from the spatial list</li>
     * <li>Remove the objects from any behaviours</li>
     * <li>Remove the objects from the text object hierarchy</li>
     * </ul>
     *
     * <p>The object is not removed immediately, it is removed prior to
     * starting the next simulator run.  This means that behaviours can safely
     * call this method without worrying that the object will disappear and
     * mess up other behaviours in the chain.  </p>
     */

	public void removeObject(TextObject to) {
	    objectsToRemove.add(to);
	}
	
	/**
	 * Removes all objects that have been marked for deletion.  
	 * 
	 * <p>Do not call this method while iterating over the TextObjectRoot 
	 * for synchronization reasons. </p> 
	 */
	protected void removeQueuedObjects() {
	    
	    if ( objectsToRemove.size() > 0 ) {
	        
	        for ( Iterator i = objectsToRemove.iterator(); i.hasNext(); ) {
                TextObject next = (TextObject) i.next();
                if (next instanceof TextObjectGlyph) {
                    removeObjectInner(next);
                } else if (next instanceof TextObjectGroup) {
                    TextObjectIterator toi = ((TextObjectGroup) next).iterator();
                    while (toi.hasNext()) {
                        removeObjectInner(toi.next());
                    }
                } else {
                    throw new RuntimeException("Unexpected TextObject subtype");
                }
	        }

	        objectsToRemove.clear();
	    }
	}

	/**
	 * See removeObject(TextObject to)
	 */
	private synchronized void removeObjectInner(TextObject to) {
		// remove the object from the spatial list
		getSpatialList().remove( to );
		// traverse the behaviour list.  try to remove the object from each
		// active behaviour
    	Iterator i = getBehaviourList().iterator();
    	while (i.hasNext()) {
    	    AbstractBehaviour b = (AbstractBehaviour)i.next();
    	    b.removeObject(to);    	        	    					
    	}
		// detach the object from the tree
		to.detach();			
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Step and draw methods 
	
	/**
     * Applies all active behaviours.
     * <p>This code is adapted from Simulator.run().</p>
     */
    public synchronized void step() {
        // apply the behaviours
        Iterator i = behaviourList.iterator();
        while (i.hasNext()) {
            AbstractBehaviour b = (AbstractBehaviour)i.next();
            b.behaveAll(); 
        }

        // remove all objects flagged for deletion 
        removeQueuedObjects();

        // the new frame is calculated, so make it the current frame
        incrementFrameCount();

        // update the spatial list
        spatialList.update();
    }
    
    /**
     * Renders a frame.
     */
    public void draw() {
        renderer.renderPages(getPages());
    }
    
    /**
     * Renders the given page for this frame.
     * 
     * @param pageName the name of the Page to render
     */
    public void drawPage(String pageName) {
    	renderer.renderPage(getPage(pageName));
    }
    
    /**
     * Goes through a full iteration of the Book loop, i.e. applies all behaviours
     * and then renders a frame.
     */
    public void stepAndDraw() {
    	step();
    	draw();
    }
    
    ///////////////////////////////////////////////////////////////////////////
	// Font loading and setting methods 
	
    
    /**
     * Loads the font with the given filename from the 'data' folder of the sketch.
     * 
     * @param filename
     * 
     * @return the loaded font
     */
    public Font loadFont(String filename) {
    	return FontManager.loadFont(pApplet, filename);
    }
    
    /**
     * Sets the font of the TextObjectBuilder.
     * 
     * @param font font to derive
     * @param size font size
     */
    public void textFont(Font font, float size) {
        toBuilder.setFont(FontManager.deriveFont(font, size));
    }
    
    /**
     * Sets the font of the TextObjectBuilder.
     * 
     * @param font font to derive
     * @param size font size
     * @param italic whether the font is italic
     * @param bold whether the font is bold
     */
    public void textFont(Font font, float size, boolean italic, boolean bold) {
        toBuilder.setFont(FontManager.deriveFont(font, size, italic, bold));
    }
    
    ///////////////////////////////////////////////////////////////////////////
	// Text building methods 
    
    /**
     * Builds a tree of TextObjects from the given string, at the specified 
     * location, using the stroke and fill colors set in the PApplet.
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * 
     * @return TextObjectGroup the built TextObjectGroup
     */
    public TextObjectGroup addText(String text, int x, int y) {
    	TextObjectGroup newTog = toBuilder.build(text, x, y);
    	setStrokeAndFill(newTog); 

        return newTog;
    }
    
    /**
     * Builds a tree of TextObjects on the given Page from the given string, 
     * at the specified location, using the stroke and fill colors set in 
     * the PApplet.
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * @param pageName the name of the Page to build on
     * 
     * @return TextObjectGroup the built TextObjectGroup
     */
    public TextObjectGroup addText(String text, int x, int y, String pageName) {
    	TextObjectGroup tempTog = toBuilder.getParent();
    	toBuilder.setParent(((TextPage)getPage(pageName)).getTextRoot());
    	TextObjectGroup newTog = addText(text, x, y);
    	toBuilder.setParent(tempTog);
    	return newTog;
    }
    
    /**
     * Builds a tree of TextObjects from the given string, at the specified
     * location, using the stroke and fill colors set in the PApplet.
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * @param lineLength the max number of characters per line
     * 
     * @return TextObjectGroup the built TextObjectGroup
     */
    public TextObjectGroup addText(String text, int x, int y, int lineLength) {
    	TextObjectGroup newTog = toBuilder.buildSentence(text, x, y, lineLength);
    	setStrokeAndFill(newTog);

        return newTog;
    }
    
    /**
     * Builds a tree of TextObjects on the given Page from the given string, 
     * at the specified location, using the stroke and fill colors set in 
     * the PApplet.
     * 
     * @param text the String to create the TextObjectGroup from
     * @param x the x-coordinate of the created TextObjectGroup
     * @param y the y-coordinate of the created TextObjectGroup
     * @param lineLength the max number of characters per line
     * @param pageName the name of the Page to build on
     * 
     * @return TextObjectGroup the built TextObjectGroup
     */
    public TextObjectGroup addText(String text, int x, int y, int lineLength, String pageName) {
    	TextObjectGroup tempTog = toBuilder.getParent();
    	toBuilder.setParent(((TextPage)getPage(pageName)).getTextRoot());
    	TextObjectGroup newTog = addText(text, x, y, lineLength);
    	toBuilder.setParent(tempTog);
    	return newTog;
    }
    
    /**
     * Sets the stroke and fill properties based on the colors set in the PApplet.
     * 
     * @param to the TextObject to apply the color properties to
     */
    private void setStrokeAndFill(TextObject to) {
    	setStroke(to);
    	setFill(to);
    }
    
    /**
     * Sets the stroke properties based on the colors set in the PApplet.
     * 
     * @param to the TextObject to apply the color properties to
     */
    private void setStroke(TextObject to) {
    	ColorProperty colProp = to.getStrokeColor();
    	
    	if (pApplet.g.stroke) {
    		// set the stroke cap
    		int cap;
	    	switch (pApplet.g.strokeCap) {
	    	case PApplet.SQUARE:
	    		cap = BasicStroke.CAP_BUTT;
	    		break;
	    	case PApplet.PROJECT:
	    		cap = BasicStroke.CAP_SQUARE;
	    		break;
	    	default:
	    		cap = BasicStroke.CAP_ROUND;
	    		break;
	    	}
	    	
	    	// set the stroke join
	    	int join;
	    	switch (pApplet.g.strokeJoin) {
	    	case PApplet.BEVEL:
	    		join = BasicStroke.JOIN_BEVEL;
	    		break;
	    	case PApplet.ROUND:
	    		join = BasicStroke.JOIN_ROUND;
	    		break;
	    	default:
	    		join = BasicStroke.JOIN_MITER;
	    		break;
	    	}
    	
	    	// set the stroke property
	    	StrokeProperty strokeProp = to.getStroke();
	    	strokeProp.setOriginal(new BasicStroke(pApplet.g.strokeWeight, cap, join));
	    	strokeProp.set(new BasicStroke(pApplet.g.strokeWeight, cap, join));
    		
	    	// set the stroke color property
	    	colProp.setOriginal(new Color(pApplet.g.strokeColor, true));
	    	colProp.set(new Color(pApplet.g.strokeColor, true));
            
    	} else {
    		// set the stroke color property to transparent
    		colProp.setOriginal(new Color(0, 0, 0, 0));
	    	colProp.set(new Color(0, 0, 0, 0));
        }
    }
    
    /**
     * Sets the fill properties based on the colors set in the PApplet.
     * 
     * @param to the TextObject to apply the color properties to
     */
    private void setFill(TextObject to) {
    	ColorProperty colProp = to.getColor();
    	
    	if (pApplet.g.fill) {
            // set the fill color property
    		colProp.setOriginal(new Color(pApplet.g.fillColor, true));
    		colProp.set(new Color(pApplet.g.fillColor, true));
        } else {
        	// set the fill color property to transparent
        	colProp.setOriginal(new Color(0, 0, 0, 0));
        	colProp.set(new Color(0, 0, 0, 0));
        }
    }
    
    public void attachText(TextObject to) {
    	toBuilder.getParent().attachChild(to);
    }
    
    public void attachText(TextObject to, String pageName) {
    	TextObjectGroup tempTog = toBuilder.getParent();
    	toBuilder.setParent(((TextPage)getPage(pageName)).getTextRoot());
    	attachText(to);
    	toBuilder.setParent(tempTog);
    }
    
    ///////////////////////////////////////////////////////////////////////////
	// Behaviour management methods 
    
    /**
     * Adds the given Behaviour to the list of Behaviours applied to new TextObjectGlyphs.
     * <p>The Behaviour will only be added to TextObjects created after this method is called.</p>
     * <p>The Behaviour is automatically added to the NTPBook.</p>
     * 
     * @param b the Behaviour to add
     */
    public void addGlyphBehaviour(AbstractBehaviour b) { 
        toBuilder.addGlyphBehaviour(b);
        addBehaviour(b);
    }
    
    /**
     * Removes the given Behaviour from the list of Behaviours applied to new TextObjectGlyphs.
     * <p>The Behaviour will not be added to TextObjects created after this method is called.</p>
     * 
     * @param b the Behaviour to remove
     */
    public void removeGlyphBehaviour(AbstractBehaviour b) {
        toBuilder.removeGlyphBehaviour(b);
    }
    
    /**
     * Removes all Behaviours from the list of Behaviours applied to new TextObjectGlyphs.
     */
    public void removeAllGlyphBehaviours() {
        toBuilder.removeAllGlyphBehaviours();
    }
    
    /**
     * Adds the given Behaviour to the list of Behaviours applied to new TextObjectGroups.
     * <p>The Behaviour will only be added to TextObjects created after this method is called.</p>
     * <p>The Behaviour is automatically added to the NTPBook.</p>
     * 
     * @param b the Behaviour to add
     */
    public void addGroupBehaviour(AbstractBehaviour b) { 
        toBuilder.addGroupBehaviour(b);
        addBehaviour(b);
    }
    
    /**
     * Removes the given Behaviour from the list of Behaviours applied to new TextObjectGroups.
     * <p>The Behaviour will not be added to TextObjects created after this method is called.</p>
     * 
     * @param b the Behaviour to remove
     */
    public void removeGroupBehaviour(AbstractBehaviour b) {
        toBuilder.removeGroupBehaviour(b);
    }
    
    /**
     * Removes all Behaviours from the list of Behaviours applied to new TextObjectGroups.
     */
    public void removeAllGroupBehaviours() {
        toBuilder.removeAllGroupBehaviours();
    }
	
	///////////////////////////////////////////////////////////////////////////
	// Get methods
	
    /** Returns the page renderer */
	public PageRenderer getRenderer() { return renderer; }
    /** Returns the page set */
    public Collection getPages() { return pages.values(); }
	/** Returns the BehaviourList */
	public List getBehaviourList() { return behaviourList; };
	/** 
     * Get the root of the TextObject hierarchy.  Any changes to this tree
     * must be synchronized on the book. Do not add textObjects as children of 
	 * the textRoot directly because they will not be rendered, add them to a TextPage instead.
     */
	public TextObjectRoot getTextRoot() { return textRoot; }
	/** Returns the Input Manager */
	public InputManager getInputs() { return inputs; }
	/** Returns the Spatial List */
	public SpatialList getSpatialList() { return spatialList; }
	
    /**
     * Add a page to the book without specifying a name.
     * 
     * <p>The page will be named "layerN" where N = 0,1,2,3...</p>
     * 
     */
    public void addPage(Page p){
        String name = "layer" + pages.size();
        pages.put(name,p);
    }

    /**
     * Add a named page to the book
     */
    public void addPage(String name, Page p){
        if (pages.containsKey(name)) {
        	log("WARNING: A Page with the name '"+name+"' already exists and will be deleted!");
        }
        pages.put(name,p);
    }
    
    /**
     * Create and add a named TextPage to the Book
     * 
     * @param pageName the name of the TextPage to add
     */
    public void addPage(String pageName) {
    	addPage(pageName, new TextPage(this));
    }
    
    /**
     * Get a named page from the book
     */
    public Page getPage(String name){
        return (Page)pages.get(name);
    }
	
	/**
	 * Adds a Behaviour to the Book.
	 */
	public void addBehaviour( AbstractBehaviour b ) {
	    behaviourList.add(b);
	}
	
	/**
	 * Removes a Behaviour from the Book.
	 */
	public void removeBehaviour( AbstractBehaviour b ) {
	    behaviourList.remove(b);
	}
    
    /**
     * Removes the children of this textObject from the book
     * @param to
     */
    public void removeChildren( TextObjectGroup to ){
        TextObject child = to.getLeftMostChild();
        while (child != null) {            
            removeObject(child);
            child = child.getRightSibling();
        }
    }
    
    /**
     * Removes all the TextObjects from all the TextPages in the Book, except for all the TextObjectRoots.
     */
    public void clear() {
    	Iterator i = pages.values().iterator();
        while (i.hasNext()) {
        	Page page = (Page)i.next();
        	if (page instanceof TextPage) {
        		TextPage textPage = (TextPage)page;
        		removeChildren(textPage.getTextRoot());
        	}
        }
    }
    
    /**
     * Removes all the TextObjects from the given TextPage, except for the TextObjectRoot.
     * @param pageName the name of the TextPage to clear
     */
    public void clearPage(String pageName) {
    	if (pages.get(pageName) instanceof TextPage) {
    		TextPage textPage = (TextPage)pages.get(pageName);
    		removeChildren(textPage.getTextRoot());
    	}
    }
    
    // font property setters and getters
    public void setLineHeight(double d) { toBuilder.setLineHeight(d); }
    public double getLineHeight() { return toBuilder.getLineHeight(); }
    public void setTrackingOffset(double d) { toBuilder.setTrackingOffset(d); }
    public double getTrackingOffset() { return toBuilder.getTrackingOffset(); }

    //////////////////////////////////////////////////////////////////////
    // Rudimentary Logging System

    PrintWriter logWriter = null;

    /**
     * Specify a PrintWriter to which all log messages will be written.
     *
     * <p>NextText generated messages are written to this PrintWriter.  </p>
     */
    public void setLogger(PrintWriter pw) {
        logWriter = pw;
    }

    /**
     * Log a message.
     *
     * <p>The message will go to standard output, as well as a log file if one
     * has been specified with setLogFile().  A newline will be appended to the
     * message.  </p>
     */
    public void log(String message) {
        System.out.println(message);
        if (logWriter != null) {
            synchronized (logWriter) {
                logWriter.println(message);
                logWriter.flush();
            }
        }
    }

    public void log(String message, Throwable t) {
        System.out.println(message);
        t.printStackTrace();
        if (logWriter != null) {
            synchronized (logWriter) {
                logWriter.println(message);
                t.printStackTrace(logWriter);
                logWriter.flush();
            }
        }
    }
}
