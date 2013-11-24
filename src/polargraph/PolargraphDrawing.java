package polargraph;

import geomerative.RPoint;
import geomerative.RShape;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.geom.RectangularShape;
import java.util.UUID;

import polargraph.queue.QueueWriter;

public class PolargraphDrawing {
	private Polargraph parent;
	private Rectangle2D.Float extent;
	private String name;
	private RPoint offsetFromParent = null;
	private RectangularShape mappingShape = null;
	private float scaleToMap = 1.0f;

	public PolargraphDrawing(String name, Rectangle2D.Float extent, Polargraph parent) {
		this(name, extent, parent, extent);
	}
	public PolargraphDrawing(String name, Rectangle2D.Float extent, Polargraph parent, RectangularShape inputShape) {
		this.setName(name);
		this.extent = extent;
		this.parent = parent;
		this.mappingShape = extent;
		this.setInputScaling(inputShape);
	}
	
	public RPoint getOffset() {
		if (this.offsetFromParent == null) 
			this.offsetFromParent = new RPoint(this.extent.x, this.extent.y);
		return this.offsetFromParent;
	}
	
	public void setName(String name) {
		if ("".equals(name) || name == null) 
			name = UUID.randomUUID().toString();
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public RPoint getAsCartesian(RPoint point) {
		RPoint p = new RPoint(point);
		p.scale(scaleToMap);
		p.translate(this.getOffset());
		return p;
	}
	public RShape getAsCartesian(RShape shape) {
		RShape sh = new RShape(shape);
		sh.scale(scaleToMap);
		sh.translate(this.getOffset());
		return sh;
	}
	public RShape getAsNative(RShape shape) {
		RShape sh = this.getAsCartesian(shape);
		sh = this.parent.getAsNative(shape);
		return sh;
	}
	
	/**
	 * Method that takes an rectangle and maps it onto the drawing, in order to produce
	 * a scaling factor.  This allows us to scale input points to actual points on the machine.
	 * 
	 * @param inputShape
	 */
	public void setInputScaling(RectangularShape inputShape) {
		this.mappingShape  = inputShape;
		float widthToHeight = this.extent.width / this.extent.height;
		float mappingWidthToHeight = (float) mappingShape.getWidth() / (float) mappingShape.getHeight();
		
		if (widthToHeight <= mappingWidthToHeight) {
			//  then scale based on the mapping WIDTH
			scaleToMap = this.extent.width / (float) mappingShape.getWidth();
		}
		else {
			scaleToMap = this.extent.height / (float) mappingShape.getHeight();
		}
	}
	public RectangularShape getExtent() {
		return this.extent;
	}
}
