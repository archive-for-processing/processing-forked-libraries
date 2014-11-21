package me.max.chartly;

public class Formatting {

	// Percents
	
	// Axis
	private static final float AXIS_MAIN_TITLE = 15, 
			AXIS_AXIS_TITLE = 10, AXIS_AXIS_WIDTH = 1, LABEL = 12;
	
	public static float axisMainTitleSizeFromHeight(float height) {
		return height * per(AXIS_MAIN_TITLE);
	}
	
	public static float axisAxisTitleFromAxisLength(float length) {
		return length * per(AXIS_AXIS_TITLE);
	}
	
	public static float labelSizeFromAxisLength(float length, float dataLength) {
		float max = length/dataLength;
		float prefered = length* per(LABEL);
		return prefered < max ? prefered : max;
	}
	
	public static float axisAxisWidthFromAxisLength(float length) {
		return length * per(AXIS_AXIS_WIDTH);
	}
		
	private static float per(float f) {
		return f/100;
	}
}
