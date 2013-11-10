package polargraph;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class Polargraph {
	
	// Possible units
	public final String UNIT_MM = "mm";
	
	private float width = 0;
	private float height = 0;
	private Rectangle2D.Float drawableArea = null;
	private double stepsPerUnit = 1;
	private String unit = UNIT_MM;
	

	Polargraph(float width, float height, RectangularShape drawableArea, double stepsPerUnit) {
		this.width = width;
		this.height = height;
		this.drawableArea = new Rectangle2D.Float((float)drawableArea.getX(), 
				(float)drawableArea.getY(), 
				(float)drawableArea.getWidth(), 
				(float)drawableArea.getHeight());
		this.stepsPerUnit = stepsPerUnit;
	}
}
