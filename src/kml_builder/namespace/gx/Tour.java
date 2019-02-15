package kml_builder.namespace.gx;

import kml_builder.KMLRoot;
import processing.data.XML;

public class Tour extends KMLRoot<Tour> {
	protected Tour(KMLRoot parent) {
		super(parent, "gx:Tour");
	}
	
	public Tour addName(String name) {
		this.addName(name);
		return this;
	}
	
	public Tour addDescription(String name) {
		this.addDescription(name);
		return this;
	}
	
	public Tour addPlayList(TourPrimitive tp) {
		XML playList;
		
		playList = this.cursor.addChild("gx:Playlist");
		playList.addChild(tp.cursor);
		
		return this;
	}
}
