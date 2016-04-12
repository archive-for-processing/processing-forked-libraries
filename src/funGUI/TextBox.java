package funGUI;
import processing.core.*;
import processing.data.*;

/**
 * The TextBox class provides structure for
 * creating boxes where text can be input.
 * The input text can be output as a StringList
 * with recordLines() or record() or as Strings
 * with recordedLines() or recorded(), the latter
 * two take the index of the line desired as
 * input. record() and recorded() give the 
 * text paragraph by paragraph, whereas recordedLInes()
 * and recordLines give the text line by line.
 * @author Audrey Seo
 *
 */
public class TextBox implements PConstants {
	private float x, y;
	private float w, h;
	public String text;
	int cursor_placement;
	int current_line = 0;
	int beginning_line = 0;
	int maxRows = 0;
	int begin = 0;
//	private float textWidth;
	private PFont font;
	private Timer t, cursorTimer;
//	private Timer offTime;
	boolean pressed = false;
	boolean clickedIn = false;
	StringList paragraph = new StringList();
	StringList lines = new StringList();
	private PApplet p;
	private PGraphics g;
	String command;
	String current;
	float fontSize;


	/**
	 * Constructor, which involves a text box without a message above it.
	 * @param p  		PApplet, the parent of the sketch, usually "this"
	 * @param x  		float, the x-coordinate of the center of text box
	 * @param y  		float, the y-coordinate of the center of the text box
	 * @param w  		float, the width of the text box
	 * @param h  		float, the height of the text box
	 * @param fontSize	float, the size of the font used
	 */
	public TextBox(PApplet p, float x, float y, float w, float h, float fontSize) {
		init(p, x, y, w, h, fontSize);
	}

	/**
	 * Constructor that involves a text box with a message above it.
	 * @param p  			PApplet, the parent of the sketch, usually "this"
	 * @param x  			float, the x-coordinate of the center of the text box
	 * @param y  			float, the y-coordinate of the center of the text box
	 * @param w  			float, the width of the text box rectangle
	 * @param h  			float, the height of the text box rectangle
	 * @param name  		String, the message that floats to the left of the text box
	 * @param fontSize		float, the size of the text drawn to the screen
	 */
	public TextBox(PApplet p, float x, float y, float w, float h, float fontSize, 
			String name) {
		command = name;
		init(p, x, y, w, h, fontSize);
	}

