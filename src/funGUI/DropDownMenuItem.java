package funGUI;

import processing.core.PApplet;

public class DropDownMenuItem extends MenuItem {
	
	
	public DropDownMenuItem(PApplet p, float ex, float why, String label, int numColumns, float width, int ordinal, Menu menu) {
		super(p, ex, why, label, numColumns, width, ordinal, menu);
	}
	
	
	public void draw(boolean selected) {
		g.pushStyle();
		display(selected);
		text();
		g.popStyle();
	}

	protected void display(boolean okayToSelect) {
		g.pushStyle();
		g.rectMode(CORNER);
		if (selected) {
			g.fill(210);
		} else {
			g.fill(c);
		}
		g.stroke(0);
		g.rect(x, y(), w, h);
		g.popStyle();

		if (okayToSelect) {
			selected();
		}

	}
}
