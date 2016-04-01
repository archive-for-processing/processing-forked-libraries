package funGUI;

import processing.core.*;
import processing.data.*;

/**
 * A window is what a display should be, except that a display is
 * rarely used as is. The window contains information and widgets,
 * and you can choose whether the widget is centered or left-
 * aligned or whatever you wish.
 * @author audrey
 *
 */
public class Window extends Display {
	float horizontalMargin = 2;
	float verticalMargin = 2;
	PFont font;
	PFont headerFont;
	StringList texts = new StringList();
	
	/**
	 * Constructor for the Window class.
	 * @param p						PApplet, the parent of the sketch, usually "this"
	 * @param x						float, x-coordinate of center of window
	 * @param y						float, y-coordinate of center of window
	 * @param w						float, width of the window
	 * @param h						float, height of the window
	 * @param horizontalMargin		float, basically the amount of area to keep clear in the x-direction along the edges
	 * @param verticalMargin		float, the amount of area to keep clear in the y-direction along the edges
	 */
	public Window(PApplet p, float x, float y, float w, float h, float horizontalMargin, float verticalMargin) {
		super(x, y, w, h);
		this.p = p;
		this.g = this.p.g;
		this.horizontalMargin = horizontalMargin;
		this.verticalMargin = verticalMargin;
		init();
	}
	
	/**
	 * Constructor for the Window class.
	 * @param p						PApplet, the parent of the sketch, usually "this"
	 * @param x						float, x-coordinate of center of window
	 * @param y						float, y-coordinate of center of window
	 * @param w						float, width of the window
	 * @param h						float, height of the window
	 */
	public Window(PApplet p, float x, float y, float w, float h) {
		super(x, y, w, h);
		this.p = p;
		this.g = this.p.g;
		init();
	}
	
	@Override
	protected void style() {
		g.stroke(30);
		g.fill(255);
	}
	
	@Override
	protected void display() {
		g.rect(x(), y(), w(), h());
	}
	
	@Override
	protected void text() {
		g.fill(30);
		g.textFont(font);
		g.textAlign(LEFT, TOP);
		if (text != null && texts.size() == 0) {
			g.text(text, x() - .5f * innerWidth(), y() - innerHeight() * .5f);
		} else {
//			PApplet.println("Used");
			for (int i = 0; i < texts.size(); i++) {
				g.text(texts.get(i), x() - innerWidth() * .5f, y() - innerHeight() * .5f + i * 20f);
//				PApplet.println(i + ":  " + texts.get(i));
			}
		}
		if (headline != null) {
			g.textFont(headerFont);
			g.text(headline, x(), y() - innerHeight() * .5f);
		}
		limitText();
	}
	
	protected void limitText() {
		if (text != null) {
			g.pushStyle();
			g.textFont(font);
			float tw = g.textWidth(text);
			if (tw > innerWidth()) {
				texts.clear();
				
				int i = 0;
				String [] txts = PApplet.split(text, " ");
				while (i < txts.length){ 
					String partial = txts[i];
					i++;
//					PApplet.println("i: " + i + " txts.lengh: " + txts.length);
					if (i < txts.length) {
						while (g.textWidth(partial + " " +  txts[i]) < innerWidth()) {
							
							partial = partial + " " + txts[i];
							i++;
//							PApplet.println("i: " + i);
							if (i >= txts.length) {
								break;
							}

						}
					}
					texts.append(partial);
//					PApplet.println(texts.size());
				}

			}
			g.popStyle();
		}
	}
	
	public void assignText(String s) {
		this.text = s;
	}
	
	public void assignHeadline(String head) {
		this.headline = head;
	}
	
	float innerWidth() {
		return(w - 2 * horizontalMargin);
	}
	
	float innerHeight() {
		return(h - 2 * verticalMargin);
	}
	
	void init() {
		font = p.createFont(REG_SANSS_TXT, REGTXTSIZE);
		headerFont = p.createFont(REG_SANSS_TXT, LGTXTSIZE);
		headline = null;
		text = null;
	}
}
