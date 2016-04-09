package funGUI;
import processing.core.*;

public class Calculator extends Frame {
	RoundButton [][] nums = new RoundButton [3][4];
	RoundButton [] operations = new RoundButton[4];
	boolean pressed = false;
	String math = "0";
	PFont font;
	TextScroll display;
	
	public Calculator(PApplet p, float x, float y, float w, float h) {
		this.p = p;
		this.g = p.g;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		display = new TextScroll(p, x, y - 90, 120, 20);
		
		
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
			operations[i] = new RoundButton(p, x + 80, y + (i - 2) * 25, 10, c[i]);
		}
	}
	
	@Override
	public void display() {
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				nums[i][j].draw();
			}
		}
		for (int i = 0; i < operations.length; i++) {
			operations[i].draw();
		}
		check();
	}
	
	@Override
	public void text() {
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
			int n = anyPressed();
			int n1 = (n >> 8) & 0xF;
			int n2 = n & 0xF;
			String s = String.valueOf(nums[n1][n2].c);
			if (s.equals("c")) {
				math = "0";
			} else {
				if (math.equals("0") && !(s.equals("."))) {
					math = "";
				}
				math += s;
			}
		}
		pressed = p.mousePressed && anyPressed() >= 0;
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