	/**
	 * Boolean function that shows whether this text box has been clicked in.
	 * @return  boolean, true/false: the mouse was pressed inside the text box
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
		g.pushStyle();
		textBox();

		cursorDisplay();
		textDisplay();
		g.popStyle();
	}

	void textBox() {
		g.fill(255);
		g.stroke(30);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
	}

	void cursorDisplay() {
		if (clickedIn && cursorTimer.timeLeft() > 1000) {
			// Color - 225/255 % gray
			g.fill(30);
			g.noStroke();
			//PApplet.println("Begin: " + begin());
			//PApplet.println("End: " + end());
//			int num_lines = PApplet.floor(((h / (fontSize + 2))));
			g.textFont(font);
			g.rect((float) (x - w * .45 + g.textWidth(current)), (float) (y - (h * .5) + ((current_line - beginning_line) + .5) * (fontSize + 2)), 3, fontSize);
		} else if (cursorTimer.done()) {
			cursorTimer.reset();
		}
	}

	void textDisplay() {
		g.textAlign(LEFT, TOP);
		g.textFont(font);
		g.fill(0);
		int i;
		for (i = beginning_line; i < endDisplay(); i++) {
			g.fill(0);
			float y1 = (float) (y - ( h * .5 ) + (i - beginning_line) * (fontSize + 2));
			PApplet.println(y1);
			g.text(lines.get(i), (float) (x - w * .45), y1);
		}

		//# of lines down from the top
//		int num_lines = PApplet.floor((h / (fontSize * 2))) - beginning_line;
		float y1 = (float) (y - (h * .5) + (i - beginning_line) * (fontSize + 2));
		g.text(current, (float) (x - w * .45), y1);
		if (command != null) {
			g.text(command, (float) (x - w * .5), y - 25);
		}
	}

	int endDisplay() {
		if (lines.size() > maxRows && lines.size() - beginning_line >= maxRows) {
			return(beginning_line + maxRows - 1);
		}
		return(lines.size());
	}

	int begin() {
		return ((end() > 0) ? begin : 0);
	}

	int end() {
		return ((cursor_placement <= text.length()) ? cursor_placement : text.length());
	}

	void type() {
		if (clicked()) {
			clickedIn = true;
			cursor_placement = begin()
					+ PApplet.round(PApplet.map((float) (p.mouseX), 
							(float) (x - w * .5), (float) (x + w * .5), 
							(float) (0.0), (float) (9.0)));
		}
		if (p.mousePressed && !clicked())
			clickedIn = false;

		if (p.keyPressed && typed() && clickedIn) {
			if (!pressed && p.key != CODED && p.key != DELETE && p.key != BACKSPACE) {
				text += PApplet.str(p.key);
				current += PApplet.str(p.key);

				cursor_placement++;
				g.textFont(font);
				//if (g.textWidth(current) > w * .9) {
					//  //begin++;
					//  current = "";
				//  current_line++;
				//}
			} else if (!pressed) {
				if (current.length() > 0 && (p.key == DELETE || p.key == BACKSPACE)) {
					text = backspace(text);
					current = backspace(current);
					if (cursor_placement > 0)
						cursor_placement--;
					if (begin > 0);
					//begin--;
					t.reset();
				} else if (cursor_placement > 1 && (p.key == CODED)) {
					if (p.keyCode == RIGHT && cursor_placement < text.length()) {
						cursor_placement++;
						begin++;
					} else if (p.keyCode == LEFT && cursor_placement > 1
							&& begin > 1) {
						cursor_placement--;
						//begin--;
					} else if (p.keyCode == DOWN && current_line < totalLines()) {
						current_line ++;
						if ((current_line - beginning_line) == maxRows && beginning_line < totalLines()) {
							beginning_line++;
						}
					} else if (p.keyCode == UP && current_line > 0) {
						current_line--;
						if (current_line < beginning_line && beginning_line > 0) {
							beginning_line--;
						}
					}
				}
			}
			g.textFont(font);
			if (g.textWidth(current) > .9 * w && linesDiffer()) {
				split();
				//lines.append(text.substring(begin, text.length() - 2));
				//begin = text.length() - 2 ;

				//for (int i= 0; i < lines.size(); i++) {
				//  println(lines.get(i));
				//}
				//println("");
			}
			enter();
			pressed = p.keyPressed;
			t.reset();
		} else if (!p.keyPressed) {
			pressed = p.keyPressed;
		}
		// PApplet.println(clickedIn + "  " + text);
	}

	void add() {
		current += PApplet.str(p.key);
		text += PApplet.str(p.key);
	}

	String backspace(String str) {
		return(str.substring(0, str.length() - 1));
	}

	void updatePosition() {
		// Update the posiition of the current line
		current_line++;

		// Update the placement of the cursor
		cursor_placement = begin + 1;
		if (beginning_line > maxRows) {
			//Increment the beginning line
			beginning_line++;
		}
	}

	int totalLines() {
		return(lines.size());
	}

	int oldIndex() {
		int n = 0;
		for (int i = 0; i < lines.size(); i++) {
			n += lines.get(i).length();
		}
		return(n);
	}

	void split() {
		int newIndex = oldIndex();

		g.textFont(font);
		String newText = text.substring(newIndex, text.length() - 2);
		lines.append(newText);

		begin = text.length() - 2;
		current = text.substring(text.length() - 2, text.length());
		current_line ++;

		for (int i = 0; i < lines.size(); i++) {
			PApplet.println(lines.get(i));
		}

		if (multipleOf(maxRows, (current_line - beginning_line))) {
			beginning_line++;
		}
	}

	boolean multipleOf(int n, int n1) {
		return(n1 % n == 0);
	}

	boolean linesDiffer() {
		if (lines.size() == 0) return(true);
		if (lines.get(lines.size() - 1).length() > 0 ) {
			if (lines.get(lines.size() - 1).equals(current)) {
				return (false);
			} else {
				return(true);
			}
		}

		return(false);
	}

	boolean typed() {
		boolean two = false;
		if (text.length() > 0) {
			two = p.key != text.charAt(text.length() - 1);
		}
		return (!pressed || two);
	}

	void limit() {
		cursor_placement = PApplet.constrain(cursor_placement, 0, text.length());
		begin = PApplet.constrain(begin, 0, text.length());
	}

	void enter() {
		if (p.key == ENTER || p.key == RETURN) {
			String new_text = text.substring(0, text.length());
			paragraph.append(new_text);
			text = text + "\n";
		}
	}

	public String recorded(int n) {
		n = PApplet.constrain(n, 0, paragraph.size() - 1);
		return(paragraph.get(n));
	}

	public StringList record() {
		return(paragraph.copy());
	}

	public String recordedLine(int n) {
		n = PApplet.constrain(n, 0, lines.size() - 1);
		return(lines.get(n));
	}

	public StringList recordLine() {
		return(lines.copy());
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

	void init(PApplet p, float x, float y, float w, float h, float fontSize) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		maxRows = PApplet.floor(h / (fontSize + 2));
		text = "";
		current = "";
		t = new Timer(1, p);
		cursorTimer = new Timer(2000, p);
//		offTime = new Timer(500, p);
		cursor_placement = 0;
		this.p = p;
		this.g = p.g;
		font = this.p.createFont("Times-New-Roman", fontSize, true);
		g.textFont(font);
//		textWidth = g.textWidth(text);
		cursorTimer.reset();
		this.fontSize = fontSize;
	}
}
