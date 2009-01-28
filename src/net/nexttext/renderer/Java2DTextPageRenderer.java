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
public class Java2DTextPageRenderer extends TextPageRenderer {
    protected Graphics2D g2;

    public Java2DTextPageRenderer(PApplet p) throws ClassCastException {
        super(p);
        this.g2 = ((PGraphicsJava2D)p.g).g2;
    }

    /**
     * 
     * The rendering loop. Takes as input a TextPage and traverses its root
     * node, rendering all the TextObjectGlyph objects along the way.
     * 
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
            traverse(textPage.getTextRoot());
            g2.setTransform(original);
        }
    } // end rendering

    /**
     * Traverse the TextObject tree and render all of its glyphs.
     */
    protected void traverse(TextObject root) {
        // The tree is traversed using a variable to point at the current node
        // being processed. TextObjects specify their rotation and position
        // relative to their parent, which is handled by registering coordinate
        // system changes with the Graphics2D object as the tree is traversed.

        // Currently rendering is not synchronized with modifications to the
        // TextObjectTree. This is dodgy, but gives a performance boost, so
        // will stay that way for the moment. However, it affects this method,
        // because we can't assume that the tree is always well structured.

        // Transformations are stored in a stack so that they can be undone as
        // needed. It is not appropriate to use the position of the TextObject
        // to undo the transformation, because this may have changed due to the
        // lack of synchronization.

        TextObject current = root;
        Stack coordTransforms = new Stack();
        do {
            // Draw any glyphs.
            if (current instanceof TextObjectGlyph) {
                enterCoords(coordTransforms, current);
                renderGlyph((TextObjectGlyph) current);
                exitCoords(coordTransforms);
            }

            // Descend to process any children
            if (current instanceof TextObjectGroup) {
                TextObjectGroup tog = (TextObjectGroup) current;
                TextObject child = tog.getLeftMostChild();
                if (child != null) {
                    enterCoords(coordTransforms, current);
                    current = child;
                    continue;
                }
            }

            // Processing of this node is complete, so move on to siblings.
            // Since a node may not have siblings, a search is made up the tree
            // for the first appropriate sibling. The search ends if a sibling
            // is found, or if it reaches the top of the tree.
            while (current != root) {
                TextObject sibling = current.getRightSibling();
                if (sibling != null) {
                    current = sibling;
                    break;
                } else {
                    current = current.getParent();
                    if (current == null) {
                        // Aaarghh, we were detached from the tree mid-render,
                        // so just abort the whole process.
                        return;
                    }
                    exitCoords(coordTransforms);
                }
            }
        } while (current != root);
    }

    /**
     * Transform the Graphics2D into the coordinates of the given TextObject.
     * 
     * <p>
     * Once this transformation is done, the TextObject and any of it's children
     * can be written directly to the Graphics2D without having to handle
     * position or rotation.
     * </p>
     */
    protected void enterCoords(Stack ct, TextObject node) {
        Vector3 pos = node.getPosition().get();
        g2.translate(pos.x, pos.y);
        ct.push(pos);

        double rotation = node.getRotation().get();
        g2.rotate(rotation);
        ct.push(new Double(rotation));
    }

    /**
     * Transform the Graphics2D out of the coordinates on top of the stack.
     * 
     * <p>
     * This undoes the change of enterCoords().
     * </p>
     */
    protected void exitCoords(Stack ct) {
        double rotation = ((Double) ct.pop()).doubleValue();
        g2.rotate(-rotation);

        Vector3 pos = (Vector3) ct.pop();
        g2.translate(-pos.x, -pos.y);
    }

    /**
     * Renders a TextObjectGlyph using quads, either as an outline or as a
     * filled shape.
     * 
     * @param glyph
     *            The TextObjectGlyph
     */
    protected void renderGlyph(TextObjectGlyph glyph) {

        // ////////////////////////////////////
        // Optimize based on presence of DForms and of outlines
        if (glyph.isDeformed() || glyph.isStroked()) {

            // ////////////////////////////////
            // Render glyph using vertex list

            // Use the cached path if possible.
            GeneralPath gp = (GeneralPath) glyph.rendererCache;

            if (gp == null) {
                // we need to rebuild the cache
                // get the list of vertices for this glyph
                Vector3PropertyList vertices = (Vector3PropertyList) glyph
                        .getProperty("Control Points");
                // create a new GeneralPath to hold the vector outline
                gp = new GeneralPath();
                // get an iterator for the list of contours
                Iterator it = glyph.contours.iterator();

                // process each contour
                while (it.hasNext()) {

                    // get the list of vertices for this contour
                    int contour[] = (int[]) it.next();

                    Vector3Property firstPoint = vertices.get(contour[0]);
                    // move the pen to the begining of the contour
                    gp.moveTo((float) firstPoint.getX(), (float) firstPoint
                            .getY());

                    // generate all the quads forming the line
                    for (int i = 1; i < contour.length; i++) {

                        Vector3Property current = vertices.get(contour[i]);
                        Vector3Property next;

                        // Since it's a closed contour, the last vertex's next
                        // is the first vertex.
                        if (i == contour.length - 1)
                            next = vertices.get(contour[0]);
                        else
                            next = vertices.get(contour[i + 1]);

                        float anchorx = (float) (current.getX() + next.getX()) / 2;
                        float anchory = (float) (current.getY() + next.getY()) / 2;

                        gp.quadTo((float) current.getX(), (float) current
                                .getY(), anchorx, anchory);
                    }
                    // close the path
                    gp.closePath();
                    // cache it
                    glyph.rendererCache = gp;
                } // end while
            }

            // draw the outline of the shape
            if (glyph.isStroked()) {
                g2.setColor(glyph.getStrokeColorAbsolute());
                g2.setStroke(glyph.getStrokeAbsolute());
                g2.draw(gp);
            }

            // fill the shape
            if (glyph.isFilled()) {
                g2.setColor(glyph.getColorAbsolute());
                g2.fill(gp);
            }
        } else {
            // /////////////////////////////////////////
            // Render glyph using Graphics.drawString()
            g2.setColor(glyph.getColorAbsolute());
            // set the font
            g2.setFont(Book.loadFontFromPFont(glyph.getFont()));
            // draw the glyph
            g2.drawString(glyph.getGlyph(), 0, 0);
        }

    } // end renderGlyph

}