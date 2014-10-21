package me.max.chartly.exceptions;

public class MissingInformationException extends InputException {
	
	private static final long serialVersionUID = 1004224374531146022L;
	
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
	
	public static MissingInformationException noTitles() {
		return new MissingInformationException("No Titles Provided.");
	}

}
