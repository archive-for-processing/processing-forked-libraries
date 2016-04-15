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
		g.pushStyle();
		g.fill(255);
		g.stroke(0);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
		g.fill(30);
		g.textFont(font);
		g.textAlign(LEFT, CENTER);
		g.text(val, x - (w * .45f), y);
		g.popStyle();
//		PApplet.println("done displaying");
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
