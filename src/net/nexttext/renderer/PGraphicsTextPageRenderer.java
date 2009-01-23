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

import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Iterator;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextPage;
import net.nexttext.Vector3;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.Vector3PropertyList;

import processing.core.*;

/**
 * Renders the text stored in a TextPage as glyphs.
 * 
 * <p>This TextPage renderer uses the parent PApplet's native drawing functions
 * to render glyphs. If the glyphs to be rendered are deformed, their shapes are
 * tesselated using the GLU tesselator from the OpenGL library.</p>
 */
/* $Id$ */
public class PGraphicsTextPageRenderer extends TextPageRenderer {
    protected GLU glu;
    protected TessCallback tessCallback;
    protected GLUtessellator tobj;

    //detail of flattened bezier curves
	protected int bezierDetail;

    /**
     * Builds a TextPageRenderer.
     * 
     * @param p the parent PApplet
     */
    public PGraphicsTextPageRenderer(PApplet p) throws NoClassDefFoundError {
        super(p);
        
        glu = new GLU();
        tobj = glu.gluNewTess();
        tessCallback = new TessCallback(); 
        glu.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback); 
        glu.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback); 
        glu.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback); 
        glu.gluTessCallback(tobj, GLU.GLU_TESS_COMBINE, tessCallback); 
        glu.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback); 
        
        bezierDetail = 4;
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
        if (p.g == null) {
            System.out.println(("Skipping rendering frame because the graphics context was lost temporarily."));
        }

        else if (textPage.getTextRoot() == null) {
            System.out.println("TextPage: No root specified yet");
        } 

        // traverse the TextObject hierarchy
        else {
            traverse(textPage.getTextRoot());
        }
    }

    /**
     * Traverse the TextObject tree to render all of its glyphs.
     * 
     * <p>The tree is traversed using a variable to point at the current node
     * being processed. TextObjects specify their rotation and position
     * relative to their parent, which is handled by registering coordinate
     * system changes with the drawing surface as the tree is traversed.</p>
     * 
     * Currently, rendering is not synchronized with modifications to the
     * TextObjectTree. This is dodgy, but gives a performance boost, so will 
     * stay that way for the moment. However, it affects this method, because we
     * can't assume that the tree is always well structured.</p>
     * 
     * <p>Transformations are stored in a stack so that they can be undone as
     * needed. It is not appropriate to use the position of the TextObject to 
     * undo the transformation, because this may have changed due to the lack of
     * synchronization.</p>
     * 
     * @param root the TextObject node to traverse
     */
    protected void traverse(TextObject root) {
        TextObject current = root;
        do {
            // Draw any glyphs
            if (current instanceof TextObjectGlyph) {
                enterCoords(current);
                renderGlyph((TextObjectGlyph) current);
                exitCoords();
            }

            // Descend to process any children
            if (current instanceof TextObjectGroup) {
                TextObjectGroup tog = (TextObjectGroup) current;
                TextObject child = tog.getLeftMostChild();
                if (child != null) {
                    enterCoords(current);
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
                    exitCoords();
                }
            }
        } while (current != root);
    }

    /**
     * Transform the drawing surface into the coordinates of the given 
     * TextObject.
     * 
     * <p>Once this transformation is done, the TextObject and any of its 
     * children can be drawn directly to the PApplet without having to handle
     * position or rotation.</p>
     * 
     * @param node the TextObject holding the translation and rotation info
     */
    protected void enterCoords(TextObject node) {
        p.pushMatrix();

        // translation
        Vector3 pos = node.getPosition().get();
        p.translate((float)pos.x, (float)pos.y);
        // rotation
        float rotation = (float)node.getRotation().get();
        p.rotate(rotation);
    }

    /**
     * Transform the drawing surface out of the coordinates on top of the stack.
     * 
     * <p>This undoes the change of enterCoords(TextObject node).</p>
     */
    protected void exitCoords() {
        p.popMatrix();
    }

    /**
     * Renders a TextObjectGlyph.
     * 
     * @param glyph The TextObjectGlyph to render
     */
    protected void renderGlyph(TextObjectGlyph glyph) {
        // save the current properties
        p.pushStyle();

        // set text properties
        p.textFont(glyph.getFont());
        p.textAlign(PConstants.LEFT, PConstants.BASELINE);
        
        // use the cached path if possible
        GeneralPath gp = (GeneralPath)glyph.rendererCache;
        if ((glyph.isDeformed() || glyph.isStroked()) && gp == null) {
            // we need to rebuild the cache
            
            // get the list of vertices for this glyph
            Vector3PropertyList vertices = (Vector3PropertyList)glyph.getProperty("Control Points");
            // create a new GeneralPath to hold the vector outline
            gp = new GeneralPath();
            // get an iterator for the list of contours
            Iterator it = glyph.contours.iterator();

            // process each contour
            while (it.hasNext()) {
                // get the list of vertices for this contour
                int contour[] = (int[]) it.next();

                Vector3Property firstPoint = vertices.get(contour[0]);
                // move the pen to the beginning of the contour
                gp.moveTo((float)firstPoint.getX(), (float)firstPoint.getY());

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

                    gp.quadTo((float)current.getX(), (float)current.getY(), anchorx, anchory);
                }
                
                // close the path
                gp.closePath();
                // cache it
                glyph.rendererCache = gp;
            } // end while
        }

        // optimize rendering based on the presence of DForms and of outlines
        if (glyph.isFilled()) {
            if (glyph.isDeformed()) {
                // fill the shape
                p.noStroke();
                p.fill(glyph.getColorAbsolute().getRGB());
                fillPath(gp);
                
            } else {
                // render glyph using Processing's native PFont drawing method
                p.fill(glyph.getColorAbsolute().getRGB());
                p.text(glyph.getGlyph(), 0, 0);
            }
        }

        if (glyph.isStroked()) {
            // draw the outline of the shape
            p.stroke(glyph.getStrokeColorAbsolute().getRGB());
            BasicStroke bs = glyph.getStrokeAbsolute();
            p.strokeWeight(bs.getLineWidth());
            if (p.g instanceof PGraphicsJava2D) {
                switch (bs.getEndCap()) {
                    case BasicStroke.CAP_ROUND:
                        p.strokeCap(PApplet.ROUND);
                        break;
                    case BasicStroke.CAP_SQUARE:
                        p.strokeCap(PApplet.PROJECT);
                        break;
                    default:
                        p.strokeCap(PApplet.SQUARE);
                    break;
                }
                switch (bs.getLineJoin()) {
                    case BasicStroke.JOIN_ROUND:
                        p.strokeJoin(PApplet.ROUND);
                        break;
                    case BasicStroke.JOIN_BEVEL:
                        p.strokeJoin(PApplet.BEVEL);
                        break;
                    default:
                        p.strokeJoin(PApplet.MITER);
                    break;
                }
            }
            p.noFill();
            strokePath(gp);
        }

        // restore saved properties
        p.popStyle();

    } // end renderGlyph

    /**
     * Strokes the glyph using native Processing drawing functions.
     * 
     * @param gp the outline of the glyph
     */
    protected void strokePath(GeneralPath gp) {
        // six element array received from the Java2D path iterator
        float textPoints[] = new float[6];

        PathIterator iter = gp.getPathIterator(null);

        float lastX = 0;
        float lastY = 0;
 
        double vertex[];
        
        while (!iter.isDone()) {
            int type = iter.currentSegment(textPoints);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    p.beginShape();
                    p.vertex(textPoints[0], textPoints[1]);
                    lastX = textPoints[0];
                    lastY = textPoints[1];
                    break;
                	
                case PathIterator.SEG_QUADTO:

                	for (int i = 1; i < bezierDetail; i++) {
                		float t = (float)i/(float)bezierDetail;
	                    vertex = new double[] {
	                            p.g.bezierPoint(
	                                    lastX, 
	                                    lastX + ((textPoints[0]-lastX)*2/3), 
	                                    textPoints[2] + ((textPoints[0]-textPoints[2])*2/3), 
	                                    textPoints[2], 
	                                    t
	                            ),
	                            p.g.bezierPoint(
	                                    lastY, 
	                                    lastY + ((textPoints[1]-lastY)*2/3),
	                                    textPoints[3] + ((textPoints[1]-textPoints[3])*2/3), 
	                                    textPoints[3], 
	                                    t
	                            ), 
	                            0
	                    };
	                    p.vertex((float)vertex[0], (float)vertex[1]);
                	}

                    lastX = textPoints[2];
                    lastY = textPoints[3];
                    break;

                case PathIterator.SEG_CLOSE:
                    p.endShape(PConstants.CLOSE);
                    
                    break;
            }
            
            iter.next();
        }
        
        p.endShape(PConstants.CLOSE);
    }

    /**
     * Fills the glyph using the tesselator.
     * 
     * @param gp the outline of the glyph
     */
    protected void fillPath(GeneralPath gp) {
        // save the current smooth property
        boolean smooth = p.g.smooth;
        // turn off smoothing so that we don't get gaps in between the triangles
        p.noSmooth();
        
        // six element array received from the Java2D path iterator
        float textPoints[] = new float[6];

        PathIterator iter = gp.getPathIterator(null);

        glu.gluTessBeginPolygon(tobj, null);
        // second param to gluTessVertex is for a user defined object that contains
        // additional info about this point, but that's not needed for anything

        float lastX = 0;
        float lastY = 0;

        // unfortunately the tesselator won't work properly unless a
        // new array of doubles is allocated for each point. that bites ass,
        // but also just reaffirms that in order to make things fast,
        // display lists will be the way to go.
        double vertex[];

        while (!iter.isDone()) {
            int type = iter.currentSegment(textPoints);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    glu.gluTessBeginContour(tobj);

                    vertex = new double[] {
                            textPoints[0], 
                            textPoints[1], 
                            0
                    };
                    
                    glu.gluTessVertex(tobj, vertex, 0, vertex);
                    
                    lastX = textPoints[0];
                    lastY = textPoints[1];
                    
                    break;

                case PathIterator.SEG_QUADTO:   // 2 points
                	
                	for (int i = 1; i < bezierDetail; i++) {
                		float t = (float)i/(float)bezierDetail;
	                    vertex = new double[] {
	                            p.g.bezierPoint(
	                                    lastX, 
	                                    lastX + ((textPoints[0]-lastX)*2/3), 
	                                    textPoints[2] + ((textPoints[0]-textPoints[2])*2/3), 
	                                    textPoints[2], 
	                                    t
	                            ),
	                            p.g.bezierPoint(
	                                    lastY, 
	                                    lastY + ((textPoints[1]-lastY)*2/3),
	                                    textPoints[3] + ((textPoints[1]-textPoints[3])*2/3), 
	                                    textPoints[3], 
	                                    t
	                            ), 
	                            0
	                    };
	                    
	                    glu.gluTessVertex(tobj, vertex, 0, vertex);
                	}
                    
                    lastX = textPoints[2];
                    lastY = textPoints[3];
                    
                    break;

                case PathIterator.SEG_CLOSE:
                    glu.gluTessEndContour(tobj);
                    
                    break;
            }
            
            iter.next();
        }
        
        glu.gluTessEndPolygon(tobj);
        
        // restore saved smooth property
        if (smooth) p.smooth();
    }

    /**
     * This tesselator callback uses native Processing drawing functions to 
     * execute the incoming commands.
     */
    public class TessCallback extends GLUtessellatorCallbackAdapter {
        
        public void begin(int type) {
            switch (type) {
                case GL.GL_TRIANGLE_FAN: 
                    p.beginShape(PApplet.TRIANGLE_FAN); 
                    break;
                case GL.GL_TRIANGLE_STRIP: 
                    p.beginShape(PApplet.TRIANGLE_STRIP); 
                    break;
                case GL.GL_TRIANGLES: 
                    p.beginShape(PApplet.TRIANGLES); 
                    break;
            }
        }

        public void end() {
            p.endShape();
        }

        public void vertex(Object data) {
            if (data instanceof double[]) {
                double[] d = (double[]) data;
                if (d.length != 3) {
                    throw new RuntimeException("TessCallback vertex() data " +
                    "isn't length 3");
                }

                //if (p.g instanceof PGraphics3D) {
                //    p.vertex((float) d[0], (float) d[1], (float) d[2]);

                //} else {
                    // assume it is 2D, ignore z
                    p.vertex((float) d[0], (float) d[1]);
                //}
                    
            } else {
                throw new RuntimeException("TessCallback vertex() data not understood");
            }
        }

        public void error(int errnum) {
            String estring = glu.gluErrorString(errnum);
            throw new RuntimeException("Tessellation Error: " + estring);
        }

        /**
         * Implementation of the GLU_TESS_COMBINE callback.
         * @param coords is the 3-vector of the new vertex
         * @param data is the vertex data to be combined, up to four elements.
         * This is useful when mixing colors together or any other
         * user data that was passed in to gluTessVertex.
         * @param weight is an array of weights, one for each element of "data"
         * that should be linearly combined for new values.
         * @param outData is the set of new values of "data" after being
         * put back together based on the weights. it's passed back as a
         * single element Object[] array because that's the closest
         * that Java gets to a pointer.
         */
        public void combine(double[] coords, Object[] data,
                float[] weight, Object[] outData) {
            double[] vertex = new double[coords.length];
            vertex[0] = coords[0];
            vertex[1] = coords[1];
            vertex[2] = coords[2];
            
            outData[0] = vertex;
        }
    }
}