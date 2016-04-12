package funGUI;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class RoundToggleButton extends RoundButton {
	public boolean on = false;
	boolean click = false;
	
	public RoundToggleButton(PApplet p, float x, float y, float r) {
		super(p, x, y, r);
		p.registerMethod("mouseEvent", this);
	}
	
	public RoundToggleButton(PApplet p, float x, float y, int r, char c) {
		super(p, x, y, r, c);
		p.registerMethod("mouseEvent", this);
	}
	
	@Override
	public void display() {
		g.pushStyle();
		g.noStroke();
		g.fill(220);
		g.ellipse(x, y, r * 2, r * 2);
		if (on) {
			g.fill(240, 240, 240);
		} else {
			g.fill(255, 255, 255);
		}
		g.ellipse(x, y, 2 * r - PApplet.log((r / r0) * 2.718f) * ri, 2 * r - PApplet.log((r / r0) * 2.718f) * ri);
		g.popStyle();
	}

	public void mouseEvent(MouseEvent me) {
		if (me.getAction() == MouseEvent.PRESS) {
			toggle();
		}
	}
	
	void toggle() {
		if (clicked() && !click) {
			on = !on;
		}
		click = clicked();
	}
	
	public boolean on() {
		return(on);
	}

}
