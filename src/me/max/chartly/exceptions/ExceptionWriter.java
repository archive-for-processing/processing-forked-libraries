package me.max.chartly.exceptions;

import processing.core.PApplet;

public class ExceptionWriter {
	
	public static void write(Exception ex) {
		h1("Chartly Error");
		h2("Information");
		h3("Error type: " + ex.getClass().getSimpleName());
		h3("Error Message: " + ex.getMessage());
	}
	
	private static void h1(String string) {
		_println("-=-=-=- " + string + " -=-=-=-");
	}
	
	private static void h2(String string) {
		_println("------- " + string + " -------");
	}
	
	private static void h3(String string) {
		_println("- " + string);
	}
	
	private static void _println(String string) {
		PApplet.println(string);
	}
	
}
