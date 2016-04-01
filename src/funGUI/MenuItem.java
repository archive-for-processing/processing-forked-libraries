package funGUI;
import processing.core.*;

public class MenuItem extends ListItem implements PConstants {
	Menu m;
	int ordinal = 0;
	public int alignment = CENTER;
	PFont font;
	int c = 0xFF3213;
	
	public MenuItem(PApplet p, float ex, float why, String label, int numColumns, float width, int ordinal, Menu menu) {
		super(p, ex, why, label, numColumns, width);
		this.ordinal = ordinal;
		this.m = menu;
		pressed = false;
		font = this.p.createFont("Helvetica", 13);
		h = 16;
		w = width;
	}
	
	public MenuItem(PApplet p, float ex, float why, String label, float width, int ordinal, Menu menu, int alignmentDirection) {
		super(p, ex, why, label, 1, width);
		this.ordinal = ordinal;
		this.m = menu;
		font = this.p.createFont("Helvetica", 13);
		pressed = false;
		alignment = alignmentDirection;
		h = 16;
		w = width;
	}
	
	public void colorize(int n) {
		this.c = n;
	}
	
	@Override
	protected void display() {
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
		
		selected();
		
	}

	void selected() {
//		PApplet.println(pressed);
		if (!pressed && clicked()) {
			selected = !selected;
			PApplet.println("selected");
//			PApplet.println("selected");
		}
		pressed = p.mousePressed;
	}
	
	@Override
	protected void text() {
		g.textAlign(alignment, CENTER);
		g.textFont(font);
		g.fill(30);
		g.text(label, (float) (x + w * 0.5), (float) (y() + (15.0 / 2)));
	}
	
	@Override
	public boolean clicked() {
		return(p.mousePressed && p.mouseX > x && p.mouseY > y() && p.mouseX < x + w && p.mouseY < y() + h);
	}
	
	public float x() {
		return(x);
	}
	
	public float y() {
		return(y + 18 * ordinal);
	}
}
