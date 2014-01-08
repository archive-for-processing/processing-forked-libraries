package polargraph;

import geomerative.RPoint;
import geomerative.RShape;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import polargraph.PolargraphDrawing;
import polargraph.queue.QueueWriter;
import polargraph.queue.VirtualComQueueWriter;
import processing.core.PVector;
import processing.serial.Serial;

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
	private float nativeUnitSize = 1.0f;
	
	// home position
	private RPoint homePosition = new RPoint(0, 0);
	
	// collection of registered drawings
	private Map<String, PolargraphDrawing> drawings = new HashMap<String, PolargraphDrawing>();
	private RPoint topRight = null;
	
	// hardware
	public static final Integer HARDWARE_VER_UNO = 1;
	public static final Integer HARDWARE_VER_MEGA = 100;
	public static final Integer HARDWARE_VER_MEGA_POLARSHIELD = 200;
	private Integer currentHardware = HARDWARE_VER_MEGA_POLARSHIELD;	
	
	// Current pen position
	private PolargraphTool tool = new PolargraphTool();
	
	/**
	 * Root constructor that takes sprocket/motor definition to calculate nativeUnitSize.
	 * @param width
	 * @param height
	 * @param reachableArea
	 * @param unitPerRev
	 * @param stepsPerRev
	 */
	public Polargraph(float width, float height, RectangularShape reachableArea, Float unitPerRev, Integer stepsPerRev) {
		this(width, height, reachableArea, unitPerRev / stepsPerRev.floatValue());
	}

	/**
	 * Constructor that accepts the nativeUnitSize directly.
	 *  
	 * @param width
	 * @param height
	 * @param reachableArea
	 * @param nativeUnitSize
	 */
	public Polargraph(float width, float height, RectangularShape reachableArea, Float nativeUnitSize) {
		if (width > 0 && height > 0)
			this.extent = new Rectangle2D.Float(ORIGIN.x,ORIGIN.y,width,height);
		this.reachableArea = new Rectangle2D.Float((float)reachableArea.getX(), 
				(float)reachableArea.getY(), 
				(float)reachableArea.getWidth(), 
				(float)reachableArea.getHeight());
		if (! this.extent.contains(this.reachableArea))
			throw new RuntimeException("Reachable area is not actually inside the machine extent.");
		
		this.nativeUnitSize = nativeUnitSize;
		this.addTool(this.tool);
	}
	
	
	public void addTool(PolargraphTool tool) {
		this.tool = tool;
		this.tool.setParentMachine(this);
	}
	public PolargraphTool getTool() {
		return this.tool;
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
		return this.createNewDrawing(name,  drawingExtent, drawingExtent);
	}
	public PolargraphDrawing createNewDrawing(String name, RectangularShape drawingExtent, RectangularShape inputShape) {
		
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
	 * Converts a PVector cartesian coordinate to a triangular coordinate PVector.
	 * It is a wrapper around convertToNative(RPoint).
	 *  
	 * @param p
	 * @return
	 */
	public PVector convertToNative(PVector p) {
		RPoint rp = this.convertToNative(new RPoint(p.x, p.y));
		return new PVector(rp.x, rp.y);
	}
	
	/**
	 * Converts cartesian coordinates to triangular coordinates, and changes the unit size.
	 * @param p
	 * @return
	 */
	public RPoint convertToNative(RPoint p) {
		p = this.convertToNativeCoordinates(p);
		p = this.convertToNativeUnitSize(p);
		return p;
	}
	
	/**
	 * Takes a coordinate in the native triangular system and converts to cartesian values.
	 *  
	 * @param p
	 * @return
	 */
	public RPoint convertToCartesian(RPoint p) {
		p = this.convertToCartesianCoordinates(p);
		p = this.convertToCartesianUnitSize(p);
		return p;
	}
	
	/**
	 *  Converts cartesian coordinates to triangular coordinates, but does NOT change
	 *  the unit size.
	 *  
	 * @param p the RPoint to convert to
	 * @return the same object as was passed in, but with changed values
	 */
	public RPoint convertToNativeCoordinates(RPoint p) {
		RPoint newP = new RPoint(p);
		newP.x = p.dist(ORIGIN);
		newP.y = p.dist(this.getTopRight());
		return newP;
	}
	
	/**
	 * Converts triangular machine coordinates to cartesian screen coords.
	 * 
	 * @param p
	 * @return
	 */
	public RPoint convertToCartesianCoordinates(RPoint p) {
	    double calcX = (pow(this.getExtent().getWidth(), 2) - pow(p.y, 2) + pow(p.x, 2)) / (this.getExtent().getWidth()*2);
	    double calcY = sqrt(pow(p.x,2)-pow(calcX,2));
	    RPoint rp = new RPoint(calcX, calcY);
	    return rp;
	}
	
	private RPoint getTopRight() {
		if (this.topRight  == null)
			this.topRight = new RPoint(this.getExtent().getWidth(), this.getExtent().getY());
		return this.topRight;
	}

	/** Simply converts natural units to native units, ie multiplies by nativeUnitSize.
	 *  Note it converts in-place - does not create and return a new object.
	 * 
	 * @param p the RPoint to convert
	 * @return same point object, with values changed.
	 */
	public RPoint convertToNativeUnitSize(RPoint p) {
		p.x = p.x * this.nativeUnitSize;
		p.y = p.y * this.nativeUnitSize;
		return p;
	}
	
	/**
	 * Converts a number of native measurement units into a cartesian (natural)
	 * units.
	 * 
	 * @param p
	 * @return
	 */
	public RPoint convertToCartesianUnitSize(RPoint p) {
		p.x = p.x / this.nativeUnitSize;
		p.y = p.y / this.nativeUnitSize;
		return p;
	}
	
	/**
	 * Returns a rectangular shape that is the entirety of the machine, including non-drawable areas.
	 * 
	 * @return RectangularShape
	 */
	public RectangularShape getExtent() {
		return this.extent;
	}
	/**
	 * Returns a rectanglular shape that designated the area that the machine can actually draw to.
	 * In principle this will be the full size of the machine, less the margins. It is designed to 
	 * isolate areas that the machine can theoretically move to (because they are on the board), but
	 * lacks the power to actually be controllable in. The top margin is the main danger area.
	 * 
	 * @return RectangularShape.
	 */
	public RectangularShape getReachableArea() {
		return this.reachableArea;
	}
	


	/**
	 * Sets the hardware version to a new one.
	 * @param newHardwareVersion
	 */
	public void setHardwareVersion(Integer newHardwareVersion) {
		// now see if it's different to last time.
		if (newHardwareVersion != currentHardware)
		{
			// and make the controller reflect the new hardware.
			this.currentHardware = newHardwareVersion;
		}
	}

}
