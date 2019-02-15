package kml_builder;

import processing.data.XML;

public class TimeStamp extends TimePrimitive<TimeStamp> {
	protected TimeStamp(KMLRoot parent) {
		super(parent, "TimeStamp");
	}
	
	private TimeStamp(KMLRoot parent, String nameSpace) {
		super(parent, nameSpace+":TimeStamp");
	}
	
	public static TimeStamp gxTimeStamp(KMLRoot parent) {
		TimeStamp out = new TimeStamp(parent, "gx");
		return out;
	}
	
	public TimeStamp setWhen(String dateTime){
		XML stamp;
		// Get or add "when" tags and insert date time
		stamp = getValidChild(this.cursor,"when");
		stamp.setContent(dateTime);
		return this;
	}
}
