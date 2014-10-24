package me.max.chartly.exceptions;

/**
 * Superclass for all input-based custom exceptions
 * 
 * @author Max Johnson
 * @example Error_Example
 */
@SuppressWarnings("serial")
public abstract class InputException extends Exception {
		
	public InputException() { super(); }
	public InputException(String message) { super(message); }
	public InputException(String message, Throwable cause) { super(message, cause); }
	public InputException(Throwable cause) { super(cause); }
	
}
