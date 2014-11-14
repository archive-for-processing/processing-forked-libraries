package me.max.chartly.charts;

import me.max.chartly.Chartly;
import me.max.chartly.Defaults;
import me.max.chartly.components.color.Looks;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.MissingInformationException;

/**
 * Represents a BarChart (also called a bar-graph)
 * 
 * @author Max Johnson
 * @example Chart_Example
 */
public class BarChart extends AxisChart {
	
	/**
	 * Creates a new BarChart object with the specified width and height
	 * @param width Width (in pixels)
	 * @param height Height (in pixels)
	 */
	public BarChart(float width, float height) {
		if (Chartly.app == null) {
			 ExceptionWriter.write(MissingInformationException.noApplet());
			 return;
		}
		data = new DataSet();
		font = Chartly.app.createFont("Helvetica", 12);
		looks = Defaults.getLooks();
		this.x_axis_width = width;
		this.y_axis_height = height;
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
		
		float w = (float) (x_axis_width/(1.5 * data.size() + .5)); //Math done on whiteboard.
		int count = 0;
		for (DataPair pair : data.getData()) {
			int c = looks.next();
			Chartly.app.stroke(c);
			Chartly.app.fill(c);
			Chartly.app.rect(
					(float) (x + w * (.5 + 1.5 * count)), y, 
					w, -1 * this.getHeightFactor(pair.value, max_y_scale, y_axis_height) // Reverse the Y
			);
			count++;
		}
		super.draw(x, y);
	}
	
	/**
	 * Provides the height in units and the increment of the yaxis
	 * @param top Max height (in units)
	 * @param increment distance between labels on YAxis
	 * @return this
	 */
	public BarChart setYLabels(float top, float increment) {
		this.max_y_scale = top;
		this.y_axis_increment = increment;
		return this;
	}
	
	/**
	 * Resize the graph
	 * @param width New width
	 * @param height New height
	 * @return this
	 */
	public BarChart resize(float width, float height) {
		this.x_axis_width = width == 0 ? this.x_axis_width : width;
		this.y_axis_height = height == 0 ? this.y_axis_height : height;
		return this;
	}
	
	@Override
	public void refresh() {
		draw(current_x,current_y);
	}
	
	@Override
	public BarChart setData(DataSet data) {
		this.data = data;
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
	public BarChart setLooks(Looks scheme) {
		this.looks = scheme;
		return this;
	}
	
	@Override
	public BarChart setTitles(String xtitle, String ytitle, String title) {
		super.setTitles(xtitle, ytitle, title);
		return this;
	}
	
	@Override
	public BarChart showLabels(boolean show) {
		super.showLabels(show);
		return this;
	}

}
