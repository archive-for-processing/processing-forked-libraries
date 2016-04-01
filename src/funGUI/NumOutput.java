package funGUI;
import processing.core.*;

public class NumOutput extends Frame implements PConstants {
	Slider s;
	float val = 0.0f;
	PFont font;
	
	
	public NumOutput(float x, float y, float w, float h, Slider slider) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.s = slider;
		this.p = slider.p;
		this.g = slider.g;
		PApplet.println("finished init");
		font = this.s.p.createFont("Times-New-Roman", (float) (h * .8), true);
	}
	
	@Override
	protected void display() {
		g.pushStyle();
		g.fill(255);
		g.stroke(0);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
		g.fill(30);
		g.textFont(font);
		g.textAlign(LEFT, CENTER);
		g.text(val(), x - (w * .45f), y);
		g.popStyle();
	}
	
	public float val() {
		return(s.p());
	}
	
	public String returnName() {
		return("NumOutput");
	}
}
