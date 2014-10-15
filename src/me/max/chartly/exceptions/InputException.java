package me.max.chartly.exceptions;

public abstract class InputException extends Exception {

	private static final long serialVersionUID = 1L; //no idea what this does.
	
	public InputException() { super(); }
	public InputException(String message) { super(message); }
	public InputException(String message, Throwable cause) { super(message, cause); }
	public InputException(Throwable cause) { super(cause); }
	
}
