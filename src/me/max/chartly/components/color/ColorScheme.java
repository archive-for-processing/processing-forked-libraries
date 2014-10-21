package me.max.chartly.components.color;

public class ColorScheme {
	private int[] chartColors;
	private int axisColor;
	private int index;
	
	/**
	 * Empty ColorScheme Constructor (Needs ChartColors & AxisColor)
	 */
	public ColorScheme() {}
	
	/**
	 * Creates a new ColorScheme with the given axis color. (Needs ChartColors)
	 * @param axisColor Axis color (Hex recomended)
	 */
	public ColorScheme(int axisColor) {
		this.axisColor = axisColor;
	}
	

	/**
	 * Creates a new ColorScheme with the given ChartColors. (Needs AxisColors)
	 * @param chartColors An array of Chart Colors (Hex Recomended)
	 */
	public ColorScheme(int... chartColors) {
		this.chartColors = chartColors;
	}
	
	/**
	 * Creates a new ColorScheme with the given ChartColors and AxisColors.
	 * @param axisColor Axis color (Hex recomended)
	 * @param chartColors An array of Chart Colors (Hex Recomended)
	 */
	public ColorScheme(int axisColor, int[] chartColors) {
		
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
	public ColorScheme setChartColors(int... chartColors) {
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
	public ColorScheme setAxisColor(int axisColor) {
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
	
	/**
	 * Creates a new default ColorScheme
	 * @return Default color scheme
	 */
	public static ColorScheme getDefaultColorScheme() {
		return new ColorScheme().setAxisColor(0)
				.setChartColors(100, 110, 120, 130, 140, 150, 160, 170, 180, 190);
	}
}
