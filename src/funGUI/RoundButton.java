package funGUI;
import processing.core.*;

public class RoundButton extends Button {
	public float r;
	public float r0 = 0;
	public float ri = 0;
	public char c = ' ';
	protected PFont font;
	
	public RoundButton(PApplet p, float x, float y, float r, char c){
		this.x = x;
		this.y = y;
		this.p = p;
		this.g = p.g;
		this.r = r;
		this.r0 = this.r;
		this.ri = this.r * .25f;
		font = p.createFont(REG_SANSS_TXT, REGTXTSIZE);
		this.c = c;
	}
	
	public RoundButton(PApplet p, float x, float y, float r){
		this.x = x;
		this.y = y;
		this.p = p;
		this.g = p.g;
		this.r = r;
		this.r0 = this.r;
		this.ri = this.r * .25f;
		font = p.createFont(REG_SANSS_TXT, REGTXTSIZE);
	}
	
	@Override
	public void display() {
		g.pushStyle();
		g.noStroke();
		g.fill(220);
		g.ellipse(x, y, r * 2, r * 2);
		if (clicked()) {
			g.fill(240, 240, 240);
		} else {
			g.fill(255, 255, 255);
		}
		g.ellipse(x, y, 2 * r - PApplet.log((r / r0) * 2.718f) * ri, 2 * r - PApplet.log((r / r0) * 2.718f) * ri);
		g.popStyle();
	}
	
	@Override
	public void text() {
		g.pushStyle();
		g.fill(30);
		g.textFont(font, (r / r0) * REGTXTSIZE);
		g.textAlign(CENTER, CENTER);
		g.text(c, x, y);
		g.popStyle();
	}
	
	@Override
	public float w() {
		return(r());
	}
	
	@Override
	public float h() {
		return(r());
	}
	
	public float r() {
		return(r);
	}
	
	@Override
	public boolean clicked() {
		return(p.mousePressed && PApplet.dist(p.mouseX, p.mouseY, this.x, this.y) <= r);
	}
	
	@Override
	public String returnName() {
		return("RoundButton");
	}
}
