package funGUI;

public class Display extends Frame {
	protected String text, headline;
	
	
	/**
	 * Constructor for the Display class. Rarely used due to the absence of most implementation in the Display class.
	 * @param x		float, the x-coordinate of the display's center
	 * @param y		float, the y-coordinate of the display's center
	 * @param w		float, the width of the display
	 * @param h		float, the height of the display
	 */
	public Display(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	protected void display() {
		// Put any shape functions in here
	}

	@Override
	protected void style() {
		// Put any style-related functions in here
	}

	@Override
	protected void text() {
		// Put any text calls in here
	}
	
	protected void textStyle() {
		
	}
	
	/**
	 * Displays the shape, text, etc. of the display.
	 */
	@Override
	public void draw() {
		// Runs all of the shape-drawing functions
		g.pushStyle();
		style();
		display();
		g.popStyle();
		g.pushStyle();
		text();
		g.popStyle();
	}
	
	public String returnName() {
		return("Display");
	}

}
