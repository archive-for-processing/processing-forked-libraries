package funGUI;

import processing.core.*;

public class ToggleButton extends Button implements PConstants {
	boolean on = false;
	public float xOffset = .25f;
	public float trueWidth = 0;
	public Timer t;
	PFont font, liFont;
	protected float r;
	String stateOn = "ON";
	String stateOff = "OFF";
	Direction textPosition = Direction.UP;
	boolean pressed = false;

	/**
	 * Makes a ToggleButton object that is toggled on and off, i.e. like a
	 * switch.
	 * 
	 * @param p
	 *            the instance of PApplet, usually "this"
	 * @param x
	 *            float
	 * @param y
	 *            float
	 */
	public ToggleButton(PApplet p, float x, float y) {
		init(p, x, y);
	}

	/**
	 * The other ToggleButton constructor.
	 * 
	 * @param p
	 *            the instance of PApplet (the Processing sketch), usually
	 *            "this"
	 * @param x
	 *            float
	 * @param y
	 *            float
	 * @param l
	 *            String, the button's descriptive name, i.e. "Display Bunnies."
	 */
	public ToggleButton(PApplet p, float x, float y, String l) {
		init(p, x, y);
		label(l);
		position();
	}

	public ToggleButton(PApplet p, float x, float y, String l, Direction d) {
		init(p, x, y);
		label(l);
		textPosition = d;
		
		position();
	}
	
	void position() {
		g.textFont(font);
		float tw = g.textWidth(label);
		float half_w = 40 + 20;
		trueWidth = tw + half_w + 30;
//		float wi = tw + half_w + 30; // 30 pixels of padding in between
//		this.xOffset = 1 - ((tw+15) / wi);
		this.xOffset = .5f; //PApplet.abs(1  - (tw / (2 * half_w)));
		PApplet.println("xOffset: " + xOffset);
	}
	
	public void position(Direction d) {
		textPosition = d;
	}

	public void label(String l) {
		this.label = l;
		position();
//		g.textFont(font);
//		float full = PApplet.abs((g.textWidth(label)));
//		this.xOffset = (float) ((full - 30.0) / full);
	}

	public void name(String name) {
		label(name);
	}

	protected void text(float dx, float dy) {
		if (notequals(label, "")) {
			g.pushStyle();
			g.fill(0);
			g.textFont(liFont);
			if (dy > 0) {
				g.textAlign(CENTER, TOP);
			} else if (dy < 0) {
				g.textAlign(CENTER, BOTTOM);
			} else {
				if (dx > 0) {
					g.textAlign(RIGHT, CENTER);
				} else {
					g.textAlign(LEFT, CENTER);
				}
			}
			g.text(label, x + dx, y + dy);
			g.popStyle();
		}
	}

	@Override
	protected void display() {
		offset();
		toggle();
	}

	protected void offset() {
		g.textFont(font);
		float dx1 = -xDisplacement();
		float dx = xDisplacement();
		float dy = yDisplacement();
		// Draw the background components of the button
		
		// Draw the front components of the button (i.e. the switch, the words
		// On or Off, etc.)
		if (dx1 < 0) {
			dx1 += 30;
		} else if (dx1 > 0) {
			dx1 -= 30;
		}
		buttonBase(dx1, -dy);
		buttonFace(dx1, -dy);
//		if (dx > 0) {
////			dx += 55;
//			dx += 70;
//		} else if (dx < 0) {
////			dx -= 55;
//			dx -= 70;
//		}
		text(dx, dy);
	}

	protected void buttonFace(float dx, float dy) {
		g.fill(255);
		g.stroke(200);
		g.strokeWeight(3.2f);
		int a = -1;
		int b = 1;
		String text = stateOn;
		if (!on()) {
			a *= -1;
			b *= -1;
			text = stateOff;
		}

		g.ellipse(x + dx + a * w2, y + dy, r * 2, r * 2);
		// shadow
		g.stroke(150);
		g.arc(x + dx + a * w2, y + dy, r * 2, r * 2, rads(60), rads(260));

		g.pushStyle();
		g.fill(255);
		g.textFont(font);
		g.textAlign(CENTER, CENTER);
		g.text(text, (float) (x + dx + b * w2 * .5),
				(float) (y + dy - 2.5));
		g.popStyle();
	}

	protected void buttonBase(float dx, float dy) {
		g.pushStyle();
		if (on)
			g.fill(50, 240, 90);
		else
			g.fill(255, 30, 10);

		g.stroke(180);
		g.strokeWeight(3);
		// The two left sides
		openarc(x - w2 + dx, y + dy, r, HALF_PI, HALF_PI * 3);
		openarc(x + w2 + dx, y + dy, r, HALF_PI * 3, TWO_PI + HALF_PI);

		g.rectMode(CENTER);
		g.rect(x + dx, y + dy, w, w2);
		g.noStroke();
		float offset = (float) (w2 * 0.90);
		// float shrink = (float) 3.75;
		float ychng = (float) .5;
		openarc((float) (x + dx - offset), (float) (y + dy + ychng),
				(float) (r * .85), rads(60), rads(300));
		openarc((float) (x + dx + offset - .05), (float) (y + dy + ychng),
				(float) (r * .89), rads(245), rads(500));
		g.popStyle();
	}

	@Override
	protected void style() {

	}

