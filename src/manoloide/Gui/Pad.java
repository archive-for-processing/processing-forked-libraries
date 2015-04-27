package manoloide.Gui;

public class Pad extends Elemento {
	public boolean mover;
	public float valx, valy, min_x, max_x, min_y, max_y;

	public Pad(String name, int x, int y, int w, int h, float min_x,
			float max_x, float min_y, float max_y) {
		super(null, name, x, y, w, h);
		mover = false;
		this.min_x = min_x;
		this.min_y = min_y;
		this.max_x = max_x;
		this.max_y = max_y;
		valx = min_x + (max_x-min_x)/2;
		valy = min_y + (max_y-min_y)/2;
	}

	public Pad(Gui gui, String name, int x, int y, int w, int h, float min_x,
			float max_x, float min_y, float max_y) {
		super(gui, name, x, y, w, h);
		mover = false;
		this.min_x = min_x;
		this.min_y = min_y;
		this.max_x = max_x;
		this.max_y = max_y;
	}

	public void update(int x, int y) {
		x = this.x + x;
		y = this.y + y;
		int mouseX = gui.input.mouseX;
		int mouseY = gui.input.mouseY;
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
			float posY = mouseY;
			if (posX < x) {
				posX = x;
			} else if (posX > x + w) {
				posX = x + w;
			}
			if (posY < y) {
				posY = y;
			} else if (posY > y + h) {
				posY = y + h;
			}
			valx = (min_x + (max_x - min_x) * ((posX - x)/w));
			valy = (min_y + (max_y - min_y) * ((posY - y) /h));
			if (valx < min_x) {
				valx = min_x;
			} else if (valx > max_x) {
				valx = max_x;
			}
			if (valy < min_y) {
				valy = min_y;
			} else if (valy > max_y) {
				valy = max_y;
			}
		}
	}

	public void draw(int x, int y) {
		x = this.x + x;
		y = this.y + y; 
		float vx = (this.valx-min_x)/(max_x-min_x);
		float vy = (this.valy-min_y)/(max_y-min_y);
		gui.applet.fill(gui.paleta.get(0));
		gui.applet.rect(x, y, w, h);
		gui.applet.stroke(gui.paleta.get(1));
		gui.applet.line(x+w*vx, y, x+w*vx, y+h);
		gui.applet.line(x, y+h*vy, x+w, y+h*vy);
		gui.applet.noStroke();
		gui.applet.fill(gui.paleta.get(1));
		gui.applet.rect(x+w*vx-4, y+h*vy-4, 8, 8);
		gui.applet.text(name + " " + valx + " " + valy, x + 2, y + 11 + h);
	}
}
