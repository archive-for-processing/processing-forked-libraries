package manoloide.Gui;

public class SliderRange extends Elemento {
	boolean mover;
	float val1, val2, min_val, max_val;

	public SliderRange(String name, int x, int y, int w, int h, float min_val,
			float max_val, float val1, float val2) {
		super(null, name, x, y, w, h);
		this.min_val = min_val;
		this.max_val = max_val;
		this.val1 = val1;
		this.val2 = val2;
	}

	public SliderRange(Gui gui, String name, int x, int y, int w, int h,
			float min_val, float max_val, float val1, float val2) {
		super(gui, name, x, y, w, h);
		this.min_val = min_val;
		this.max_val = max_val;
		this.val1 = val1;
		this.val2 = val2;
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
			float posX = mouseX;
			if (posX < x) {
				posX = x;
			} else if (posX > x + w) {
				posX = x + w;
			}
			float pos1 = x+((w - h) * (val1 - min_val) / (max_val - min_val));
			float pos2 = x+((w - h) * (val2 - min_val) / (max_val - min_val));
			float val = (min_val + (max_val - min_val) * ((posX - x) / (w)));
			if (val < min_val) {
				val = min_val;
			} else if (val > max_val) {
				val = max_val;
			}
			if(gui.applet.abs(pos1-posX) < gui.applet.abs(pos2-posX)){
				val1 = val;
			} else if(gui.applet.abs(pos1-posX) > gui.applet.abs(pos2-posX)){
				val2 = val;
			}else{
				if(posX >= pos1){
					val2 = val;
				}else{
					val1 = val;
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
		float pos1 = ((w) * (val1 - min_val) / (max_val - min_val));
		float pos2 = ((w) * (val2 - min_val) / (max_val - min_val));
		gui.applet.rect(x+pos1, y, pos2-pos1, h);
		gui.applet.fill(gui.paleta.get(0));
		gui.applet.rect(x+pos1-2, y, 2, h);
		gui.applet.rect(x+pos2, y, 2, h);

		gui.applet.fill(gui.paleta.get(1));
		if (gui.applet.abs(max_val - min_val) >= 20) {
			gui.applet.text(name + " " + (int) val1 + " - " + (int) val2, x + 2, y + 11 + h);
		} else {
			gui.applet.text(name + " " + val1 + " - " + val2, x + 2, y + 11+ h);
		}
	}

	public float getFloat() {
		return val1;
	}

	public int getInt() {
		return gui.applet.round(val1);
	}

	public void set(float val1, float val2) {
		this.val1 = val1;
		this.val2 = val2;
	}
}


