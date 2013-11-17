package polargraph;

import geomerative.RPoint;
import geomerative.RShape;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.UUID;

import polargraph.queue.QueueWriter;

public class PolargraphDrawing {
	private Polargraph parent;
	private Rectangle2D.Float extent;
	private String name;
	private QueueWriter queueWriter;
	private RPoint offsetFromParent = null;

	public PolargraphDrawing(String name, Rectangle2D.Float extent, Polargraph parent) {
		this.setName(name);
		this.extent = extent;
		this.parent = parent;
	}
	
	public RPoint getOffset() {
		if (this.offsetFromParent == null) 
			this.offsetFromParent = new RPoint(this.extent.x, this.extent.y);
		processing.core.PApplet.println("offset: " + this.offsetFromParent.toString());
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
	
	public void addQueueWriter(QueueWriter queueWriter) {
		this.queueWriter = queueWriter;
	}
	
	
	public RShape getAsCartesian(RShape shape) {
		RShape sh = new RShape(shape);
		sh.translate(this.getOffset());
		return sh;
	}
	public RShape getAsNative(RShape shape) {
		RShape sh = this.getAsCartesian(shape);
		sh = this.parent.getAsNative(shape);
		return sh;
	}
}
