package funGUI;

import processing.core.*;
/**
 * The frame class provides the basic structure of many of the
 * interface classes, such as the way many objects are drawn to
 * the screen.
 * @author Audrey Seo
 */
public abstract class Frame implements FunGUIObject {
	public enum ColorApp {
		INNER_FILL, OUTER_FILL, INNER_STROKE, OUTER_STROKE, FONT_FILL;

	}

	public int[] c;
	protected PApplet p;
	
	public float x, y;
	public float w, h;
	protected PGraphics g;
	
	
	/**
	 * The constructor.
	 */
	public Frame() {
		// BLANK
	}
	
	protected void display() {
		// Put any shape functions in here
	}

	protected void style() {
		// Put any style-related functions in here
	}

	protected void text() {
		// Put any text calls in here
	}
	
	/**
	 * Draw the frame to the screen.
	 */
	public void draw() {
		// Runs all of the shape-drawing functions
		g.pushStyle();
		style();
		display();
		text();
		g.popStyle();
	}

	protected void col(ColorApp app) {
		switch (app) {
		case INNER_FILL:
			if (col(0))
				g.fill(c[0]);
			break;
		case INNER_STROKE:
			if (col(1))
				g.stroke(c[1]);
			break;
		case OUTER_FILL:
			if (col(2))
				g.fill(c[2]);
			break;
		case OUTER_STROKE:
			if (col(3))
				g.stroke(c[3]);
			break;
		case FONT_FILL:
			if (col(4))
				g.fill(c[4]);
			break;
		default:
			break;
		}
	}
	
	public boolean col(int n) {
		return (c.length > (n + 1) && c[n] >= 0);
	}
	
	public float x() {
		return(x);
	}
	
	public float y() {
		return(y);
	}

	public float w() {
		return (w);
	}

	public float h() {
		return (h);
	}


	float limitFont(PFont font, float fontSize, String str, float w) {
		float hrat = (float) (p.height / 400.0);
		float wrat = (float) (p.width / 600.0);
		g.textFont(font, fontSize * hrat);
		float twidth = g.textWidth(str);
		if (twidth < w - 30 * wrat) {
			return(fontSize * hrat);
		}
		if (fontSize * hrat * ((w - (30 * wrat)) / twidth) > 0) return(fontSize * hrat * ((w - (30 * wrat)) / twidth));
		return(PApplet.abs(fontSize * hrat * ((w * 4) / (5 * twidth))));
	}
}
