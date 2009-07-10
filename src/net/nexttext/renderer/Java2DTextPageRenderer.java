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

package net.nexttext.renderer;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.Stack;

import processing.core.*;

import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextPage;
import net.nexttext.Vector3;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.Vector3PropertyList;

/**
 * 
 * Renders the text stored in a text page.
 * 
 * <p>
 * This TextPage renderer is based on the Java2D API.
 * </p>
 * 
 */
/* $Id$ */
public class Java2DTextPageRenderer extends G2DTextPageRenderer {

    public Java2DTextPageRenderer(PApplet p) throws ClassCastException {
        super(p);
        this.g2 = ((PGraphicsJava2D)p.g).g2;
    }

    /**
     * The rendering loop. Takes as input a TextPage and traverses its root
     * node, rendering all the TextObjectGlyph objects along the way.
     * 
     * @param textPage the TextPage to render
     */
    public void renderPage(TextPage textPage) {
        // When resizing, it's possible to lose the reference to the graphics
        // context, so we skip rendering the frame.
        if (g2 == null) {
            System.out.println(("Skip rendering frame because the graphics context was lost temporarily."));
        }

        else if (textPage.getTextRoot() == null) {
            System.out.println("TextPage: No root specified yet");
        } 
        
        // traverse the TextObject hierarchy
        else {
            AffineTransform original = g2.getTransform();
            enterCoords(textPage);
            traverse(textPage.getTextRoot());
            exitCoords(textPage);
            g2.setTransform(original);
        }
    } // end rendering
}