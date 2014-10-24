package me.max.chartly.charts;

import me.max.chartly.components.color.Looks;
import me.max.chartly.components.data.DataSet;

/**
 * Interface of all Chart objects
 *
 * @author Max Johnson
 * @example Chart_Example
 */
public interface Chart {

	/**
	 * Draws the graph at the provided coordinates
	 * 
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
	 * Provides the chart with a Looks instance.
	 * Looks changes how the chart will look
	 * including fonts and colorschemes.
	 * @param looks the new Looks
	 * @return this
	 */
	public Chart setLooks(Looks looks);
		
}
