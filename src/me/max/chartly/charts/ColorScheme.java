package me.max.chartly.charts;

public class ColorScheme {
	private int[] chartColors;
	private int axisColor;
	private int index;
	
	public ColorScheme(int axisColor) {
		this.axisColor = axisColor;
	}
	
	public ColorScheme(int... chartColors) {
		this.chartColors = chartColors;
	}
	
	public ColorScheme(int axisColor, int[] chartColors) {
		
	}
	
	public int[] getChartColors() {
		return chartColors;
	}
	
	public ColorScheme setChartColors(int... chartColors) {
		this.chartColors = chartColors;
		return this;
	}
	
	public int getAxisColor() {
		return axisColor;
	}
	
	public ColorScheme setAxisColor(int axisColor) {
		this.axisColor = axisColor;
		return this;
	}
	
	public int next() {
		return chartColors[(index = ++index % chartColors.length)];
	}
	
	public static ColorScheme getDefaultColorScheme() {
		return new ColorScheme().setAxisColor(0)
				.setChartColors(100, 110, 120, 130, 140, 150, 160, 170, 180, 190);
	}
}
