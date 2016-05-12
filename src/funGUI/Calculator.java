package funGUI;
import processing.core.*;
import processing.data.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * The Calculator class creates an actual calculator inside your
 * Processing sketch. While I'm not entirely sure why this would
 * be necessary at all, it's pretty neat and you can access the
 * answers the calculator finds through the function lastAnswer().
 * This is almost more of an example of the something this library
 * has enabled me to do within a reasonable time instead of an actual
 * widget for the library.
 * @author Audrey Seo
 */
public class Calculator extends Frame {
	
	// All of the different buttons needed by the calculator.
	RoundButton [][] nums = new RoundButton [3][4];
	RoundToggleButton [] operations = new RoundToggleButton[4];
	TextScroll display;
	RoundedRectButton equals;
	
	// Stores all of the calculated numbers
	FloatList answers;
	
	// Holds all of the math going on - numbers only - for display
	String math = "0";
	
	PFont font;
	
	int chosenOp = -1;
	float firstNum = 0;
	float secondNum = 0;
	
	boolean pressed = false;
	boolean runButtons = false;
	boolean runKeys = false;
	
	/**
	 * Constructor of the class.
	 * @param p		PApplet, the parent of the sketch, usually "this"
	 * @param x		float, the x-coordinate of the center of the calculator
	 * @param y		float, the y-coordinate of the center of the calculator
	 * @param w		float, the length along the x-axis of the calculator
	 * @param h		float, the length along the y-axis of the calculator
	 */
	public Calculator(PApplet p, float x, float y, float w, float h) {
		this.p = p;
		this.g = p.g;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		display = new TextScroll(p, x, y - 90, 120, 20);
		
		int [] colors = {g.color(220), g.color(255, 255, 255), g.color(240, 240, 240)};
		
		equals = new RoundedRectButton(p, x + 80, y + 75, 20, 75, colors, "=", .5f, 12);
		
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				String chars = Integer.toString(9 - ((2 - i) + j * 3));
				if (i < 3 && j < 3) {
					nums[i][j] = new RoundButton(p, x + (i - 1) * 50, y + (j - 1) * 50, 20, chars.charAt(0));
				} else {
					nums[i][j] = new RoundButton(p, x + (i - 1) * 50, y + (j - 1) * 50, 20, ' ');
				}
			}
		}
		nums[0][3] = new RoundButton(p, x - 50, y + 100, 20, '0');
		nums[1][3] = new RoundButton(p, x, y + 100, 20, '.');
		nums[2][3] = new RoundButton(p, x + 50, y + 100, 20, 'c');
		font = p.createFont(REG_SANSS_TXT, LGTXTSIZE);
		
		char [] c = {'÷', 'x', '-', '+'};
		for (int i = 0; i < operations.length; i++) {
			operations[i] = new RoundToggleButton(p, x + 80, y + (i - 2) * 25, 10, c[i]);
		}
		
		p.registerMethod("keyEvent", this);
		p.registerMethod("mouseEvent", this);
	}
	

	@Override
	protected void display() {
		equals.draw();
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				nums[i][j].draw();
			}
		}
		for (int i = 0; i < operations.length; i++) {
			operations[i].draw();
		}
		
		if (runButtons) {
			runButtons = false;
			buttons();
		} else if (runKeys) {
			equate();
		}
		check();
	}
	
	@Override
	protected void text() {
		g.pushStyle();
//		g.textFont(font);
//		g.textAlign(CENTER, CENTER);
//		g.text(math, x, y - 70);
		display.assign(math);
		display.draw();
		g.popStyle();
	}

	void check() {
		if (!pressed && p.mousePressed && anyPressed() >= 0) {
			PApplet.println("ChosenOP: " + chosenOp);
			int n = anyPressed();
			int n1 = (n >> 8) & 0xF;
			int n2 = n & 0xF;
			if (chosenOp >= 0 && firstNum == 0) {
				firstNum = Float.valueOf(math);
				PApplet.println("FirstNum: " + firstNum);
				math = "0";
			}
			String s = String.valueOf(nums[n1][n2].c);
			if (s.equals("c")) {
				math = "0";
				firstNum = 0;
				secondNum = 0;
				chosenOp = -1;
				for (int i = 0; i < operations.length; i++) {
					operations[i].on = false;
				}
			} else {
				if (math.equals("0") && !(s.equals("."))) {
					math = "";
				}
				math += s;
			}
		}
		pressed = p.mousePressed && anyPressed() >= 0;
		
		if (operationsPressed() > 0) {
			if (operationsPressed() > 1) {
				if (chosenOp < 0) {
					
				} else {
					for (int i = 0; i < operations.length; i++) {
						if (operations[i].on() && i != chosenOp) {
							chosenOp = i;
						} else {
							operations[i].on = false;
						}
					}
				}
			} else {
				for (int i = 0; i < operations.length; i++) {
					if (operations[i].on()) {
						chosenOp = i;
					}
				}
			}
		}
	}
	
	/**
	 * Triggered when a key event occurs. This should only
	 * be called by the sketch itself.
	 * @param k		KeyEvent, the keyboard event that just happened
	 */
	public void keyEvent(KeyEvent k) {
		if ((k.getKey() == ENTER || k.getKey() == RETURN) && k.getAction() == KeyEvent.RELEASE) {
//			equate();
			runKeys = true;
		}
	}
	
	/**
	 * Triggered when a mouse event (clicking, releasing,
	 * etc.) This is only meant to be called by the sketch
	 * itself.
	 * @param m		MouseEvent, the mouse event that just occurred
	 */
	public void mouseEvent(MouseEvent m) {
		if (m.getAction() == MouseEvent.PRESS) {
			runButtons = true;
		}
	}
	
	/**
	 * Checks the buttons to see if any other functions
	 * need to be called.
	 */
	public void buttons() {
//		record("Start buttons");
		if (equals != null) {
//			record("Equals != null");
			if (!equals.pressed){
//				record("Equals.pressed is false");
				if (equals.clicked()) equate();
			}
		}
	}
	
	void equate() {
		secondNum = Float.valueOf(math);
		float result = 0;
		if (chosenOp >= 0) {
			switch(operations[chosenOp].c) {
			case '÷':
				result = firstNum / secondNum;
				break;
			case 'x':
				result = firstNum * secondNum;
				break;
			case '-':
				result = firstNum - secondNum;
				break;
			case '+':
				result = firstNum + secondNum;
				break;
			default:
				break;
			}
		}
		answers.append(result);
		math = String.valueOf(result);
		firstNum = Float.valueOf(result);
		secondNum = 0;
		chosenOp = -1;
		for (int i = 0; i < operations.length; i++) {
			operations[i].on = false;
		}
		
	}

	public boolean haveAnswers() {
		return(answers.size() > 0);
	}

	/**
	 * Retrieves the most recent answer the calculator found.
	 * @return      float, the most recent answer the calculator found, or 0.
	 */
	public float lastAnswer() {
		if (haveAnswers()) {
			return(answers.get(answers.size() - 1));
		}
		return(0);
	}

	int operationsPressed() {
		int total = 0;
		for (int i = 0; i < operations.length; i++) {
			if (operations[i].on()) {
				total++;
			}
		}
		return(total);
	}
	
	int anyPressed() {
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if (nums[i][j].clicked()) {
					int index = (i << 8) + j;
					return(index);
				}
			}
		}
		return(-1);
	}
	
	
	@Override
	public String returnName() {
		return("Calculator");
	}
}
