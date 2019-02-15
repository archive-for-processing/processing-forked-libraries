package kml_builder.namespace.gx;

import kml_builder.AbstractView;
import kml_builder.Camera;
import kml_builder.KMLRoot;
import kml_builder.LookAt;
import processing.data.XML;

public class FlyTo extends TourPrimitive<FlyTo> {
	protected FlyTo(KMLRoot<?> parent) {
		super(parent,"gx:FlyTo");
	}
	
	public FlyTo() {
		this.cursor = new XML("gx:FlyTo");
	}
	
	public FlyTo setDuration(double duration) {
		this.cursor.addChild("gx:duration")
		.setContent(Double.toString(duration));
		
		return this;
	}
	
	public FlyTo setFlyMode(String Mode) {
		getValidChild(this.cursor,"gx:flyToMode")
		.setContent(Mode);
		
		return this;
	}
	
	
	public FlyTo setAbstractView(AbstractView<?> av) {
		this.cursor.addChild(av.cursor);
		
		return this;
	}
	
	public Camera addCamera(String id) {
		Camera out = new Camera(this);
		return out.addID(id);
	}
	
	public LookAt addLookAt(String id) {
		LookAt out = new LookAt(this);
		return out.addID(id);
	}
}
