package funGUI;

/**
 * The direction enum holds the information for the placement
 * of certain objects, such as the position of a button vs its label.
 * @author Audrey Seo
 */
public enum Direction {
	LEFT(-1, 0), UP(0, -1), DOWN(0, 1), RIGHT(1, 0);
	int x;
	int y;
	
	/**
	 * The getX() function retrieves the displacement direction for x.
	 * @return the displacement value for the x-direction
	 */
	public int getX() {
		return (x);
	}
	
	/**
	 * The getY() function retrieves the displacement direction for y.
	 * @return the displacement value for the 
	 */
	public int getY() {
		return (y);
	}

	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
