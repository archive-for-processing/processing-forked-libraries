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

import net.nexttext.TextObjectGlyph;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;

/**
 * Renders the text stored in a TextPage as glyphs.
 * 
 * <p>This TextPage renderer uses the parent PApplet's native drawing functions
 * to render glyphs. If the glyphs to be rendered are deformed, their shapes are
 * tesselated using the GLU tesselator from the OpenGL library.</p>
 */
public class OpenGLTextPageRenderer extends G3DTextPageRenderer {

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
        
        glyph.getTessData().get().draw(p);
        
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
