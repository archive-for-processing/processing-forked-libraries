package me.max.chartly.charts;

import java.util.Iterator;

import me.max.chartly.Chartly;
import me.max.chartly.DataUtils;
import me.max.chartly.Defaults;
import me.max.chartly.components.color.Looks;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.MissingInformationException;
import processing.core.PConstants;

/**
 * Represents a PieChart
 * Draws the data based off of arcs proportional to 
 * its values' sum
 * 
 * @author Max Johnson
 * @example Chart_Example
 */
public class PieChart implements Chart {
	
	private DataSet data;
	private float x,y,radius,max;
	private Looks looks;
	private String title;
	
	private final float TOP_TITLE = 7;
	private final float LABEL = 9;
	
	/**
	 * Creates a new piechart with the given radius.
	 * and an empty DataSet
	 * 
	 * @param radius Radius (in pixels)
	 */
	public PieChart(float radius) {
		if (!Chartly.hasApplet()) {
			 ExceptionWriter.write(MissingInformationException.noApplet());
			 return;
		}
		
		data = new DataSet();
		this.radius = radius;
		this.looks = Defaults.getLooks();
		this.title = null;
	}
	
	@Override
	public void draw(float x, float y) {		
		Chartly.cleaner.load();
		
		try {
			testComplete();
		} catch (MissingInformationException ex) {
			ExceptionWriter.write(ex);
			return;
		}
		
		Chartly.app.textFont(looks.getFont());
		
		this.x = x; 
		this.y = y;
		
		float r = PConstants.PI / 2; // Start at 90 degrees	
		Iterator<DataPair> it = data.getData().iterator();	
		
		
		Chartly.app.textSize((radius)/LABEL);
		while(it.hasNext()) {
			// Chart
			DataPair current = it.next();
			float percent = DataUtils.dataToPercent(max, current.value);
			float dr = DataUtils.percentToRadians(percent);
			
			Chartly.app.stroke(looks.getAxisColor());
			Chartly.app.fill(looks.next());
			
			Chartly.app.arc(x, y, radius * 2, radius * 2, r, r+dr);
			
			// Labeling
			double tx = (1.1 * radius) * Math.cos(r + dr/2); 
			double ty = (1.1 * radius) * Math.sin(r + dr/2);
			
			Chartly.app.pushMatrix();
			Chartly.app.translate((float) tx, (float) ty);
			Chartly.app.textAlign(tx+x > x ? PConstants.LEFT : PConstants.RIGHT);
			Chartly.app.text(current.label + " " + DataUtils.floatToFormattedString(percent) + "%", x, y);
			Chartly.app.popMatrix();

			// incrementation
			r+=dr;			
		}
		
		if (title != null) {
			Chartly.app.textSize((radius) / TOP_TITLE);
			Chartly.app.textAlign(PConstants.CENTER);
			Chartly.app.stroke(looks.getAxisColor());
			Chartly.app.fill(looks.getAxisColor());
			Chartly.app.text(title, x, (float) (y - (1.11 *radius) - radius/LABEL));
		}
		
		Chartly.cleaner.restore();
	}
	
	@Override
	public void refresh() {
		draw(x,y);
	}

	@Override
	public PieChart setData(DataSet data) {
		this.data = data;
		this.max = data.getTotal();
		return this;
	}

	@Override
	public DataSet getData() {
		return data;
	}

	@Override
	public Looks getLooks() {
		return looks;
	}

	@Override
	public PieChart setLooks(Looks scheme) {
		this.looks = scheme;
		return this;
	}
	
	/**
	 * Sets the title of the chart
	 * @param title new title
	 * @return this
	 */
	public PieChart setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * Get the title of this chart
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Ensures that the graph has all of the things it needs
	 * before drawing it.
	 * 
	 * @throws MissingInformationException Thrown if no data is provided.
	 */
	private void testComplete() throws MissingInformationException {
		if (this.data.getData().isEmpty()) {
			throw MissingInformationException.noData();
		}
	}
	 
	/** 
	 * 
	 * @return the radius
	 */
	public float getRadius() {
		return this.radius;
	}

}
