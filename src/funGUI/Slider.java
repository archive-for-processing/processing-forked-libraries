package funGUI;

import processing.core.*;
/**
 * A slider that calculates a float value depending on the 
 * placement of the slider.
 * @author Audrey Seo
 */
public class Slider extends Frame implements PConstants {
//	public float x, y, w, h;

	float percent;
	protected boolean following = false;
	protected boolean pressed = false;

	protected float min;
	protected float max;
	String label;
	PFont f;
	Direction textPosition = Direction.UP;
//	PGraphics g;
//	PApplet p;
	
	/**
	 * The constructor of the Slider object. This slider defaults to a minimum of 0 and a maximum of 1.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param x			float, the x location of the slider's center
	 * @param y			float, the y location of the slider's center
	 */
	public Slider(PApplet p, float x, float y) {
		init(p, x, y, 0, 1);
		this.w = 150;
	}
	
	/**
	 * The constructor of the Slider object. This slider defaults to a minimum of 0 and a maximum of 1.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param x			float, the x location of the slider's center
	 * @param y			float, the y location of the slider's center
	 * @param name		String, the label of the slider to display
	 */
	public Slider(PApplet p, float x, float y, String name) {
		init(p, x, y, 0, 1);
		this.w = 150;
		this.label = name;
	}
	
	/**
	 * The constructor of the Slider object. This slider defaults to a minimum of 0 and a maximum of 1.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param x			float, the x location of the slider's center
	 * @param y			float, the y location of the slider's center
	 * @param w			float, the width of the slider
	 */
	public Slider(PApplet p, float x, float y, float w) {
		init(p, x, y, 0, 1);
		this.w = w;
	}
	
	/**
	 * The constructor of the Slider object. This slider defaults to a minimum of 0 and a maximum of 1.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param x			float, the x location of the slider's center
	 * @param y			float, the y location of the slider's center
	 * @param w			float, the width of the slider
	 * @param label		String, the label of the slider
	 */
	public Slider(PApplet p, float x, float y, float w, String label) {
		this.label = label;
		init(p, x, y, 0, 1);
		this.w = w;
	}
	
	/**
	 * The constructor of the Slider object.
	 * @param p		PApplet, the parent of the sketch, usually "this."
	 * @param x		float, the x location of the slider's center.
	 * @param y		float, the y location of the slider's center.
	 * @param w		float, the width of the slider
	 * @param min	float, the minimum value of the slider.
	 * @param max	float, the maximum value of the slider.
	 */
	public Slider(PApplet p, float x, float y, float w, float min, float max) {
		init(p, x, y, min, max);
		this.w = w;
	}
	
	/**
	 * The constructor of the Slider object.
	 * @param p		PApplet, the parent of the sketch, usually "this."
	 * @param x		float, the x location of the slider's center.
	 * @param y		float, the y location of the slider's center.
	 * @param min	float, the minimum value of the slider.
	 * @param max	float, the maximum value of the slider.
	 */
	public Slider(PApplet p, float x, float y, float min, float max) {
		init(p, x, y, min, max);
	}
	
	/**
	 * The constructor of the Slider objects.
	 * @param p		PApplet, the parent of the sketch, usually "this."
	 * @param x		float, the x location of the slider's center.
	 * @param y		float, the y location of the slider's center.
	 * @param min	float, the minimum value of the slider.
	 * @param max	float, the maximum value of the slider.
	 * @param label String, the name of the slider
	 */
	public Slider(PApplet p, float x, float y, float min, float max, String label) {
		this.label = label;
		init(p, x, y, min, max);
	}
	
	/**
	 * The constructor of the Slider objects.
	 * @param p			PApplet, the parent of the sketch, usually "this."
	 * @param x			float, the x location of the slider's center.
	 * @param y			float, the y location of the slider's center.
	 * @param min		float, the minimum value of the slider.
	 * @param max		float, the maximum value of the slider.
	 * @param label 	String, the name of the slider
	 * @param initial	float, the initial value of the slider.
	 */
	public Slider(PApplet p, float x, float y, float min, float max, String label, float initial) {
		this.label = label;
		init(p, x, y, min, max);
		this.percent = PApplet.constrain(PApplet.map(initial, min, max, 0, 1), 0, 1);
	}
	
	/**
	 * Changes the position of the text to a new position for the text:
	 * RIGHT, LEFT, UP, DOWN. All of these are relative to the slider.
	 * @param newPosition		Direction, the new relative position of the slider.
	 */
	public void alignText(Direction newPosition) {
		this.textPosition = newPosition;
	}
	
	public void assignLowHigh(float low, float high) {
		this.min = low;
		this.max = high;
	}
	
	/**
	 * Displays the slider on the screen, fully operational.
	 */
	public void draw() {
		txt();
		bg();
		slider();
		slide();
	}

	void txt() {
		if (label != null) {
			g.pushStyle();
			g.textFont(f);
			
			g.textAlign(CENTER, CENTER);
			
			g.fill(0);
			g.text(label, x + xOffset(), y + yOffset());
			g.popStyle();
		}
	}
	
