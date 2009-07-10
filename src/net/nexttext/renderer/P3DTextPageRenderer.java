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
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics2D;
import processing.core.PGraphicsJava2D;
import net.nexttext.FastMath;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextPage;
import net.nexttext.Vector3;
import net.nexttext.Vector3ArithmeticException;
import net.nexttext.renderer.util.ClosedPolygon;
import net.nexttext.renderer.util.Glyph3D;
import net.nexttext.renderer.util.TriangulationVertex;


/**
 * 
 * Renders the text stored in a text page.
 * 
 * <p>
 * This TextPage renderer is based on the Java2D API.
 * </p>
 * 
 */
public class P3DTextPageRenderer extends TextPageRenderer {

	protected double bezierDetail;
	
    /**
     * Renderer type enumeration.
     */
	public enum RendererType
	{
		TWO_D,
		THREE_D
	}
	RendererType renderer_type = RendererType.THREE_D;
	
    public P3DTextPageRenderer(PApplet p) {
        super(p);
        bezierDetail = 1.0;
        
        if ((p.g instanceof PGraphics2D) || (p.g instanceof PGraphicsJava2D)) {
        	renderer_type = RendererType.TWO_D;
        }
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
        	enterCoords(textPage);
            traverse(textPage.getTextRoot());
            exitCoords();
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
     * TextPage.
     * 
     * @param page the TextPage
     */
    protected void enterCoords(TextPage page) {
        p.pushMatrix();

        // properties
        Vector3 pos = page.getPosition().get();
        Vector3 rot = page.getRotation().get();
        
        if (renderer_type == RendererType.THREE_D) {
			p.translate((float)pos.x, (float)pos.y, (float)pos.z);
        	p.translate(p.width/2.0f, p.height/2.0f, 0);
        	p.rotateX((float)rot.x);
        	p.rotateY((float)rot.y);
        	p.rotateZ((float)rot.z);
        	p.translate(-p.width/2.0f, -p.height/2.0f, 0);
		}
		else {
			p.translate((float)pos.x, (float)pos.y);
        	p.translate(p.width/2.0f, p.height/2.0f);
			p.rotate((float)rot.z);
        	p.translate(-p.width/2.0f, -p.height/2.0f);
		}
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
        
        if (renderer_type == RendererType.THREE_D)
        	p.translate((float)pos.x, (float)pos.y, 0); //todo: use Z coord
        else
        	p.translate((float)pos.x, (float)pos.y);
        // rotation
        float rotation = (float)node.getRotation().get();	//todo: rotate in 3D
        p.rotate(rotation);
    }

    /**
     * Transform the drawing surface out of the coordinates on top of the stack.
     * 
     * <p>This undoes the change of enterCoords(...).</p>
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
        p.textFont(glyph.getFont(), glyph.getFont().getFont().getSize());
        p.textAlign(PConstants.LEFT, PConstants.BASELINE);
        
        // use the cached path if possible
        //GeneralPath gp = null;       
        //if (glyph.isDeformed() || glyph.isStroked())
        GeneralPath	gp = glyph.getOutline();

        // optimize rendering based on the presence of DForms and of outlines
        if (glyph.isFilled()) {
            // fill the shape
            p.noStroke();
            p.fill(glyph.getColorAbsolute().getRGB());
            fillPath(glyph, gp);

            /*
             * Don't use Processing's native with P3D because it leaves
             * hairlines when anti-aliasing shapes. 
            if (glyph.isDeformed()) {
                // fill the shape
                p.noStroke();
                p.fill(glyph.getColorAbsolute().getRGB());
                fillPath(glyph, gp);
                
            } else {
                // render glyph using Processing's native PFont drawing method
                p.fill(glyph.getColorAbsolute().getRGB());
                p.text(glyph.getGlyph(), 0, 0);
            }
            */
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
     * Create a new IntBuffer of the specified size.
     *
     * @param size
     *            required number of ints to store.
     * @return the new IntBuffer
     */
    public static IntBuffer createIntBuffer(int size) {
        IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
        buf.clear();
        return buf;
    }

    /**
     * Fills the glyph using native Processing drawing functions.
     * 
     * @param glyph the glyph
     * @param gp the outline of the glyph
     */
    protected void fillPath(TextObjectGlyph glyph, GeneralPath gp) {
        // save the current smooth property
        boolean smooth = p.g.smooth;
        // turn off smoothing so that we don't get gaps in between the triangles
        p.noSmooth();
        
        //Convert the path to triangles
        PathIterator pi = new FlatteningPathIterator(gp
        		.getPathIterator(new AffineTransform()), this.bezierDetail);
        ClosedPolygon closedPolygon = null;
        Glyph3D fontGlyph = new Glyph3D();
        float[] coords = new float[6];

        while (!pi.isDone()) {
            int seg = pi.currentSegment(coords);
            switch (seg) {
                case PathIterator.SEG_MOVETO:
                    closedPolygon = new ClosedPolygon();
					closedPolygon.addPoint(new Vector3(coords[0], -coords[1], 0));
                    break;
                case PathIterator.SEG_LINETO:
					closedPolygon.addPoint(new Vector3(coords[0], -coords[1], 0));

                    break;
                case PathIterator.SEG_CLOSE:
                    closedPolygon.close();
                    fontGlyph.addPolygon(closedPolygon);
                    closedPolygon = null;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "unknown segment type " + seg);
            }
            pi.next();
        }

        if (fontGlyph.isEmpty())
        	return;

        // Time to triangulate the surface of the glyph
        fontGlyph.triangulate();
        
        // Calculate how many vertices we need
        int vertex_count = fontGlyph.getVertices().size();
        int triangle_count = fontGlyph.getSurface().capacity() / 3;
        
        // Get the triangle list from the cache or create it if it's the first time
        TriangleList triList = (TriangleList)glyph.rendererCache;
        if (triList == null) {
	        triList = new TriangleList(vertex_count, triangle_count);
	        
	        // Add all the vertices (either one or two layers)
	        int vcount = 0; // Used to pad indexes.
	        for (TriangulationVertex v : fontGlyph.getVertices()) {
	        	triList.verts[vcount + v.getIndex()] = new Vector3(v.getPoint());
	        	triList.verts[vcount + v.getIndex()].z += 0.5f;
	        }
	        fontGlyph.getSurface().rewind();
	        while (fontGlyph.getSurface().remaining() > 0) {
	            int tri[] = { fontGlyph.getSurface().get() + vcount,
	            		fontGlyph.getSurface().get() + vcount,
	            		fontGlyph.getSurface().get() + vcount };
	            triList.triangles.put(tri[2]);
	            triList.triangles.put(tri[1]);
	            triList.triangles.put(tri[0]);
	        }
	        vcount += vertex_count;
	        glyph.rendererCache = triList;
        }
        
        //draw the triangles
        p.beginShape(PApplet.TRIANGLES);
        Vector3 vert;
        triList.triangles.rewind();
        while(triList.triangles.remaining() > 0) {
        	vert = triList.verts[triList.triangles.get()];
        	
        	if (renderer_type == RendererType.THREE_D)
        		p.vertex((float)vert.x, (float)-vert.y, (float)vert.z);
        	else
        		p.vertex((float)vert.x, (float)-vert.y);
        }
        p.endShape();
        
        // restore saved smooth property
        if (smooth) p.smooth();
    }
    
    /**
     * Strokes the glyph using native Processing drawing functions.
     * 
     * @param gp the outline of the glyph
     */
    protected void strokePath(GeneralPath gp) {
        PathIterator pi = new FlatteningPathIterator(gp
        		.getPathIterator(new AffineTransform()), this.bezierDetail);
        float[] coords = new float[6];
        
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    p.beginShape();
                    p.vertex(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    p.vertex(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_CLOSE:
                    p.endShape(PConstants.CLOSE);
                    
                    break;
            }
            
            pi.next();
        }
        
        p.endShape(PConstants.CLOSE);
    }    
    
    protected class TriangleList {
        public Vector3 verts[] = null;
        IntBuffer triangles = null;
        
        public TriangleList(int numVertex, int numTriangles) {
        	 verts = new Vector3[numVertex];
        	 triangles = createIntBuffer(numTriangles * 3);
        }
    }
}

