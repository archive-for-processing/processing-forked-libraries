package net.nexttext.tesselator;

import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;

import net.nexttext.TextObjectGlyph;
import processing.core.PGraphics;
import processing.core.PVector;

public class Tesselator {

	protected GLU glu;
    protected GLUtessellator tess;
    protected TessCallback tessCallback;
    protected PGraphics g;
    protected float tessDetail;
    
    public Tesselator(GLU glu, PGraphics g, float tessDetail) {
    	this.glu = glu;
    	this.g = g;
    	this.tessDetail = tessDetail;
    	
    	tess = glu.gluNewTess();
		tessCallback = new TessCallback(glu);
		glu.gluTessCallback(tess, GLU.GLU_TESS_BEGIN, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_END, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_VERTEX, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_COMBINE, tessCallback);
		glu.gluTessCallback(tess, GLU.GLU_TESS_ERROR, tessCallback);
    }
    
    /** Tesselates a glyph. */
  	public TessData tesselateGlyph(TextObjectGlyph glyph) {
  		// six element array received from the Java2D path iterator
  		float textPoints[] = new float[6];

  		// get absolute outline
  		// get the glyph's position
  		//PVector pos = glyph.getPositionAbsolute();
  		//Rectangle bounds = glyph.getBounds();
  		//float rot = glyph.getParent().getRotation().get();  //a bit of a hack
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

  				for (int i = 1; i <= tessDetail; i++) {
  					float t = (float)(i/tessDetail);
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
}
