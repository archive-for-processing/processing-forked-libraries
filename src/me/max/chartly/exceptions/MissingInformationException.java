package me.max.chartly.exceptions;

/**
 * This exceptions represents an attempt to draw a chart
 * without the prerequisite information.
 * 
 * @author Max Johnson
 * @example Error_Example
 */
@SuppressWarnings("serial")
public class MissingInformationException extends InputException {
		
	public MissingInformationException() { super(); }
	public MissingInformationException(String message) { super(message); }
	public MissingInformationException(String message, Throwable cause) { super(message, cause); }
	public MissingInformationException(Throwable cause) { super(cause); }
	
	public static MissingInformationException noData() {
		return new MissingInformationException("No Data Provided.");
	}
	
	public static MissingInformationException noLabels() {
		return new MissingInformationException("No Labels Provided.");
	}

	public static MissingInformationException noApplet() {
		return new MissingInformationException("No App Registered. Try adding \"new Chartly(this)\" to your code");
	}
}
