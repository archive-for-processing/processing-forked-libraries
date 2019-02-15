package kml_builder;
import processing.data.*;

public class TimeSpan extends TimePrimitive<TimeSpan> {
	protected TimeSpan(KMLRoot parent) {
		super(parent,"TimeSpan");
	}
	
	private TimeSpan(KMLRoot parent,String nameSpace) {
		super(parent, nameSpace+":TimeSpan");
	}
	
	public static TimeSpan gxTimeSpan(KMLRoot parent) {
		TimeSpan out = new TimeSpan(parent, "gx");
		return out;
	}
	
	public TimeSpan setBegin(String date) {
		XML begin;
		begin = getValidChild(this.cursor,"begin");
		begin.setContent(date);
		return this;
	}
	
	public TimeSpan setEnd(String date) {
		XML end;
		// Set KML:Date Times 
		end = getValidChild(this.cursor,"end");
		end.setContent(date);
		return this;
	}
}
