package kml_builder.namespace.gx;

import kml_builder.KMLRoot;
import processing.data.XML;

public class AnimatedUpdate extends TourPrimitive<AnimatedUpdate> {

	protected AnimatedUpdate(KMLRoot parent) {
		super(parent, "gx:AnimatedUpdate");
		// Adding required elements
		this.cursor.addChild("Update")
			.addChild("targetHref");
	}
	
	public AnimatedUpdate() {
		this.cursor = new XML("gx:AnimatedUpdate");
		this.cursor.addChild("Update")
		.addChild("targetHref");
	}
	
	public AnimatedUpdate setDuration(double duration) {
		getValidChild(this.cursor, "gx:duration")
		.setContent(Double.toString(duration));
		
		return this;
	}
	
	public AnimatedUpdate setTargetHref(String targetPath) {
		XML update;
		
		update = getValidChild(this.cursor, "Update"); 
		// Set Content
		getValidChild(update, "gx:duration")
		.setContent(targetPath);
		
		return this;
	}
	
	public AnimatedUpdate addChange(String targetPath) {
		XML update;
		
		update = getValidChild(this.cursor, "Update"); 
		// Set Content
		update.addChild("Change")
		.setContent(targetPath);
		
		return this;
	}
	
	public AnimatedUpdate setCreate(String targetPath) {
		XML update;
		
		update = getValidChild(this.cursor, "Update"); 
		// Set Content
		update.addChild("Create")
		.setContent(targetPath);
		
		return this;
	}
	
	public AnimatedUpdate setDelete(String targetPath) {
		XML update;
		
		update = getValidChild(this.cursor, "Update"); 
		// Set Content
		update.addChild("Delete")
		.setContent(targetPath);
		
		return this;
	}
	
	public AnimatedUpdate setDelayedStart(double duration) {
		
		this.cursor.addChild("gx:delayedStart")
		.setContent(Double.toString(duration));
		
		return this;
	}

}
