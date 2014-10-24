package me.max.chartly.exceptions;

import processing.core.PApplet;

/**
 * Custom Exception writer. This class prints
 * more user-friendly error messages than the 
 * default printstacktrace.
 * 
 * @author Max Johnson
 */
public class ExceptionWriter {
	
	public static void write(Exception ex) {
		h1("Chartly Error");
		h2("Information");
		h3("Error type: " + ex.getClass().getSimpleName());
		h3("Error Message: " + ex.getMessage());
	}
	
	/**
	 * Prints a formatted string
	 * Result:
	 * -=-=-=- Example -=-=-=-
	 * 
	 * @param string the raw message.
	 */
	private static void h1(String string) {
		_println("-=-=-=- " + string + " -=-=-=-");
	}
	
	/**
	 * Prints a formatted string
	 * Result:
	 * ------- Example -------
	 * 
	 * @param string the raw message.
	 */
	private static void h2(String string) {
		_println("------- " + string + " -------");
	}
	
	/**
	 * Prints a formatted string
	 * Result:
	 * - Example
	 * 
	 * @param string the raw message.
	 */
	private static void h3(String string) {
		_println("- " + string);
	}
	
	/**
	 * Uses PApplet's print function to print a message
	 * to the applet console.
	 * 
	 * @param string the formatted string.
	 */
	private static void _println(String string) {
		PApplet.println(string);
	}
	
}
