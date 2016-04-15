package funGUI;
import processing.core.*;

/**
 * The Label class is a version of a number output
 * that represents certain aspects. That way, it can
 * show the value of anything you want it to represent.
 * @author audrey
 *
 */
public class Label extends NumOutput {
	public float y0, x0;
	public int c;
	boolean circle;
	
	/**
	 * Constructor
	 * @param p					PApplet, the parent of the sketch (usually "this")
	 * @param x					float, the x-coordinate of the label's center
	 * @param y					float, the y-coordinate of the label's center
	 * @param w					float, the width of the label
	 * @param h					float, the height of the label
	 * @param initialValue		float, the first value to show on the label
	 * @param x0				float, the x-coordinate of the location that the label points to
	 * @param y0				float, the y-coordinate of the location that the label points to
	 */
	public Label(PApplet p, float x, float y, float w, float h, float initialValue, float x0, float y0) {
		super(p, x, y, w, h, initialValue);
		this.y0 = y0;
		this.x0 = x0;
		c = g.color(255);
	}
	
	/**
	 * Set the color of the line
	 * @param n		color, the ARGB value that dictates the color of line of the label's pointer
	 */
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
