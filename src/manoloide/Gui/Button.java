package manoloide.Gui;

public class Button extends Elemento {
	public boolean click, press;

	public Button(String name, int x, int y, int w, int h) {
		super(null, name, x, y, w, h);
		click = press = false;
	}

	public Button(Gui gui, String name, int x, int y, int w, int h) {
		super(gui, name, x, y, w, h);
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
		}
		if (gui.input.released) {
			press = false;
		}
	}

	public void draw(int x, int y) {
		x = this.x + x;
		y = this.y + y;

		if (click) {
			gui.applet.fill(gui.paleta.get(1));
		} else {
			gui.applet.fill(gui.paleta.get(0));
		}
		if (sobre) {
			gui.applet.fill(gui.paleta.get(1), 20);
			gui.applet.rect(x, y, w, h);
		}
		gui.applet.rect(x, y, w, h);

		gui.applet.fill(gui.paleta.get(1));
		gui.applet.text(name, x + 2, y + 11 + h);
	}
}
