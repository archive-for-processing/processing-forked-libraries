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
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics2D;
import processing.core.PGraphicsJava2D;
import processing.core.PVector;
import net.nexttext.GeometricException;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextPage;
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
public class P3DTextPageRenderer extends G3DTextPageRenderer {

	/**
	 * Constructor.
	 * @param p
	 */
    public P3DTextPageRenderer(PApplet p) {
        super(p, 1.0f);

        //check if the Processing renderer is 2D and keep track of it
        //we need this to make sure we flatten values when needed.
        if ((p.g instanceof PGraphics2D) || (p.g instanceof PGraphicsJava2D)) {
        	renderer_type = RendererType.TWO_D;
        }
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
        if (glyph.getFont().getFont() != null)
        	p.textFont(glyph.getFont(), glyph.getFont().getFont().getSize());
        else
        	p.textFont(glyph.getFont());
        p.textAlign(PConstants.LEFT, PConstants.BASELINE);
        
        // use the cached path if possible
        // use the cached path if possible
        GeneralPath gp = null;       
        if (glyph.isDeformed() || glyph.isStroked())
        	gp = glyph.getOutline();
        
        // optimize rendering based on the presence of DForms and of outlines
        if (glyph.isFilled()) {

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

        try {
        	while (!pi.isDone()) {
	            int seg = pi.currentSegment(coords);
	            switch (seg) {
	                case PathIterator.SEG_MOVETO:
	                    closedPolygon = new ClosedPolygon();
						closedPolygon.addPoint(new PVector(coords[0], -coords[1], 0));
	                    break;
	                case PathIterator.SEG_LINETO:
						closedPolygon.addPoint(new PVector(coords[0], -coords[1], 0));
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
        } catch (GeometricException ge) {
        	System.err.println("Warning: NextText's renderer could not triangulate and fill glyph '" + 
        			glyph.getGlyph() + "'.");
        	return;
        } catch (NoSuchElementException nsee) {
        	System.err.println("Warning: NextText's renderer could not triangulate and fill glyph '" + 
        			glyph.getGlyph() + "'.");
        	return;       
        } catch (RuntimeException re) {
        	System.err.println("Warning: NextText's renderer could not triangulate and fill glyph '" + 
        			glyph.getGlyph() + "'.");
        	return;       
        }
        
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
	        	triList.verts[vcount + v.getIndex()] = v.getPoint().get();
	        	//triList.verts[vcount + v.getIndex()].z += 0f;
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
        PVector vert;
        triList.triangles.rewind();
        while(triList.triangles.remaining() > 0) {
        	vert = triList.verts[triList.triangles.get()];
        	
        	if ((vert.z != 0) && (renderer_type == RendererType.THREE_D)) {
        		p.vertex((float)vert.x, (float)-vert.y, (float)vert.z);
        	}
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
        public PVector verts[] = null;
        IntBuffer triangles = null;
        
        public TriangleList(int numVertex, int numTriangles) {
        	 verts = new PVector[numVertex];
        	 triangles = createIntBuffer(numTriangles * 3);
        }
    }
}

