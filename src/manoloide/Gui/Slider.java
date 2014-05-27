package manoloide.Gui;

public class Slider extends Elemento {
	boolean mover, vertical;
	float val, min_val, max_val;

	public Slider(String name, int x, int y, int w, int h, float min_val,
			float max_val, float val) {
		super(null, name, x, y, w, h);
		this.min_val = min_val;
		this.max_val = max_val;
		this.val = val;
		if (h > w)
			vertical = true;
		else
			vertical = false;
	}

	public Slider(Gui gui, String name, int x, int y, int w, int h,
			float min_val, float max_val, float val) {
		super(gui, name, x, y, w, h);
		this.min_val = min_val;
		this.max_val = max_val;
		this.val = val;
		if (h > w)
			vertical = true;
		else
			vertical = false;
	}

	public void update(int x, int y) {
		x = this.x + x;
		y = this.y + y;
		int mouseX = gui.applet.mouseX;
		int mouseY = gui.applet.mouseY;
		if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h) {
			sobre = true;
		} else
			sobre = false;
		if (sobre && gui.input.click) {
			mover = true;
		}
		if (gui.input.released) {
			mover = false;
		}

		if (mover) {
			if (vertical) {
				float posY = mouseY;
				if (posY < y) {
					posY = y;
				} else if (posY > y + h) {
					posY = y + h;
				}
				val = (min_val + (max_val - min_val)
						* ((posY - w / 2 - y) / (h - w)));
				if (val < min_val) {
					val = min_val;
				} else if (val > max_val) {
					val = max_val;
				}
			} else {
				float posX = mouseX;
				if (posX < x) {
					posX = x;
				} else if (posX > x + w) {
					posX = x + w;
				}
				val = (min_val + (max_val - min_val)
						* ((posX - h / 2 - x) / (w - h)));
				if (val < min_val) {
					val = min_val;
				} else if (val > max_val) {
					val = max_val;
				}
			}
		}
	}

	public void draw(int x, int y) {
		x = this.x + x;
		y = this.y + y;
		gui.applet.fill(gui.paleta.get(0));
		gui.applet.rect(x, y, w, h);
		gui.applet.fill(gui.paleta.get(1));
		if (vertical) {
			float pos = x;// - ((w - h) * ((val - min_val) / (max_val - min_val)));
			gui.applet.rect(x, pos, w, w);
		}else{
			float pos = y;// - ((h - w) * ((val - min_val) / (max_val - min_val)));
			gui.applet.rect(pos, y, h, h);
		}
		if (gui.applet.abs(max_val - min_val) >= 20) {
			gui.applet.text(name + " " + (int) val, x + 2, y + 11 + h);
		} else {
			gui.applet.text(name + " " + val, x + 2, y + 11 + h);
		}
	}

	public float getFloat() {
		return val;
	}

	public int getInt() {
		return gui.applet.round(val);
	}

	public void set(float val) {
		this.val = val;
	}
}
