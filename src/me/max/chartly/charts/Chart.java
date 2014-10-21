package me.max.chartly.charts;

import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataSet;

public interface Chart {

	/**
	 * Draws the graph
	 * @param x Bottom left corner X-Coord
	 * @param y Bottom left corner Y-Coord
	 */
	public void draw(float x, float y);
	
	/**
	 * Re-Draws the graph
	 */
	public void refresh();
	
	/**
	 * Provides the chart with its data
	 * @param data DataSet to give the chart
	 * @return this
	 */
	public Chart setData(DataSet data);
	
	/**
	 * Get the chart's data
	 * @return Chart's dataset
	 */
	public DataSet getData();
	
	/**
	 * Get the current ColorScheme
	 * @return the ColorScheme
	 */
	public ColorScheme getColorScheme();
	
	/**
	 * Provides the chart with a colorscheme
	 * @param scheme the new colorscheme
	 * @return this
	 */
	public Chart setColorScheme(ColorScheme scheme);
		
}
