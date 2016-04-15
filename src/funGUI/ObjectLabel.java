package funGUI;

import processing.core.PApplet;

public class ObjectLabel extends Label {
	Frame object;
	
	public ObjectLabel(PApplet p, float x, float y, float initialValue, Frame f) {
		super(p, x, y, p.g.textWidth(String.valueOf(initialValue)), REGTXTSIZE, initialValue, f.x(), f.y());
		object = f;
	}
	
	public ObjectLabel(PApplet p, float x, float y, float w, float h,
			float initialValue, Frame f) {
		super(p, x, y, w, h, initialValue, f.x(), f.y());
		object = f;
	}

	public ObjectLabel(PApplet p, float x, float y, float w, float h,
			float initialValue, float x0, float y0, Frame f) {
		super(p, x, y, w, h, initialValue, x0, y0);
		object = f;
	}

}
