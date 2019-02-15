package kml_builder.namespace.gx;

import kml_builder.KMLObject;

public abstract class TourPrimitive<T extends KMLObject> extends KMLObject<T> {
	protected TourPrimitive(String tag) {
		super(tag);
	}
}
