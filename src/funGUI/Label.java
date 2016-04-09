package funGUI;
import processing.core.*;

public class Label extends NumOutput {
	public float y0, x0;
	public int c;
	boolean circle;
	
	public Label(PApplet p, float x, float y, float w, float h, float initialValue, float x0, float y0) {
		super(p, x, y, w, h, initialValue);
		this.y0 = y0;
		this.x0 = x0;
		c = g.color(255);
	}
	
	public void colorLine(int n) {
		c = n;
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
		g.text(val, x - (w * .45f), y);
		
		g.stroke(c);
		float offsetX = (x0 < x) ? x - w * .5f : x + w * .5f;
		g.line(offsetX, y, x0, y0);
		g.popStyle();
	}
}
