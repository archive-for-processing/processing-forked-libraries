package polargraph;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.UUID;

import queue.QueueWriter;

public class PolargraphDrawing {
	private Polargraph parent;
	private Float extent;
	private String name;
	private QueueWriter queueWriter;

	public PolargraphDrawing(String name, Rectangle2D.Float extent, Polargraph parent) {
		this.setName(name);
		this.extent = extent;
		this.parent = parent;
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

}
