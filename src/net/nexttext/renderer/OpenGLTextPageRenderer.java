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
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;

import net.nexttext.TextObjectGlyph;
import net.nexttext.property.TessDataProperty;
import net.nexttext.renderer.util.TessCallback;
import net.nexttext.renderer.util.TessData;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.core.PVector;

/**
 * Renders the text stored in a TextPage as glyphs.
 * 
 * <p>This TextPage renderer uses the parent PApplet's native drawing functions
 * to render glyphs. If the glyphs to be rendered are deformed, their shapes are
 * tesselated using the GLU tesselator from the OpenGL library.</p>
 */
public class OpenGLTextPageRenderer extends G3DTextPageRenderer {
    protected GLU glu;
    protected GLUtessellator tess;
    protected TessCallback tessCallback;

    /**
     * Builds a TextPageRenderer.
     * 
     * @param p the parent PApplet
     */
    public OpenGLTextPageRenderer(PApplet p) throws NoClassDefFoundError {
        this(p, p.g);
    }

    /**
     * Builds a TextPageRenderer.
     * 
     * @param p the parent PApplet
     */
    public OpenGLTextPageRenderer(PApplet p, PGraphics g) throws NoClassDefFoundError {
        super(p, g, 3.0f);
        
        glu = new GLU();
        tess = glu.gluNewTess();
		tessCallback = new TessCallback(glu);
		glu.gluTessCallback(tess, GLU.GLU_TESS_BEGIN, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_END, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_VERTEX, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_COMBINE, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_ERROR, tessCallback);
    }
    
    /** Convert color (int) to Color. */
	public Color intToColor(int c) { return new Color((int)g.red(c), (int)g.green(c), (int)g.blue(c), (int)g.alpha(c)); }

