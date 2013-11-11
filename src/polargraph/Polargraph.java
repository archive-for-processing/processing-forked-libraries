package polargraph;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import processing.core.PVector;

public class Polargraph {
	
	// basic machine extent.  Origin will always be 0, 0.
	private Rectangle2D.Float extent = null;
	
	// reachable area is the full, addressable space that the drawing tool can be moved to.
	private Rectangle2D.Float reachableArea = null;
	
	// units and step size
	public final String UNIT_MM = "mm";
	private double stepsPerUnit = 1;
	private String unit = UNIT_MM;
	
	// home position
	private PVector homePosition = new PVector(0, 0);
	
	// collection of registered drawings
	private Map<String, PolargraphDrawing> drawings = new HashMap<String, PolargraphDrawing>();

	
	/*
	 * Root constructor.
	 */
	public Polargraph(float width, float height, RectangularShape reachableArea, double stepsPerUnit) {
		if (width > 0 && height > 0)
			this.extent = new Rectangle2D.Float(0,0,width,height);
		this.reachableArea = new Rectangle2D.Float((float)reachableArea.getX(), 
				(float)reachableArea.getY(), 
				(float)reachableArea.getWidth(), 
				(float)reachableArea.getHeight());
		if (! this.extent.contains(this.reachableArea))
			throw new RuntimeException("Reachable area is not actually inside the machine extent.");
		
		this.stepsPerUnit = stepsPerUnit;
	}
	
	
	/**
	 * Sets a the home position of the drawing tool.
	 * 
	 */
	public void setHomePosition(PVector homePosition) {
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
}
