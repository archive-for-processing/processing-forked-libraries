package me.max.chartly.charts;

import me.max.chartly.Chartly;
import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.MissingInformationException;

public class BarChart extends AxisChart {
	
	/**
	 * Constructor
	 * @param dx Width
	 * @param dy Height
	 */
	public BarChart(float dx, float dy) {
		data = new DataSet();
		font = Chartly.app.createFont("Helvetica", 12);
		colorScheme = ColorScheme.getDefaultColorScheme();
		this.x_axis_width = dx;
		this.y_axis_height = dy;
	}
	
	@Override
	public void draw(float x, float y) {
		try {
			testComplete();
		} catch (MissingInformationException ex) {
			ExceptionWriter.write(ex);
			return;
		}
		
		Chartly.app.textFont(font);
		
		float w = (float) (x_axis_width/(1.5 * data.getData().size() + .5)); //Math done on whiteboard.
		int count = 0;
		for (DataPair pair : data.getData()) {
			int c = colorScheme.next();
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
	 * @param end Max height (in units)
	 * @param incr distance between labels on YAxis
	 * @return this
	 */
	public BarChart setYLabels(float end, float incr) {
		this.max_y_scale = end;
		this.y_axis_increment = incr;
		return this;
	}
	
	/**
	 * Resize the graph
	 * @param dx New width
	 * @param dy New height
	 * @return
	 */
	public BarChart resize(float dx, float dy) {
		this.x_axis_width = dx == 0 ? this.x_axis_width : dx;
		this.y_axis_height = dy == 0 ? this.y_axis_height : dy;
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
	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	@Override
	public BarChart setColorScheme(ColorScheme scheme) {
		this.colorScheme = scheme;
		return this;
	}
	
	@Override
	public BarChart setTitles(String xtitle, String ytitle, String title) {
		super.setTitles(xtitle, ytitle, title);
		return this;
	}
	
	private void testComplete() throws MissingInformationException {
		if (this.data.getData().isEmpty()) {
			throw MissingInformationException.noData();
		}
		if (y_axis_increment == 0 && max_y_scale == 0) {
			throw MissingInformationException.noLabels();
		}
	}

}
