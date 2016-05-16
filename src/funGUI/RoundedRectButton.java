package funGUI;

import processing.core.*;

public class RoundedRectButton extends RectButton implements PConstants {
	float curvature = 0.0f;
	float curveRadius = 0.0f;
	float innerCurveRadius = 0.0f;
	private boolean printing = false;
	public boolean border = true;
	
	/**
	 * Most basic constructor for the RoundedRectButton class.
	 * @param p		PApplet, the parent sketch, usually "this"
	 * @param nx	float, the horizontal location of the button's center
	 * @param ny	float, the vertical location of the button's center
	 * @param nw	float, the width of the button
	 * @param nh	float, the height of the button
	 * @param text	String, the label of the button
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, String text) {
		super(p, nx, ny, nw, nh, text);
		this.curvature = PApplet.constrain(curvature, 0.1f, 1f);
		calculateRadius();
		this.fsize = (int) fsize;
		this.font = p.createFont(REG_SANSS_TXT, nh - 3);
	}
	
	/**
	 * Constructor for the RoundedRectButton class.
	 * @param p			PApplet, the parent sketch of the object (usually "this")
	 * @param nx		float, the horizontal location of the center of this button
	 * @param ny		float, the vertical location of the center of this button
	 * @param nw		float, the horizontal length of this button
	 * @param nh		float, the vertical length of this button
	 * @param ncolors	color, the colors used in this button
	 * @param ntext		String, the string value of the name of this button
	 * @param curvature	float, the percent by which to curve the button's corners in
	 * @param fsize		float, the font size 
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext, float curvature, float fsize) {
		super(p, nx, ny, nw, nh, ncolors, ntext);
		this.curvature = PApplet.constrain(curvature, 0.1f, 1f);
		calculateRadius();
		this.fsize = (int) fsize;
	}
	
	/**
	 * Constructor for the RoundedRectButton class.
	 * @param p			PApplet, the parent sketch of the object (usually "this")
	 * @param nx		float, the horizontal location of the center of this button
	 * @param ny		float, the vertical location of the center of this button
	 * @param nw		float, the horizontal length of this button
	 * @param nh		float, the vertical length of this button
	 * @param ntext		String, the string value of the name of this button
	 * @param curvature	float, the percent by which to curve the button's corners in
	 * @param fsize		float, the font size
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, String ntext, float curvature, float fsize) {
		super(p, nx, ny, nw, nh, ntext);
		this.curvature = PApplet.constrain(curvature, 0.1f, 1f);
		calculateRadius();
		this.fsize = (int) fsize;
	}
	
	/**
	 * Constructor for the RoundedRectButton class.
	 * @param p			PApplet, the parent sketch of the object (usually "this")
	 * @param nx		float, the horizontal location of the center of this button
	 * @param ny		float, the vertical location of the center of this button
	 * @param nw		float, the horizontal length of this button
	 * @param nh		float, the vertical length of this button
	 * @param ntext		String, the string value of the name of this button
	 * @param curvature	float, the percent by which to curve the button's corners in
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, String ntext, float curvature) {
		super(p, nx, ny, nw, nh, ntext);
		this.curvature = PApplet.constrain(curvature, 0.1f, 1f);
		calculateRadius();
	}
	
	
	/**
	 * Constructor for the RoundedRectButton class.
	 * @param p			PApplet, the parent sketch of the object (usually "this")
	 * @param nx		float, the horizontal location of the center of this button
	 * @param ny		float, the vertical location of the center of this button
	 * @param nw		float, the horizontal length of this button
	 * @param nh		float, the vertical length of this button
	 * @param ncolors	color, the colors used in this button
	 * @param ntext		String, the string value of the name of this button
	 * @param curvature	float, the percent by which to curve the button's corners in
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext, float curvature) {
		super(p, nx, ny, nw, nh, ncolors, ntext);
		this.curvature = PApplet.constrain(curvature, 0.1f, 1f);
		calculateRadius();
	}
	
	/**
	 * Constructor for the RoundedRectButton class. Uses a default amount of curvature.
	 * @param p			PApplet, the parent sketch of the object (usually "this")
	 * @param nx		float, the horizontal location of the center of this button
	 * @param ny		float, the vertical location of the center of this button
	 * @param nw		float, the horizontal length of this button
	 * @param nh		float, the vertical length of this button
	 * @param ncolors	color, the colors used in this button
	 * @param ntext		String, the string value of the name of this button
	 */
	public RoundedRectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext) {
		super(p, nx, ny, nw, nh, ncolors, ntext);
		curvature = 0.25f;
		calculateRadius();
	}
	
	public void borders(boolean isBordered) {
		border = isBordered;
		if (!border) fsize = (int) (h * .9);
	}
	
	@Override
	public void draw() {
		/*Function to display the button
	     takes no arguments* bc all of the properties of the button are initialized/created by the initializer, new Button()
		 *other than mX and mY to allow the function rolled_over */
		rolled_over(p.mouseX, p.mouseY);

//		back();
		display("", font, fsize);
		
		
		if (rollover) {
			g.pushStyle();
			g.fill(scheme[2]);
			g.noStroke();
			g.rectMode(CENTER);
			backing(x(), y(), w(), h(), r());
			g.popStyle();
		}
		
		drawButtonName();
	}
	
	@Override
	protected void back() {
		g.pushStyle();
		g.rectMode(CENTER);
		g.noStroke();
		if (border) {
			g.fill(outside);
			//		if (printing) PApplet.println("BackShape - Before");
			//		backShape(x(), y(), w(), h());
			backing(x(),y(), w(), h(), r());
			//		if (printing) PApplet.println("BackShape- After");

			//		float insideW, insideH;
			//		insideW = w - 20 * wratio;
			//		insideH = h - 20 * hratio;

			g.fill(inside);
			//		innerBackShape(x(), y(), iw(), ih());\
			backing(x(), y(), iw(), ih(), ir());

		} else {
			g.stroke(outside);
			g.fill(inside);
			backing(x(), y(), w(), h(), r());
		}
		g.popStyle();

	}
	
	void backing(float xi, float yi, float wi, float hi, float ri) {
		g.rect(xi, yi, wi, hi, ri);
	}
	
	void backOutline(float xi, float yi, float wi, float hi, float r) {
		float wi2 = (float) (wi * .5);
		float hi2 = (float) (hi * .5);
		float partialW = (float) (wi2 - r);
		float partialH = (float) (hi2 - r);
		//PApplet.println("PartialW: " + partialW);
		float x1a = xi - wi2;
		float x1b = xi - partialW;
		
		float x2a = xi + wi2;
		float x2b = xi + partialW;
		
		float y1a = yi - hi2;
		float y1b = yi - partialH;
		
		float y2a = yi + hi2;
		float y2b = yi + partialH;
		
		g.beginShape();
		// First corner
		g.vertex(x1b, y1a);
		g.vertex(x1a, y1b);
		
		// Second corner
		g.vertex(x1a, y2b);
		g.vertex(x1b, y2a);
		// Third corner
		g.vertex(x2b, y2a);
		g.vertex(x2a, y2b);
		
		// Fourth corner
		g.vertex(x2a, y1b);
		g.vertex(x2b, y1a);
		g.endShape(CLOSE);
	}
	
	void backCorners(float xi, float yi, float wi, float hi, float r) {
		float wi2 = (float) (wi * .5);
		float hi2 = (float) (hi * .5);

		float x1a = xi - wi2;		
		float x2a = xi + wi2;		
		float y1a = yi - hi2;		
		float y2a = yi + hi2;
		
		float diameter = r * 2;
		float radius = r;
		float xa = x1a + radius;
		float xb = x2a - radius;
		float ya = y1a + radius;
		float yb = y2a - radius;
		
		float dth = 0;
		g.arc(xa, ya, diameter, diameter, PI - dth, 3 * HALF_PI + dth, CHORD);
		
		// Second corner - arc
		g.arc(xa, yb, diameter, diameter, HALF_PI - dth, PI + dth, CHORD);
		
		// Third corner - arc
		g.arc(xb, yb, diameter, diameter, -dth, HALF_PI + dth, CHORD);
		
		// Fourth corner - arc
		g.arc(xb, ya, diameter, diameter, -HALF_PI - dth, dth, CHORD);
	}

	public void backShape(float xi, float yi, float wi, float hi, float r) {
		backOutline(xi, yi, wi, hi, r);
		backCorners(xi, yi, wi, hi, r);
		
//		float wi2 = (float) (wi * .5);
//		float hi2 = (float) (hi * .5);
//		float partialW = (float) (wi2 - r());
//		float partialH = (float) (hi2 - r());
//		//PApplet.println("PartialW: " + partialW);
//		float x1a = xi - wi2;
//		float x1b = xi - partialW;
//		
//		float x2a = xi + wi2;
//		float x2b = xi + partialW;
//		
//		float y1a = yi - hi2;
//		float y1b = yi - partialH;
//		
//		float y2a = yi + hi2;
//		float y2b = yi + partialH;
//		
//		g.beginShape();
//		// First corner
//		g.vertex(x1b, y1a);
//		g.vertex(x1a, y1b);
//		
//		// Second corner
//		g.vertex(x1a, y2b);
//		g.vertex(x1b, y2a);
//		// Third corner
//		g.vertex(x2b, y2a);
//		g.vertex(x2a, y2b);
//		
//		// Fourth corner
//		g.vertex(x2a, y1b);
//		g.vertex(x2b, y1a);
//		g.endShape(CLOSE);
//		
////		float diameter = (float) (PApplet.dist(0, 0, r(), r()));
//		float diameter = r() * 2;
//		float radius = diameter * .5f;
//		float xa = x1a + radius;
//		float xb = x2a - radius;
//		float ya = y1a + radius;
//		float yb = y2a - radius;
//		
//		
//		//float theta = 0;
////		float dth = HALF_PI;
//		float dth = 0;
//		// First corner - arc
////		if (printing) PApplet.println("dth???:     " + dth);
//		g.arc(xa, ya, diameter, diameter, PI - dth, 3 * HALF_PI + dth, CHORD);
//		
//		// Second corner - arc
//		g.arc(xa, yb, diameter, diameter, HALF_PI - dth, PI + dth, CHORD);
//		
//		// Third corner - arc
//		g.arc(xb, yb, diameter, diameter, -dth, HALF_PI + dth, CHORD);
//		
//		// Fourth corner - arc
//		g.arc(xb, ya, diameter, diameter, -HALF_PI - dth, dth, CHORD);
	}
	
	@Override
	public void innerBackShape(float xi, float yi, float wi, float hi) {
		float wi2 = (float) (wi * .5);
		float hi2 = (float) (hi * .5);
		float partialW = (float) (wi2 - ir());
		float partialH = (float) (hi2 - ir());
//		if (printing) PApplet.println("partialW: " + partialW);
		float x1a = xi - wi2;
		float x1b = xi - partialW;
		
		float x2a = xi + wi2;
		float x2b = xi + partialW;
		
		float y1a = yi - hi2;
		float y1b = yi - partialH;
		
		float y2a = yi + hi2;
		float y2b = yi + partialH;
		
		g.beginShape();
		// First corner
		g.vertex(x1b, y1a);
		g.vertex(x1a, y1b);
		
		// Second corner
		g.vertex(x1a, y2b);
		g.vertex(x1b, y2a);
		// Third corner
		g.vertex(x2b, y2a);
		g.vertex(x2a, y2b);
		
		// Fourth corner
		g.vertex(x2a, y1b);
		g.vertex(x2b, y1a);
		g.endShape(CLOSE);
		
//		float diameter = (float) (PApplet.dist(0, 0, ir(), ir()));
		float diameter = ir() * 2;
		
		float radius = diameter * .5f;
//		if (printing) PApplet.println("Radius: " + r() + " Inner Radius: " + ir());
//		if (printing) PApplet.println("Real radius: " + radius);
		float xa = x1a + radius;
		float xb = x2a - radius;
		float ya = y1a + radius;
		float yb = y2a - radius;
		
		
		//float theta = 0;
		float dth = HALF_PI;
//		if (printing) PApplet.println("dth:     " + dth + "\n");
//		 First corner - arc
		g.arc(xa, ya, diameter, diameter, PI - dth, 3 * HALF_PI + dth, CHORD);
		
		// Second corner - arc
		g.arc(xa, yb, diameter, diameter, HALF_PI - dth, PI + dth, CHORD);
		
		// Third corner - arc
		g.arc(xb, yb, diameter, diameter, -dth, HALF_PI + dth, CHORD);
		
		// Fourth corner - arc
		g.arc(xb, ya, diameter, diameter, -HALF_PI - dth, dth, CHORD);
	}
	
	public void changeCurvature(float f) {
		this.curvature = f;
		calculateRadius();
	}
	
	private void calculateRadius() {
		if (printing) PApplet.println("");
		float rh = rH();
		float rw = rW();
		float rih = PApplet.constrain(PApplet.map(curvature * ih() * .5f, 0f, ih(), 0f, ih() * .5f), 0, ih() * .5f);
		float riw = PApplet.constrain(PApplet.map(curvature * iw() * .5f, 0f, iw(), 0f, iw() * .5f), 0, iw() * .5f);
		if (rh < rw) {
			if (rw < h * .4) {
				curveRadius = rw;
			} else {
				curveRadius = (float) (h * .4);
			}
		} else {
			if (rh < w * .4) {
				curveRadius = rh;
			} else {
				curveRadius = (float) (w * .4);
			}
		}
		
//		float widthRatio = (w / iw());
//		float heightRatio = (h / ih());
		float hW = rih / riw;
		float wH = riw / rih;
//		if (printing) PApplet.println(heightRatio + " " + widthRatio);
	    
		boolean heightUsed = false;
		if (rih < riw) {
			heightUsed = true;
			if (riw < ih() * .4) {
				innerCurveRadius = riw;
			} else if (rih * wH < ih() * .4) {
//				innerCurveRadius = (float) (h() * .4 * heightRatio);
//				innerCurveRadius = rih * wH;
				innerCurveRadius = rih * wH;
			} else {
				innerCurveRadius = ih() * .4f;
			}
		} else {
			if (rih < iw() * .4) {
				innerCurveRadius = rih;
			} else {
//				innerCurveRadius = (float) (w() * .4 * widthRatio);
//				innerCurveRadius = riw * hW;
				innerCurveRadius = iw() * .4f;
			}
		}
		if (printing) PApplet.println("Inner Curve Radius: " + innerCurveRadius + "  RIW: " + riw + "  RIH: " + (rih) +  "  WH: " + wH + "  HW: " + hW + "  HeightRatioUsed: " + heightUsed + " Curvature: " + curvature);
	}
	
	private float r() {
		return(curveRadius);
	}
	
	private float ir() {
		return(innerCurveRadius);
	}
	
	/**
	 * Calculates the amount of the height that is cut out by curvature, aka the radius of the height curvature
	 * @return
	 */
	private float rH() {
		return((float) PApplet.constrain(PApplet.map(curvature * h * .5f, 0f, h, 0f, h * .5f), 0, h * .5f));
	}
	
	private float rW() {
		return((float) PApplet.constrain(PApplet.map(curvature * w * .5f, 0f, w, 0f, w * .5f), 0, w * .5f));
	}
	
//	private float complement() {
//		return(1 - curvature);
//	}
	
	public float avg(float one, float two) {
		return((float) ((one + two) / 2));
	}
}