	/** Convert Color to color (int). */
	public int colorToInt(Color c) { return g.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()); }
    
    /** Tesselates a glyph. */
  	public TessData tesselateGlyph(TextObjectGlyph glyph) {
  		// six element array received from the Java2D path iterator
  		float textPoints[] = new float[6];

  		// get absolute outline
  		// get the glyph's position
  		PVector pos = glyph.getPositionAbsolute();
  		Rectangle bounds = glyph.getBounds();
  		float rot = glyph.getParent().getRotation().get();  //a bit of a hack
  		GeneralPath outline = new GeneralPath(glyph.getOutline());
  		//outline.transform(AffineTransform.getTranslateInstance(pos.x, pos.y));
  		//outline.transform(AffineTransform.getRotateInstance(rot, bounds.getX(), bounds.getY()));
  		PathIterator iter = outline.getPathIterator(null);

  		glu.gluTessBeginPolygon(tess, null);
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
  				glu.gluTessBeginContour(tess);

  				vertex = new double[] { textPoints[0], textPoints[1], 0 };

  				glu.gluTessVertex(tess, vertex, 0, vertex);

  				lastX = textPoints[0];
  				lastY = textPoints[1];

  				break;

  			case PathIterator.SEG_QUADTO:   // 2 points

  				for (int i = 1; i <= bezierDetail; i++) {
  					float t = (float)(i/bezierDetail);
  					vertex = new double[] {
  							g.bezierPoint(
  									lastX, 
  									lastX + ((textPoints[0]-lastX)*2/3), 
  									textPoints[2] + ((textPoints[0]-textPoints[2])*2/3), 
  									textPoints[2], 
  									t
  									),
  									g.bezierPoint(
  											lastY, 
  											lastY + ((textPoints[1]-lastY)*2/3),
  											textPoints[3] + ((textPoints[1]-textPoints[3])*2/3), 
  											textPoints[3], 
  											t
  											), 
  											0
  					};

  					glu.gluTessVertex(tess, vertex, 0, vertex);
  				}

  				lastX = textPoints[2];
  				lastY = textPoints[3];

  				break;

  			case PathIterator.SEG_CLOSE:
  				glu.gluTessEndContour(tess);

  				break;
  			}

  			iter.next();
  		}

  		glu.gluTessEndPolygon(tess);  

  		return tessCallback.getData();
  	}
    
    /**
     * Renders a TextObjectGlyph.
     * 
     * @param glyph The TextObjectGlyph to render
     */
    protected void renderGlyph(TextObjectGlyph glyph) {    	
        // save the current properties
        g.pushStyle();

        // check if the glyph's font has a vector font
        boolean hasVectorFont = glyph.getFont().getFont() != null;
        
        // use the cached path if possible
        GeneralPath gp = null;       
        if (glyph.isDeformed() || glyph.isStroked())
        	gp = glyph.getOutline();

        // optimize rendering based on the presence of DForms and of outlines
        if (glyph.isFilled()) {
            if (glyph.isDeformed()) {
        	    // fill the shape
                g.noStroke();
                g.fill(glyph.getColorAbsolute().getRGB());
                fillPath(glyph);
                
            } else {
                // set text properties
                if (hasVectorFont)
                	g.textFont(glyph.getFont(), glyph.getFont().getFont().getSize());
                else
                	g.textFont(glyph.getFont());
                g.textAlign(PConstants.LEFT, PConstants.BASELINE);
                
                // render glyph using Processing's native PFont drawing method
                g.fill(glyph.getColorAbsolute().getRGB());
                g.text(glyph.getGlyph(), 0, 0);
            }
        }

        if (glyph.isStroked()) {
            // draw the outline of the shape
            g.stroke(glyph.getStrokeColorAbsolute().getRGB());
            BasicStroke bs = glyph.getStrokeAbsolute();
            g.strokeWeight(bs.getLineWidth());
            if (g instanceof PGraphicsJava2D) {
                switch (bs.getEndCap()) {
                    case BasicStroke.CAP_ROUND:
                        g.strokeCap(PApplet.ROUND);
                        break;
                    case BasicStroke.CAP_SQUARE:
                        g.strokeCap(PApplet.PROJECT);
                        break;
                    default:
                        g.strokeCap(PApplet.SQUARE);
                    break;
                }
                switch (bs.getLineJoin()) {
                    case BasicStroke.JOIN_ROUND:
                        g.strokeJoin(PApplet.ROUND);
                        break;
                    case BasicStroke.JOIN_BEVEL:
                        g.strokeJoin(PApplet.BEVEL);
                        break;
                    default:
                        g.strokeJoin(PApplet.MITER);
                    break;
                }
            }
            g.noFill();
            strokePath(gp);
        }

        // restore saved properties
        g.popStyle();

    } // end renderGlyph    
    
    /**
     * Fills the glyph using the tesselator.
     * 
     * @param glyph the glyph
     * @param gp the outline of the glyph
     */
    protected void fillPath(TextObjectGlyph glyph) {
        // save the current smooth property
        boolean smooth = g.smooth;
        // turn off smoothing so that we don't get gaps in between the triangles
        g.noSmooth();
        
        TessData tessData;
        try {
        	tessData = ((TessDataProperty)glyph.getProperty("Tess Data")).get();
        } catch (NullPointerException npe) {
        	// tesselate that shit!
        	tessData = tesselateGlyph(glyph);
        	glyph.init("Tess Data", new TessDataProperty(tessData));
        }
        
        tessData.draw(p);
        
        // restore saved smooth property
        if (smooth) g.smooth();
    }

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
                    g.beginShape();
                    g.vertex(textPoints[0], textPoints[1]);
                    lastX = textPoints[0];
                    lastY = textPoints[1];
                    break;
                	
                case PathIterator.SEG_QUADTO:

                	g.bezierVertex(lastX + ((textPoints[0]-lastX)*2/3),
                				   lastY + ((textPoints[1]-lastY)*2/3),
                				   textPoints[2] + ((textPoints[0]-textPoints[2])*2/3),
                				   textPoints[3] + ((textPoints[1]-textPoints[3])*2/3),
                				   textPoints[2], textPoints[3]);
                	
                    lastX = textPoints[2];
                    lastY = textPoints[3];
                    break;

                case PathIterator.SEG_CLOSE:
                    g.endShape(PConstants.CLOSE);
                    
                    break;
            }
            
            iter.next();
        }
        
        g.endShape(PConstants.CLOSE);
    }
}
