package funGUI;

import processing.core.*;

/**
 * The RectButton class is one of the many options included 
 * in the FunGUI library for classes. This button returns a
 * boolean if the mouse is pressed over it.
 * @author Audrey Seo
 *
 */
public class RectButton extends Button {
	// float, the ratios of the size of the PApplet sketch
	float hratio = 1;
	float wratio = 1;

	// boolean
	boolean pressed = false;
	boolean rollover;

	// The button's label
	String buttonText;

	// PFont
	PFont font;
	PFont smallfont;
	PFont minifont;
	PFont sFont;
	PFont mFont;
	PFont myfont1;

	// PFont sizes
	int fsize;  // For font
	int sfsize;  // For smallfont
	int mfsize;  // For minifont
	int beginningWidth = 0;
	int beginningHeight = 0;

	// Colors
	int outside;
	int inside;
	int cGrey15;
	int [] scheme = new int [3]; //buttons need three colors typically

	// Obsolete
	float wid;
	float hei;

	// Counters
	int counting = 0;
	int counter = 0;
	PApplet p;
	PGraphics g;
	
	/**
	 * Constructor of a rectangular button.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param nx		float, the x-coordinate of the location of the button
	 * @param ny		float, the y-coordinate of the location of the button
	 * @param nw		float, the width of the button
	 * @param nh		float, the height of the button
	 * @param ncolors	color [], the colors of the button (0-outside, 1-overlay (for clicked status), 2-inside)
	 * @param ntext		String, the label for the button
	 */
	public RectButton(PApplet p, float nx, float ny, float nw, float nh, String ntext) {
		wid = 20 / w;
		hei = 20 / h;
		this.x = nx;
		this.y = ny;
		this.w = nw;
		this.h = nh;
		this.p = p;
		this.g = p.g;
		if (w * wrat() < 26 * wrat() && h * hrat() < 26 * hrat()) {
			wid = 9/w;
			hei = 9/w;
			PApplet.println(ntext);
		}
		scheme = standardColors();
		buttonText = ntext;
		font = p.createFont("Baskerville-Italic", 200, true);
		fsize = PApplet.floor((float) (nh - 20));

		init();
	}

