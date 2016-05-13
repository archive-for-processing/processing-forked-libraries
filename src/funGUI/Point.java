package funGUI;
import processing.core.*;

/**
 * A class for storing a single pair of
 * x and y coordinates. I find that I make
 * enough of these classes on a regular basis
 * already that I just decided to include one
 * in this library, so that I could be all set,
 * once and for all. Included are some additional
 * functions to make both your life and mine better.
 * @author audrey
 *
 */
public class Point {
	public float x;
	public float y;
	
	/**
	 * Constructor of the Point object
	 * @param x		float, the horizontal position of this point
	 * @param y		float, the vertical position of this point
	 */
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gives the distance from one Point object to another
	 * @param p		Point, the other point object
	 * @return		float, the distance between a given point and another point, p
	 */
	public float dist(Point p) {
		return (float) (Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2)));
	}
	
	/**
	 * Finds the distance between any two points.
	 * @param p1	Point, the first of the two points
	 * @param p2	Point, the second of the two points
	 * @return		float, the distance between the Points p1 and p2
	 */
	public static float dist(Point p1, Point p2) {
		return( ( float ) ( Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) ) );
	}
	
	/**
	 * Finds the angle for any point in between two other points.
	 * @param beginning		Point, the first point
	 * @param middle		Point, the second point (where the angle is being measured)
	 * @param end			Point, the last point
	 * @return		float, the angle in radians of the angular distance between the vectors represented by these points
	 */
	public static float angle(Point beginning, Point middle, Point end) {
		PVector vector = new PVector(beginning.x - middle.x, beginning.y - middle.y);
		PVector vector2 = new PVector(end.x - middle.x, end.y - middle.y);
		
		return( (float) Math.acos(PVector.dot(vector, vector2) / (vector.mag() * vector.mag())) );
	}
	
	/**
	 * Retrieves and returns the x-coordinate of this point
	 * @return		float
	 */
	public float x() {
		return(x);
	}
	
	/**
	 * Retrieves and returns the y-coordinate of this point
	 * @return		float
	 */
	public float y() {
		return(y);
	}
}
