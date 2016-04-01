package funGUI;

import processing.core.*;
import processing.data.FloatList;
import funGUI.ToggleButton;

public class ToggleMatrix {
	private ToggleButton[][] toggles;
//	private float longest = 0;
	PApplet p;
	public boolean draw = false;
	PGraphics g = new PGraphics();
	int num = 0;

	public ToggleMatrix(PApplet p, int x, int y, float width, float height) {
		toggles = new ToggleButton[x][y];
		this.p = p;
		float w = width / (float) (x);
		float h = height / (float) (y);
		for (int i = 0; i < toggles.length; i++) {
			for (int j = 0; j < toggles[i].length; j++) {
				toggles[i][j] = new ToggleButton(p, i * w, j * h);
			}
		}
		num = x * y;
	}

	public void name(int x, int y, String name) {
		x = PApplet.constrain(x, 0, toggles.length);
		y = PApplet.constrain(y, 0, toggles[0].length);
		toggles[x][y].name(name);
		if (filled() && !draw) {
			draw = true;
			FloatList fls = new FloatList();
			for (int i = 0; i < toggles.length; i++) {
				for (int j = 0; j < toggles[i].length; j++) {
					fls.append(g.textWidth(toggles[i][j].label));
				}
			}
//			longest = fls.max();
		}
	}

	public void draw() {
		if (draw) {
			for (int i = 0; i < toggles.length; i++) {
				for (int j = 0; j < toggles[i].length; j++) {
					toggles[i][j].draw();
				}
			}
		}
	}

	public boolean filled() {
		int count = 0;
		for (int i = 0; i < toggles.length; i++) {
			for (int j = 0; j < toggles[i].length; j++) {
				if (toggles[i][j] != null)
					count++;
			}
		}
		return (count == (num));
	}
}
