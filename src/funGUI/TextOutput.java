package funGUI;

import processing.core.*;

public class TextOutput extends TextDisplay {
	PFont font;

	public TextOutput(PApplet p, float x, float y, float w, float h, String txt,
			String header) {
		super(x, y, w, h, txt, header);
		this.p = p;
		this.g = p.g;
		font = p.createFont(REG_SANSS_TXT, REGTXTSIZE);
	}
	
	@Override
	protected void display() {
		g.fill(255);
		g.stroke(30);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
	}

	@Override
	protected void text() {
		g.textAlign(LEFT, CENTER);
		g.textFont(font);
		g.fill(0);
		g.text(text, (float) (x - w * .45), y);

	}
	
	public void assignText(String newText) {
		text = newText;
	}

}
