package me.max.chartly.components.color;

import me.max.chartly.Defaults;
import processing.core.PFont;

public class Looks {	
	private int[] chartColors;
	private int axisColor;
	private int index;
	private PFont font;
	
	/**
	 * Empty Looks Constructor (Default ChartColors & AxisColor)
	 */
	public Looks() {
		this(Defaults.getLooks().getAxisColor(), Defaults.getLooks().getChartColors());
	}
	
	/**
	 * Creates a new Looks with the given axis color. (Default ChartColors)
	 * @param axisColor Axis color (Hex recomended)
	 */
	public Looks(int axisColor) {
		this(axisColor, Defaults.getLooks().getChartColors());
	}
	

	/**
	 * Creates a new Looks with the given ChartColors. (Default AxisColors)
	 * @param chartColors An array of Chart Colors (Hex Recomended)
	 */
	public Looks(int... chartColors) {
		this(Defaults.getLooks().getAxisColor(), chartColors);
	}
	
	/**
	 * Creates a new Looks with the given ChartColors and AxisColors.
	 * @param axisColor Axis color (Hex recomended)
	 * @param chartColors An array of Chart Colors (Hex Recomended)
	 */
	public Looks(int axisColor, int[] chartColors) {
		this.axisColor = axisColor;
		this.chartColors = chartColors;
		this.font = Defaults.getFont();
	}
	
	/**
	 * Get the chart colors
	 * @return an array of ints that represent the chart's colors
	 */
	public int[] getChartColors() {
		return chartColors;
	}
	
	/**
	 * Sets the chart colors
	 * @param chartColors New array of chart colors
	 * @return this
	 */
	public Looks setChartColors(int... chartColors) {
		this.chartColors = chartColors;
		return this;
	}
	
	/**
	 * Gets the axis color
	 * @return int that represents the Axis Color
	 */
	public int getAxisColor() {
		return axisColor;
	}
	
	/**
	 * Sets the axis color
	 * @param axisColor The new Axis Color
	 * @return this
	 */
	public Looks setAxisColor(int axisColor) {
		this.axisColor = axisColor;
		return this;
	}
	
	/**
	 * Tells the drawer to move on to the next color in the sequence.
	 * @return The new color
	 */
	public int next() {
		return chartColors[(index = ++index % chartColors.length)];
	}
	
	public Looks setFont(PFont font) {
		this.font = font;
		return this;
	}
	
	public PFont getFont() {
		return this.font;
	}
	
}
