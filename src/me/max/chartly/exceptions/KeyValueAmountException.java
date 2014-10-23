package me.max.chartly.exceptions;

/**
 * This exceptions represents an invalid input in which
 * the user either gives more keys than values or visa versa.
 * 
 * @author maxjohnson
 */
public class KeyValueAmountException extends InputException {

	private static final long serialVersionUID = -4181974239466042064L; //auto-generated
	
	public KeyValueAmountException() { super(); }
	public KeyValueAmountException(String message) { super(message); }
	public KeyValueAmountException(String message, Throwable cause) { super(message, cause); }
	public KeyValueAmountException(Throwable cause) { super(cause); }

}
