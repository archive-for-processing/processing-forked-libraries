package manoloide.Gui;

public class Toggle extends Elemento {
	public boolean click, press, val;
	public Toggle(String name, int x, int y, int w, int h, boolean val) {
		super(null, name, x, y, w, h);
		this.val = val;
		click = press = false;
	}
	public Toggle(Gui gui, String name, int x, int y, int w, int h, boolean val) {
		super(gui, name, x, y, w, h);
		this.val = val;
		click = press = false;
	}

	public void update(int x, int y) {
		x = this.x + x;
		y = this.y + y;
		click = false;
		int mouseX = gui.input.mouseX;
		int mouseY = gui.input.mouseY;
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
		gui.applet.fill(gui.paleta.get(1));
		gui.applet.text(name, x + 2, y + 11 + h);
	}

	public boolean get() {
		return val;
	}

	public void set(boolean val) {
		this.val = val;
	}
}
