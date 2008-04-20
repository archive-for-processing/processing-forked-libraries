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
/* $Id$ */
public class Processing2DRenderer implements PageRenderer {
    
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
