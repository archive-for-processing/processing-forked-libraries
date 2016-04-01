package funGUI;

import processing.core.*;
import processing.data.StringList;

/**
 * The TextBoxStrip class embodies the little text
 * inputs that are only one line high. Press enter/
 * return to "enter" the information into a log of
 * strings, which are accessible to the user. Useful
 * for creating forms and other such interfaces.
 * 
 * @author Audrey Seo
 *
 */
public class TextStrip implements PConstants {
	private float x, y;
	private float w, h;
	public String text;
	int cursor_placement;
	int begin = 0;
//	private float textWidth;
	private PFont font;
	protected Timer t, cursorTimer, offTime;
	boolean pressed = false;
	boolean clickedIn = false;
	StringList lines = new StringList();
	protected PApplet p;
	protected PGraphics g;
	String command;
	
	/**
	 * Constructor, which involves a text box without a message preceding it.
	 * @param p	PApplet, the parent of the sketch, usually "this"
	 * @param x	float, the x-coordinate of the center of text box
	 * @param y	float, the y-coordinate of the center of the text box
	 * @param w	float, the width of the text box
	 * @param h	float, the height of the text box
	 */
	public TextStrip(PApplet p, float x, float y, float w, float h) {
		init(p, x, y, w, h);
	}
	
	/**
	 * Constructor that involves a text box with a message preceding it.
	 * @param p	PApplet, the parent of the sketch, usually "this"
	 * @param x	float, the x-coordinate of the center of the text box
	 * @param y	float, the y-coordinate of the center of the text box
	 * @param w	float, the width of the text box rectangle
	 * @param h	float, the height of the text box rectangle
	 * @param name	String, the message that floats to the left of the text box
	 */
	public TextStrip(PApplet p, float x, float y, float w, float h,
			String name) {
		command = name;
		init(p, x, y, w, h);
	}
	
	/**
	 * Boolean function that shows whether this text box has been clicked in.
	 * @return	boolean, true/false: the mouse was pressed inside the text box
	 */
	public boolean clicked() {
		return (p.mouseX < x + w * .5 && p.mouseX > x - w * .5
				&& p.mouseY < y + h * .5 && p.mouseY > y - h * .5 && p.mousePressed);
	}
	/**
	 * Shows the text box on the screen.
	 */
	public void draw() {
		type();
		display();
		limit();
	}

	void display() {
		g.fill(255);
		g.stroke(30);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
		cursorDisplay();
		textDisplay();
	}
	
	void cursorDisplay() {
		if (clickedIn && cursorTimer.timeLeft() > 1000) {
			g.fill(30);
			PApplet.println("Begin: " + begin());
			PApplet.println("End: " + end());
			g.textFont(font);
			g.rect((float) (x - w * .45 + g.textWidth(text.substring(begin(),
					end()))), y, 3, (float) (h * .9));
		} else if (cursorTimer.done()) {
			cursorTimer.reset();
		}
	}

	void textDisplay() {
		g.textAlign(LEFT, CENTER);
		g.textFont(font);
		g.fill(0);
		g.text(text.substring(begin(), end()), (float) (x - w * .45), y);
		if (command != null) {
			g.text(command, (float) (x - w * .45), y - 25);
		}

	}

	int begin() {
		return ((end() > 0) ? begin : 0);
	}

	int end() {
		return ((cursor_placement <= text.length()) ? cursor_placement : text
				.length());
	}
	
	protected void add() {
		text += addition();
		cursor_placement++;
		g.textFont(font);
		if (g.textWidth(text.substring(begin, text.length())) > w * .8) {
			begin++;
		}
	}
	
	protected String addition() {
		return(PApplet.str(p.key));
	}
	
	protected boolean addingConditions() {
		return(!pressed && p.key != CODED && p.key != DELETE
					&& p.key != BACKSPACE);
	}

	void type() {
		if (clicked()) {
			clickedIn = true;
			cursor_placement = begin() + PApplet.round(PApplet.map((float) (p.mouseX),
							(float) (x - w * .5), (float) (x + w * .5),
							(float) (0.0), (float) (9.0)));
		}
		if (p.mousePressed && !clicked()) {
			clickedIn = false;
		}
		
		if (p.keyPressed && typed() && clickedIn) {
			if (addingConditions()) {
				add();
			} else if (!pressed) {
				if (text.length() > 0&& (p.key == DELETE || p.key == BACKSPACE)) {
					text = backspace(text);
					decrementPosition();
				} else if (cursor_placement > 1 && (p.key == CODED)) {
					moveCursor();
				}
			}
			enter();
			pressed = p.keyPressed;
			t.reset();
		} else if (!p.keyPressed) {
			pressed = p.keyPressed;
		}
		// PApplet.println(clickedIn + "  " + text);
	}
	
	protected void moveCursor() {
		if (p.keyCode == RIGHT && cursor_placement < text.length()) {
			cursor_placement++;
			begin++;
		} else if (p.keyCode == LEFT && cursor_placement > 1
				&& begin > 1) {
			cursor_placement--;
			begin--;
		}
	}
	
	protected void decrementPosition() {
		if (cursor_placement > 0)
			cursor_placement--;
		if (begin > 0)
			begin--;
	}

	protected String backspace(String str) {
		return(str.substring(0, str.length() - 1));
	}

	boolean typed() {
		boolean two = false;
		if (text.length() > 0) {
			two = p.key != text.charAt(text.length() - 1);
		}
		return (!pressed || two);
	}

	void limit() {
		cursor_placement = PApplet
				.constrain(cursor_placement, 0, text.length());
		begin = PApplet.constrain(begin, 0, text.length() - wLimits());
	}

	void enter() {
		if (p.key == ENTER || p.key == RETURN) {
			String new_text = text.substring(0, text.length());
			lines.append(new_text);
			text = "";
		}
	}

	public String recorded(int n) {
		n = PApplet.constrain(n, 0, lines.size() - 1);
		return (lines.get(n));
	}

	public StringList record() {
		return (lines.copy());
	}

	int wLimits() {
		g.textFont(font);
		float size = g.textWidth(text);
		if (text.length() > 0) {
			for (int i = text.length() - 1; i > 0; i--) {
				size = g.textWidth(text.substring(i, text.length()));
				if (size >= .9 * w) {
					return (text.length() - i);
				}
			}
		}
		return (0);
	}

	void init(PApplet p, float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		text = "";
		t = new Timer(1, p);
		cursorTimer = new Timer(2000, p);
		offTime = new Timer(500, p);
		cursor_placement = 0;
		this.p = p;
		this.g = p.g;
		font = this.p.createFont("Times-New-Roman", (float) (h * .8), true);
		g.textFont(font);
//		textWidth = g.textWidth(text);
		cursorTimer.reset();
	}
}
