package me.max.chartly.charts;

import java.util.Iterator;

import me.max.chartly.Chartly;
import me.max.chartly.DataUtils;
import me.max.chartly.components.color.Looks;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.MissingInformationException;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

/**
 * Represents a PieChart
 * 
 * @author Max Johnson
 */
public class PieChart implements Chart {
	
	private DataSet data;
	private float x,y,radius,max;
	private PFont font;
	private Looks looks;
	
	/**
	 * Constructor
	 * @param radius Radius (in pixels)
	 */
	public PieChart(float radius) {
		data = new DataSet();
		
		//Default to center
		x = Chartly.app.width/2;
		y = Chartly.app.height/2;
		this.radius = radius;
		
		font = Chartly.app.createFont("Helvetica", 12);
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
		
		Chartly.app.textFont(font);
		
		this.x = x; 
		this.y = y;
		
		float r = PConstants.PI / 2; //90 degrees	
		Iterator<DataPair> it = data.getData().iterator();	
		
		while(it.hasNext()) {
			//Chart
			DataPair current = it.next();
			float percent = dataToPercent(current.value);
			float dr = DataUtils.percentToRadians(percent);
			
			Chartly.app.stroke(looks.getAxisColor());
			Chartly.app.fill(looks.next());
			
			Chartly.app.arc(x, y, radius * 2, radius * 2, r, r+dr);
			
			//Labeling
			double tx = (1.1 * radius) * Math.cos(r + dr/2); 
			double ty = (1.1 * radius) * Math.sin(r + dr/2);
			
			Chartly.app.pushMatrix();
			Chartly.app.translate((float) tx, (float) ty);
			Chartly.app.textAlign(tx+x > x ? PConstants.LEFT : PConstants.RIGHT);
			Chartly.app.text(current.label + " " + DataUtils.floatToFormattedString(percent) + "%", x, y);
			Chartly.app.popMatrix();

			//incrementation
			r+=dr;			
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
	
	private float dataToPercent(float f) {
		return PApplet.map(f, 0, max, 0, 100);
	}
	
	private void testComplete() throws MissingInformationException {
		if (this.data.getData().isEmpty()) {
			throw MissingInformationException.noData();
		}
	}
	
	public float getRadius() {
		return this.radius;
	}

}
