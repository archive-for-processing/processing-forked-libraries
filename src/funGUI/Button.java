package funGUI;

import processing.core.*;

/**
 * The Button class serves as the template for all button-type
 * objects. Events such as mouseClick and draw are handled here.
 * However, this class is abstract, and thus never implemented.
 * If you want a button, try ToggleButton, RectButton, etc. instead.
 * @author Audrey Seo
 *
 */
public abstract class Button extends Frame implements PConstants {
	public String label;
	public float w2, h2;
	
	/**
	 * The constructor of this button.
	 */
	public Button() {
		// nothing here
	}
	
	/**
	 * Runs all the shape functions.
	 */
	@Override
	protected void display() {
		// Put any shape functions in here
	}
	
	/**
	 * Runs all of the style functions.
	 */
	@Override
	protected void style() {
		// Put any style-related functions in here
	}
	
	/**
	 * Runs all of the functions related to text.
	 */
	@Override
	protected void text() {
		// Put any text calls in here
	}
	
	/**
	 * Draws the object to the screen.
	 */
	@Override
	public void draw() {
		// Runs all of the shape-drawing functions
		g.pushStyle();
		style();
		display();
		text();
		g.popStyle();
	}
	
	/**
	 * Assigns a color for the button.
	 * @param n		color, the background of the button
	 */
	public void color(int n) {
		c = new int[1];
		c[0] = n;
	}
	
	/**
	 * Assigns several different colors different parts of the button.
	 * Assign as many colors as you need. The maximum is 5.
	 * Here is how they are allotted to different purposes: inner fill,
	 * inner stroke, outer fill, outer stroke, font fill.
	 * @param n		color [], an array of the color values, just pass in individually.
	 */
	public void color(int... n) {
		// The color array is in the following format:
		// inner fill, inner stroke, outer fill, outer stroke, font fill
		c = new int[n.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = n[i];
		}
	}

	/**
	 * The boolean function that returns whether or not the mouse is pressed while over the button.
	 * @return
	 */
	public boolean clicked() {
		return (p.mousePressed && p.mouseX < x + w * .5
				&& p.mouseX > x - w * .5 && p.mouseY < y + h * .5 && p.mouseY > y
				- h * .5);
	}

	/**
	 * Finds whether or not the button is clicked when the button is moved by a certain value.
	 * @param dx	float, the value to displace the button by on the x-axis
	 * @param dy	float, the value to displace the button by on the y-axis
	 * @return
	 */
	public boolean clicked(float dx, float dy) {
		// p.println("MX1: " + (x + dx + w * .5));
		// p.println("MX2: " + (x + dx - w * .5));
		// p.println("MY1: " + (y + dy + h * .5));
		// p.println("MY2: " + (y + dy - h * .5));
		// p.println("MP:  " + p.mousePressed);
		return (p.mousePressed && (p.mouseX < x + dx + w * .5)
				&& (p.mouseX > x + dx - w * .5) && (p.mouseY < y + dy + h * .5) && (p.mouseY > y
				+ dy - h * .5));
	}
}
