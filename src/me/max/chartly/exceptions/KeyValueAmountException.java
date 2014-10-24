package me.max.chartly.exceptions;

/**
 * This exceptions represents an invalid input in which
 * the user either gives more keys than values or visa versa.
 * 
 * @author Max Johnson
 * @example Error_Example
 */
@SuppressWarnings("serial")
public class KeyValueAmountException extends InputException {
	
	public KeyValueAmountException() { super(); }
	public KeyValueAmountException(String message) { super(message); }
	public KeyValueAmountException(String message, Throwable cause) { super(message, cause); }
	public KeyValueAmountException(Throwable cause) { super(cause); }

}
