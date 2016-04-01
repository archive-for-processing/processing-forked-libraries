package funGUI;

import processing.core.*;

public class IncrementalSlider extends Slider {
	public IncrementalSlider(PApplet p, float x, float y, float min, float max) {
		super(p, x, y, min, max);
	}
	
	public IncrementalSlider(PApplet p, float x, float y, float min,
			float max, String label) {
		super(p, x, y, min, max, label);
	}
	
	public IncrementalSlider(PApplet p, float x, float y, float min,
			float max, String label, float initial) {
		super(p, x, y, min, max, label, initial);
	}
	
	@Override
	public float percent() {
		return((float) (increment()) / Math.round(range()));
	}
	
	public int increment() {
		return(Math.round(p()));
	}
}