	/**
	 * Constructor of a rectangular button.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param nx		float, the x-coordinate of the location of the button
	 * @param ny		float, the y-coordinate of the location of the button
	 * @param nw		float, the width of the button
	 * @param nh		float, the height of the button
	 * @param ncolors	color [], the colors of the button (0-outside, 1-overlay (for clicked status), 2-inside)
	 * @param ntext		String, the label for the button
	 */
	public RectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext) {
		wid = 20 / w;
		hei = 20 / h;
		this.x = nx;
		this.y = ny;
		this.w = nw;
		this.h = nh;
		this.p = p;
		this.g = p.g;
		if (w * wrat() < 26 * wrat() && h * hrat() < 26 * hrat()) {
			wid = 9/w;
			hei = 9/w;
			PApplet.println(ntext);
		}
		scheme = ncolors;
		buttonText = ntext;
		font = p.createFont("Baskerville-Italic", 200, true);
		fsize = PApplet.floor((float) (nh - 20));

		init();
	}
	
	/**
	 * Constructor of a rectangular button.
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param nx		float, the x-coordinate of the location of the button
	 * @param ny		float, the y-coordinate of the location of the button
	 * @param nw		float, the width of the button
	 * @param nh		float, the height of the button
	 * @param ncolors	color [], the colors of the button (0-outside, 1-overlay (for clicked status), 2-inside)
	 * @param ntext		String, the label for the button
	 * @param fontName	String, the name of the font needed for the label (must be compatible with createFont, use PFont.list() if you're not sure)
	 * @param fontSize  int, the size of the font for the label
	 */
	public RectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext, String fontName, int fontSize) {
		wid = 20 / w;
		hei = 20 / h;
		this.x = nx;
		this.y = ny;
		this.w = nw;
		this.h = nh;
		this.p = p;
		this.g = p.g;
		if (w * wrat() < 26 * wrat() && h * hrat() < 26 * hrat()) {
			wid = 9/w;
			hei = 9/w;
			PApplet.println(ntext);
		}
		scheme = ncolors;
		buttonText = ntext;
		font = p.createFont(fontName, 200, true);
		fsize = fontSize;

		init();
	}

	/**
	 * Constructor of the button with room for specification of the font
	 * @param p			PApplet, the parent of the sketch, usually "this"
	 * @param nx		float, the x-coordinate of the location of the button
	 * @param ny		float, the y-coordinate of the location of the button
	 * @param nw		float, the width of the button
	 * @param nh		float, the height of the button
	 * @param ncolors	color [], the colors of the button (0-outside, 1-overlay (for clicked status), 2-inside)
	 * @param ntext		String, the label for the button
	 * @param nfont		PFont, the font of the button's label
	 * @param nfsize	int, the size of the font
	 */
	public RectButton(PApplet p, float nx, float ny, float nw, float nh, int [] ncolors, String ntext, PFont nfont, int nfsize) {
		wid = 20 / w;
		hei = 20 / h;
		this.x = nx;
		this.y = ny;
		this.w = nw;
		this.h = nh;
		this.p = p;
		this.g = p.g;
		if (w * wrat() < 26 * wrat() && h * hrat() < 26 * hrat()) {
			wid = 9/w;
			hei = 9/w;
			PApplet.println(ntext);
		}
		scheme = ncolors;
		buttonText = ntext;
		font = nfont;
		fsize = nfsize;

		init();
	}

	protected int [] standardColors() {
		int [] stdColors = {g.color(200, 240, 240), g.color(250, 250, 250), g.color(200, 200, 200, 190)};
		return(stdColors);
	}

	void change() {
		wratio = p.width / beginningWidth;
		hratio = p.height / beginningHeight;
	}
	void update() {
		change();
		//		if (buttonMessage != null) {
		//			buttonMessage.update();
		//		}
	}

	void display(String time, PFont displayFont, float fontsize) {
		back();
		g.textFont(displayFont, limitFont(displayFont, fontsize, time, w));
		g.textAlign(CENTER, CENTER);
		g.text(time, x(), y());
	}

	protected void back() {
		g.rectMode(CENTER);
		g.noStroke();
		g.fill(outside);
		backShape(x(), y(), w(), h());
//		float insideW, insideH;
//		insideW = w - 20 * wratio;
//		insideH = h - 20 * hratio;
		g.fill(inside);
		innerBackShape(x(), y(), iw(), ih());
		g.fill(0);
	}
	
	public void backShape(float xi, float yi, float wi, float hi) {
		g.rect(xi, yi, wi, hi);
	}
	
	public void innerBackShape(float xi, float yi, float wi, float hi) {
		backShape(xi, yi, wi, hi);
	}


	//MAJOR FUNCTIONS:
	public void draw() {
		/*Function to display the button
	     takes no arguments* bc all of the properties of the button are initialized/created by the initializer, new Button()
		 *other than mX and mY to allow the function rolled_over */
		rolled_over(p.mouseX, p.mouseY);

		

//		g.noStroke();
//		g.rectMode(CENTER);
//		if ((rollover)) {
//			g.fill(scheme[0]);
//			backShape(x(), y(), w(), h());
//			g.fill(scheme[1]);
//			backShape(x(), y(), w(), h());
//			g.fill(scheme[2]);
//			backShape(x(), y(), iw(), ih());
//		} else {
//			g.fill(scheme[0]);
//			backShape(x(), y(), w(), h());
//			g.fill(scheme[1]);
//			backShape(x(), y(), iw(), ih());
//		}
		back();
		display("", font, fsize);
		
		if (rollover) {
			g.pushStyle();
			g.fill(scheme[2]);
			g.noStroke();
			backShape(x(), y(), w(), h());
			g.popStyle();
		}
		
		drawButtonName();
	}

	void drawButtonName() {
		g.pushStyle();
		g.textFont(font, limitFont(fsize, buttonText, w));
		g.textAlign(CENTER, CENTER);
		g.fill(0);
		g.text(buttonText, x(), y());
		g.popStyle();

	}

	void rolled_over(float mX, float mY) {
		//for the buttons that do NOT display messages when the user clicks on them
		if (clicked()) {
			rollover = true;
		} else {
			rollover = false; //these buttons need to become false right when they are released
		}

	}



	@Override
	public boolean clicked(float mX, float mY) {
		//for the buttons that do NOT display messages when the user clicks on them
		return(clicked()); //these buttons need to become false right when they are released

	}

	//SUPPORTING FUNCTIONS:
	void changeColors(int [] newColors) {
		scheme = newColors;
	}

	public boolean clicked() {
		return(xmin() < p.mouseX && xmax() > p.mouseX && ymin() < p.mouseY && ymax() > p.mouseY && p.mousePressed);
	}

	boolean state() {
		int state = counting % 2;
		if (state == 0) {
			return false;
		} else if (state == 1) {
			return true;
		} else {
			return false;
		}
	}

	//	void createMessage(String tempHeader, String tempMessage, String tempYesOpt, String tempNoOpt, color [] scheme, color [] buttonColors) {
	//		buttonMessage = new Message(tempHeader, tempMessage, tempYesOpt, tempNoOpt, scheme, buttonColors);
	//	}

	boolean isRolledOver() {
		rolled_over(p.mouseX, p.mouseY);
		return rollover;
	}

	void increaseCount() {
		counter ++;
	}

	boolean returnState() {
		if (counter % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}

	float wrat() {
		return(p.width / 600);
	}

	float hrat() {
		return(p.height/ 400);
	}

	float ohsix() {
		return(w * wid);
	}

	float ohthree() {
		return(h * hei);
	}

	@Override
	public float y() {
		return(y * hratio);
	}

	@Override
	public float h() {
		return(h * hratio);
	}

	@Override
	public float w() {
		return(w * wratio);
	}

	@Override
	public float x() {
		return(x * wratio);
	}

	float iw() {
		int n = 20;
		if (w() < 1.5 * n * wratio) {
			n = PApplet.floor((float) (w() * .2));
		}
		return(w() - n);
	}

	float ih() {
		int n = 20;
		if (h() < 1.5 * n * hratio) {
			n = PApplet.floor((float) (h() * .2));
		}
		return(h() - n);
	}

	public float reY() {
		return y;
	}

	public float reX() {
		return x;
	}

	public float reH() {
		return h;
	}

	public float reW() {
		return w;
	}

	public float xmin() {
		return(x() - (w() / 2));
	}

	public float xmax() {
		return(x() + (w() / 2));
	}

	public float ymin() {
		return(y() - (h() / 2));
	}

	public float ymax() {
		return(y() + (h() / 2));
	}

	float limitFont(float fontSize, String str, float w) {
		//		  float hrat = (float) (p.height / 400.0);
		//		  float wrat = (float) (p.width / 600.0);
		//		  g.textFont(myfont1, fontSize * hrat);
		//		  float twidth = g.textWidth(str);
		//		  if (twidth < iw()) {
		//			  return(fontSize * hrat);
		//		  } else if (fontSize * hrat > ih()) {
		//			  return(PApplet.abs((float) (fontSize * hrat * ((ih() / 1.1) / (fontSize * hrat)))));
		//		  }
		//		  return(PApplet.abs((float) (fontSize * hrat * ((iw() / 1.1) / twidth))));
		return(fontSize);
	}

	float limitFont(PFont font, float fontSize, String str, float w) {
		//		  float hrat = (float) (p.height / 400.0);
		//		  float wrat = (float) (p.width / 600.0);
		//		  g.textFont(font, fontSize * hrat);
		//		  float twidth = g.textWidth(str);
		//		  if (twidth < iw()) {
		//			  return(fontSize * hrat);
		//		  }
		//		  return(PApplet.abs((float) (fontSize * hrat * (iw() / 1.1) / twidth)));
		return(fontSize);
	}
	
	void init() {
		//fonts
		minifont = p.createFont("Menlo-regular", 20);
		smallfont = p.createFont("FZLTXHK--GBK1-0", 200, true);
		sFont = p.createFont("FZLTXHK--GBK1-0", 15, true);
		mFont = p.createFont("FZLTXHK--GBK1-0", 19, true);
		myfont1 = p.createFont("FZLTXHK--GBK1-0", 20, true);
		cGrey15 = g.color(250, 250, 250);
		inside = scheme[1];
		outside = scheme[0];
		sfsize = 15;
		mfsize = 19;

		beginningWidth = p.width;
		beginningHeight = p.height;
	}
	
	@Override
	public String returnName() {
		return("RectButton");
	}
}