	void customAlignText() {
		int hAlign = CENTER;
		int vAlign = CENTER;
		
		switch(textPosition) {
		case LEFT:
			hAlign = RIGHT;
		break;
		case RIGHT:
			hAlign = LEFT;
			break;
		case UP: case DOWN:
			hAlign = CENTER;
			vAlign = CENTER;
			break;
		default:
			break;
		}
		
		g.textAlign(hAlign, vAlign);
	}
	
	float xOffset() {
		float dx = 0;
		switch(textPosition) {
		case LEFT:
			dx = -.25f * w();
			break;
		case RIGHT:
			dx = .25f * w();
			break;
		case DOWN: case UP:
			dx = 0;
			break;
		default:
			break;
		}
		
		return(dx);
	}
	
	float yOffset() {
		float dy = 0;
		switch(textPosition) {
		case LEFT: case RIGHT:
			dy = 0;
			break;
		case DOWN: 
			dy = 25;
			break;
		case UP:
			dy = -25;
			break;
		default:
			break;
		}
		
		return(dy * .5f);
	}

	void bg() {
		g.pushStyle();
		g.rectMode(CENTER);
		g.fill(255);
		g.stroke(30);
		
		float yi = y - yOffset();
		
		g.rect(x - xOffset(), yi, w, h);
		float dx = (w < 150) ? .48f : .49f;
		g.arc((float) (x - xOffset() - w * dx), yi, h, h, HALF_PI, HALF_PI * 3, OPEN);
		g.arc((float) (x - xOffset() + w * dx), yi, h, h, HALF_PI * 3, HALF_PI * 5, OPEN);
		
		
		float xi = (float) (x - xOffset() - w * .45);
		int total = 10;
		if (range() > 3) {
			total = PApplet.round(range());
		}
		float step = (float) (((x - xOffset() + w * .45) - xi) / total);
		float exp = (float) Math.log10(total);
		int apprxExp = Math.round(exp);
		float a = (step < 10) ? 2.5f : 1f;
		
		if (apprxExp >= 2) {
			step = (float) ((w * .9) / 10);
			a = 1f;
		}
		
		for (int i = 0; i < ((w * .9) / (a * step)) + 1; i++) {
			g.fill(30);
			g.strokeWeight((float) (1.5));
			if (i % 2 == 1)
				g.strokeWeight((float) (.5));
			g.rect(xi + step * a * i, yi, 2, 10);
		}
		
		g.strokeWeight(3);
		g.line(xi, y - yOffset(), xi + w * .9f, yi);
		g.popStyle();
	}

	void slider() {
		g.pushStyle();
		float xs = (float) (x - xOffset() - w * .45 + percent() * (w * .9));
		float ys = y - yOffset();
		g.rectMode(CENTER);
		g.fill(255);
		g.stroke(0);
		g.beginShape();
		g.vertex((float) (xs - 3.5), (float) (ys - 7.5));
		g.vertex((float) (xs - 3.5), (float) (ys + 5));
		g.vertex(xs, (float) (ys + 10));
		g.vertex((float) (xs + 3.5), (float) (ys + 5));
		g.vertex((float) (xs + 3.5), (float) (ys - 7.5));
		g.endShape(CLOSE);
		// g.rect(xs, y, 7, 15);
		g.popStyle();
	}
	
	public float percent() {
		return(percent);
	}

	void slide() {
//		float xs = (float) (x - w * .45 + percent() * (w * .9));
		if (slid() || following) {
			percent = PApplet.constrain(PApplet.map((float) (p.mouseX),
					(float) (x - xOffset() - .45 * w), (float) (x - xOffset() + .45 * w), 0.0f, 1.0f),
					0.0f, 1.0f);
		}
		
		if (!pressed && slid()) {
			following = !following;
		} else if ((!pressed && !slid() && PApplet.dist(p.mouseX, p.mouseY, x - xOffset(), y - yOffset()) > w * 1.5) || (!p.mousePressed)) {
			following = false;
		}
		
		pressed = p.mousePressed;
		
	}

	boolean slid() {
		return (p.mousePressed && p.mouseX > x - xOffset() - .45 * w && p.mouseX < x - xOffset() + .45 * w && p.mouseY > y - yOffset() - 7.5 && p.mouseY < y - yOffset() + 7.5);
	}
	
	/**
	 * Returns value marked by the slider's position.
	 * @return float, the value that the slider is at currently
	 */
	public float p() {
		return (percent * (range()) + min);
	}
	
	public float range() {
		return(max - min);
	}

	void init(PApplet p, float x, float y, float min, float max) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.min = min;
		this.max = max;

		this.percent = .5f;
		this.w = 150;
		this.h = 20;
		f = p.createFont("Georgia", 20, true);
		g = p.g;
	}
	
	public float low() {
		return(min);
	}
	
	public float high() {
		return(max);
	}
	
	@Override
	public String returnName() {
		return("Slider");
	}
	
	@Override
	public float w() {
		float w0 = w + h;
		g.textFont(f);
		float textwid = g.textWidth(label);
		switch(textPosition) {
		case UP: case DOWN:
			w0 = (textwid < w0) ? w0 : textwid;
			break;
		case RIGHT: case LEFT:
			w0 += 30 + textwid;
			break;
		default:
			break;
		}
		return(w0);
	}
}
