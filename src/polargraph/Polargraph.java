package polargraph;

import geomerative.RPoint;
import geomerative.RShape;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import processing.core.PVector;

/**
 * This is the most generic class, simulates a Polargraph machine.  It has an extent and a
 * reachable area (a section within the extent that can be safely drawn to).  The units
 * the machine is measured in is not stated, but would normally be mm.
 * 
 * It also has a native unit size - this is the pitch of the finest reliable grid that
 * the machine can address.  In most cases this is related to the sprocket (or pulley)
 * circumference, divided by the number of motor steps per revolution.
 * 
 * For a PolargraphSD this will be 92mm / 400 =  0.23mm
 * 
 * 
 * 
 * @author sandy_000
 *
 */
public class Polargraph {
	
	// basic machine extent.
	public static final RPoint ORIGIN = new RPoint(0,0);
	private Rectangle2D.Float extent = null;
	
	// reachable area is the full, addressable space that the drawing tool can be moved to.
	private Rectangle2D.Float reachableArea = null;
	
	// native unit size is the size of 1 "step" in comparison to the measurement unit.
	// eg the machine size is speced in mm, one machine motor step extends 0.23mm of cord, 
	// so the native unit size is 0.23.  Hooray!
	// This figure is actually a result of sprocket circumference / number of steps per rev.
	private double nativeUnitSize = 1.0;
	
	// home position
	private RPoint homePosition = new RPoint(0, 0);
	
	// collection of registered drawings
	private Map<String, PolargraphDrawing> drawings = new HashMap<String, PolargraphDrawing>();

	
	/**
	 * Root constructor that takes sprocket/motor definition to calculate nativeUnitSize.
	 * @param width
	 * @param height
	 * @param reachableArea
	 * @param unitPerRev
	 * @param stepsPerRev
	 */
	public Polargraph(float width, float height, RectangularShape reachableArea, Double unitPerRev, Integer stepsPerRev) {
		this(width, height, reachableArea, unitPerRev / stepsPerRev.doubleValue());
	}

	/**
	 * Constructor that accepts the nativeUnitSize directly.
	 *  
	 * @param width
	 * @param height
	 * @param reachableArea
	 * @param nativeUnitSize
	 */
	public Polargraph(float width, float height, RectangularShape reachableArea, Double nativeUnitSize) {
		if (width > 0 && height > 0)
			this.extent = new Rectangle2D.Float(ORIGIN.x,ORIGIN.y,width,height);
		this.reachableArea = new Rectangle2D.Float((float)reachableArea.getX(), 
				(float)reachableArea.getY(), 
				(float)reachableArea.getWidth(), 
				(float)reachableArea.getHeight());
		if (! this.extent.contains(this.reachableArea))
			throw new RuntimeException("Reachable area is not actually inside the machine extent.");
		
		this.nativeUnitSize = nativeUnitSize;
	}
	
	
	/**
	 * Sets a the home position of the drawing tool.
	 * 
	 */
	public void setHomePosition(RPoint homePosition) {
		if (this.reachableArea.contains(homePosition.x, homePosition.y))
			this.homePosition = homePosition;
		else
			throw new RuntimeException("Home position is not in the reachable area");
	}
	
	/**
	 * This is a method that takes a rectangle as an input, and returns a reference to a sub-panel in this Polargraph
	 * machine.  It is for defining a "drawable area" that can be addresses in a cartesian or native fashion.
	 * 
	 * Drawings are kept for convenience in a local map, keyed by their names.
	 * 
	 * @param drawingExtent
	 * @return
	 */
	public PolargraphDrawing createNewDrawing(String name, RectangularShape drawingExtent) {
		
		Rectangle2D.Float drawingExtent2D = new Rectangle2D.Float((float)drawingExtent.getX(), (float)drawingExtent.getY(), (float)drawingExtent.getWidth(), (float)drawingExtent.getHeight());
		PolargraphDrawing drawing = new PolargraphDrawing(name, drawingExtent2D, this);
		drawings.put(drawing.getName(), drawing);
		return drawing;
	}

	/**
	 * Converts the points in a shape to their equivalents in the native coordinates system.  
	 * So that is a series of points, specified by their distance from points A and B,
	 * and measured in the native unit (ie motor steps rather than mm).
	 * 
	 * @param shape
	 * @return a new shape containing native coordinates
	 */
	public RShape getAsNative(RShape shape) {
		RShape rs = new RShape(shape);
		// first, convert each point the triangular coordinates
		int i = 0;
		for (RPoint rp : rs.getPoints()) {
			this.convertToNative(rp);
		}
		return rs;
	}
	
	/**
	 * Converts cartesian coordinates to triangular coordinates, and changes the unit size.
	 * @param p
	 * @return
	 */
	public RPoint convertToNative(RPoint p) {
		this.convertToNativeCoordinates(p);
		this.convertToNativeUnitSize(p);
		return p;
	}
	
	/**
	 *  Converts cartesian coordinates to triangular coordinates, but does NOT change
	 *  the unit size.  Note it converts in-place - it does not create a new object.
	 *  
	 * @param p the RPoint to convert to
	 * @return the same object as was passed in, but with changed values
	 */
	public RPoint convertToNativeCoordinates(RPoint p) {
		p.x = p.dist(ORIGIN);
		p.y = p.dist(new RPoint(this.extent.width, this.extent.height));
		return p;
	}
	
	/** Simply converts natural units to native units, ie multiplies by nativeUnitSize.
	 * Note it converts in-place - does not create and return a new object.
	 * 
	 * @param p the RPoint to convert
	 * @return same point object, with values changed.
	 */
	public RPoint convertToNativeUnitSize(RPoint p) {
		p.x = p.x * (float) this.nativeUnitSize;
		p.y = p.y * (float) this.nativeUnitSize;
		return p;
	}
}
