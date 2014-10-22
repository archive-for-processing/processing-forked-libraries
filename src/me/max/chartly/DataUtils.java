package me.max.chartly;

import java.text.DecimalFormat;

import processing.core.PConstants;

public class DataUtils {
	/**
	 * Converts a percent to the equivalent portion of 2 PI Radians
	 * @param d Percent to convert
	 * @return Radians
	 */
	public static float percentToRadians(double d) {
		return (float) ((d * 2 * PConstants.PI) / 100);
	}
	
	/**
	 * Shortens numbers to a representable state (#*.###)
	 * @param f float to trim
	 * @return trimmed number as a string
	 */
	public static String floatToFormattedString(float f) {
		if (f ==  (int) f) {
			return String.valueOf((int) f);
		} else {
			return new DecimalFormat("#.##").format(f);
		}
	}
	
	public static Float[] primitiveToObjectFloatArray(float[] primitives) {
		Float[] floats = new Float[primitives.length];
		for (int i = 0; i < primitives.length; i++) {
			floats[i] = primitives[i];
		}
		return floats;
	}
	
	public static Float[] intToFloatArray(int[] ints) {
		Float[] floats = new Float[ints.length];
		for (int i = 0; i < ints.length; i++) {
			floats[i] = (float) ints[i];
		}
		return floats;
	}
	
}
