package net.nexttext.processing.renderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Iterator;

import net.nexttext.*;

import processing.core.*;

/**
 * This is a renderer that uses the Processing PApplet's Graphics2D object to 
 * perform all the drawing operations.
 */
public class Processing2DRenderer implements PageRenderer {

    static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/processing/renderer/Attic/Processing2DRenderer.java,v 1.2.2.2 2007/09/22 16:06:22 elie Exp $";
    
    private PApplet pApplet;
    private Graphics2D g2;
    
    /**
     * Builds a Processing2DRenderer
     * 
     * @param pApplet the parent PApplet
     */
    public Processing2DRenderer(PApplet pApplet) {
        this.pApplet = pApplet;
        g2 = ((PGraphicsJava2D)pApplet.g).g2;
    }
    
    /**
     * Renders the given Pages on the PApplet's Graphics
     * 
     * @param pages the Pages to render
     */
    public void renderPages(Collection pages) {
    	// render the pages
    	Iterator i = pages.iterator();
        while (i.hasNext()) {
            Page page = (Page)i.next();
            renderPage(page);      
        }
    }
    
    /**
     * Renders the given Page on the PApplet's Graphics
     * 
     * @param page the Page to render
     */
    public void renderPage(Page page) {
    	AffineTransform original = g2.getTransform();
        page.render(g2, this.getCanvas());
        g2.setTransform(original);
    }
    
    /**
     * Returns the PApplet used for drawing
     *
     * @return Component the drawing surface
     */
    public Component getCanvas() { 
        return pApplet;
    }
    
    /**
     * Returns the PApplet 
     * 
     * @return the PApplet
     */
    public PApplet getPApplet() {
        return pApplet;
    }
}
