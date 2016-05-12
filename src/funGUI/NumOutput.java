package funGUI;
import processing.core.*;

public class NumOutput extends Frame {
	float val = 0.0f;
	PFont font;
	String label;
	Slider s;
	
	
	public NumOutput(PApplet p, float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.p = p;
		this.g = p.g;
		val = 0;
//		PApplet.println("finished init");
		font = this.p.createFont("Times-New-Roman", (float) (h * .8), true);
	}
	
	public NumOutput(PApplet p, float x, float y, float w, float h, float initialValue) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.p = p;
		this.g = p.g;
		val = initialValue;
//		PApplet.println("finished init");
		font = this.p.createFont("Times-New-Roman", (float) (h * .8), true);
	}
	
	public NumOutput(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
//		PApplet.println("finished init");
	}
	
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
//	public void draw(float text) {
//		g.pushStyle();
//		g.fill(255);
//		g.stroke(0);
//		g.rectMode(CENTER);
//		g.rect(x, y, w, h);
//		g.fill(30);
//		g.textFont(font);
//		g.textAlign(LEFT, CENTER);
//		g.text(text, x - (w * .45f), y);
//		g.popStyle();
//	}
	
	@Override
	protected void display() {
//		PApplet.println("displayed");
		g.textFont(font);
		float x0 = x() + xOffset();
		float y0 = yCenter();
		g.pushStyle();
		g.fill(255);
		g.stroke(0);
		g.rectMode(CENTER);
		g.rect(x0, y0, w, h);
		g.fill(30);
		g.textFont(font);
		g.textAlign(LEFT, CENTER);
		g.text(val, xLeft(), y0);
		g.popStyle();
//		PApplet.println("done displaying");
	}
	
	protected float xOffset() {
		g.textFont(font);
		return((label != null) ? (w + g.textWidth(label) + 30) * .25f : 0);
	}
	protected float yCenter() {
		return(y);
	}
	
	protected float xLeft() {
		return(x - (w * .45f) + xOffset());
	}
	
	@Override
	protected void text() {
		if (label != null) {
			g.pushStyle();
			g.textFont(font);
			g.textAlign(CENTER, CENTER);
			g.fill(30);
			g.text(label, x() - xOffset(), y());
			g.popStyle();
		}
	}
	
	float val() {
		if (val == 0 && s != null) {
			return(s.p());
		} else {
			return(val);
		}
	}
	
	public void assign(float f) {
		val = f;
	}
	
	@Override
	public String returnName() {
		return("NumOutput");
	}
}
