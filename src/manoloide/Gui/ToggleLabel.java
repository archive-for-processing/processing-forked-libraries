package manoloide.Gui;

public class ToggleLabel extends Elemento {
	public boolean click, press, val;
	public ToggleLabel(String name, int x, int y, int w, int h, boolean val) {
		super(null, name, x, y, w, h);
		this.val = val;
		click = press = false;
	}
	public ToggleLabel(Gui gui, String name, int x, int y, int w, int h, boolean val) {
		super(gui, name, x, y, w, h);
		this.val = val;
		click = press = false;
	}

	public void update(int x, int y) {
		x = this.x + x;
		y = this.y + y;
		click = false;
		int mouseX = gui.applet.mouseX;
		int mouseY = gui.applet.mouseY;
		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h) {
			sobre = true;
		} else
			sobre = false;
		if (sobre && gui.input.click) {
			click = true;
			press = true;
			val = !val;
		}
		if (gui.input.released) {
			press = false;
		}
	}

	public void draw(int x, int y) {
		x = this.x + x;
		y = this.y + y;

		if (val) {
			gui.applet.fill(gui.paleta.get(1));
		} else {
			gui.applet.fill(gui.paleta.get(0));
		}
		gui.applet.rect(x, y, w, h);
		if (sobre) {
			gui.applet.fill(gui.paleta.get(1), 20);
			gui.applet.rect(x, y, w, h);
		}
		if(val) gui.applet.fill(gui.paleta.get(0));
		else gui.applet.fill(gui.paleta.get(1));
		gui.applet.pushStyle();
		gui.applet.textAlign(3, 3);
		gui.applet.text(name, x, y, w, h);
		gui.applet.popStyle();
	}

	public boolean get() {
		return val;
	}

	public void set(boolean val) {
		this.val = val;
	}
}
