package me.max.chartly.charts;

import me.max.chartly.components.color.Looks;
import me.max.chartly.components.data.DataSet;

/**
 * Interface of all Chart objects
 *
 * @author Max Johnson
 */
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
	 * Get the current Looks
	 * @return the Looks
	 */
	public Looks getLooks();
	
	/**
	 * Provides the chart with a colorscheme
	 * @param scheme the new colorscheme
	 * @return this
	 */
	public Chart setLooks(Looks looks);
		
}