	/**
	 * If the button is clicked, the button becomes on if it was off and off if
	 * it was on.
	 */
	protected void toggle() {
		if (clicked()) {
			if (!pressed && p.mousePressed) { // && t.done())
				on = !on;
				pressed = true;
			}
			t.reset();
		}
		pressed = p.mousePressed;
	}

	protected void arch(float x, float y, float r, float s, float e) {
		g.arc(x, y, r * 2, r * 2, s, e);
	}

	protected void openarc(float x, float y, float r, float s, float e) {
		g.arc(x, y, r * 2, r * 2, s, e, OPEN);
	}

	@Override
	public float w() {
		float dx = 25;
//		float dy = 25;
		if (textPosition == Direction.DOWN || textPosition == Direction.UP) {
			return (x);
		} else if (textPosition == Direction.LEFT) {
			return ((float) (x - dx - .5 * g.textWidth(label)));
		} else {
			return ((float) (x + dx + .5 * g.textWidth(label)));
		}
	}

	@Override
	public float h() {
//		float dx = 25;
		float dy = 25;
		if (textPosition == Direction.LEFT || textPosition == Direction.RIGHT) {
			return (y);
		} else if (textPosition == Direction.UP) {
			return ((float) (y - dy));
		} else {
			return ((float) (y + dy));
		}
	}

	public float adjustedX() {
		g.textFont(font);
//		float dx1 = (trueWidth * (1 - xOffset)) + 10f;
		float dx = (trueWidth * xOffset);
//		float dy = 12;
		switch (textPosition) {
		case UP:
			dx = 0;
//			dx1 = 0;
//			dy *= -1;
//			break;
		case DOWN:
			dx = 0;
//			dx1 = 0;
			break;
		case RIGHT:
//			dy = 0;
			break;
		case LEFT:
			dx *= -1;
//			dx1 *= -1;
//			dy = 0;
			break;
		default:
			break;
		}
		return (x + dx);
	}

	public float adjustedY() {
		g.textFont(font);
//		float dx1 = (trueWidth * (1 - xOffset));
//		float dx = (trueWidth * xOffset) + 10f;
		float dy = 12;
		switch (textPosition) {
		case UP:
//			dx = 0;
//			dx1 = 0;
			dy *= -1;
			break;
		case DOWN:
//			dx = 0;
//			dx1 = 0;
			break;
		case RIGHT:
			dy = 0;
			break;
		case LEFT:
//			dx *= -1;
//			dx1 *= -1;
			dy = 0;
			break;
		default:
			break;
		}

		return (y + dy);
	}
	
	protected float xDisplacement() {
		float dx1 = (trueWidth * (1 - xOffset));
		switch (textPosition) {
		case UP:
			dx1 = 0;
			break;
		case DOWN:
			dx1 = 0;
			break;
		case RIGHT:
			break;
		case LEFT:
			dx1 *= -1;
			break;
		default:
			break;
		}
		return(dx1);
	}
	
	protected float yDisplacement() {
		float dy = 12;
		switch (textPosition) {
		case UP:
			dy *= -1;
			break;
		case DOWN:
			break;
		case RIGHT:
			dy = 0;
			break;
		case LEFT:
			dy = 0;
			break;
		default:
			break;
		}
		return(dy);
	}
	

	@Override
	public boolean clicked() {
		g.textFont(font);
		float dx1 = (trueWidth * (1 - xOffset));
		float dy = 12;
		switch (textPosition) {
		case UP:
			dx1 = 0;
			dy *= -1;
			break;
		case DOWN:
			dx1 = 0;
			break;
		case RIGHT:
			dy = 0;
			break;
		case LEFT:
			dx1 *= -1;
			dy = 0;
			break;
		default:
			break;
		}
		//pressed = p.mousePressed;
		if (dx1 < 0) {
			dx1 += 30;
		} else if (dx1 > 0) {
			dx1 -= 30;
		}
		return (super.clicked(-dx1, -dy) || edgesClicked(-dx1, -dy));
	}
	
	protected boolean edgesClicked(float dx, float dy) {
		float ychng = (float) .5;
		float x1 = (float) ((x + dx - w2));
		float y1 = (float) (y + dy + ychng);
		float x2 = (float) (x + dx + w2);
		boolean leftCircleClicked = PApplet.dist(x1, y1, p.mouseX, p.mouseY) <= r;
		boolean rightCircleClicked = PApplet.dist(x2, y1, p.mouseX, p.mouseY) <= r;
		return(leftCircleClicked || rightCircleClicked);
	}
	
	

	protected float rads(float degrees) {
		return ((float) ((degrees / 180.0) * PI));
	}

	public boolean on() {
		return (on);
	}

	protected boolean notequals(String s1, String s2) {
		return (!(s1.equals(s2)));
	}
	
	public String returnName() {
		return("ToggleButton");
	}

	/**
	 * Enacts all of the basic functionality of the two constructors.
	 * 
	 * @param x
	 *            float, the x-coordinate of the button's center
	 * @param y
	 *            float, the y-coordinate of the button's center
	 */
	private void init(PApplet p, float x, float y) {
		this.p = p;
		this.x = x;
		this.y = y;
		t = new Timer(60, p);
		t.reset();
		w = 40.0f;
		h = 20;
		r = 10.0f;
		w2 = 20.0f;
		
		font = p.createFont("Optima-Bold", 17);
		liFont = p.createFont("Helvetica-Regular", 18);
		g = p.g;
		g.textFont(font);
		
	}
}
