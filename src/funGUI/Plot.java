package funGUI;
import processing.core.*;
import processing.data.FloatList;

class Plot implements PConstants {
	float x, y;
	float q0, q1, q2, q3, q4;
	float range, iqr;
	PFont font;
	float fontsize;
	FloatList outliers;
	float wra, hra;
	float r;
	PApplet p;
	PGraphics g;
	float w = 0;
	float h = 0;

	Plot(PApplet p, float ex, float why, PFont f, float fsize) {
		this.p = p;
		x = ex;
		y = why;
		font = f;
		fontsize = fsize;
		outliers = new FloatList();
		wra = 1;
		hra = 1;
	}

	void set(float q00, float q11, float q22, float q33, float q44) {
		q0 = q00;
		q1 = q11;
		q2 = q22;
		q3 = q33;
		q4 = q44;
		range = q4 - q0;
		iqr = q3 - q1;
	}

	void update(float ex, float why) {
		x = ex;
		y = why;
	}

	void display(float wratio, float hratio, FloatList outs, float radius) {
		outliers = outs(outs);
		wra = wratio;
		hra = hratio;
		if (range() > 0) {
			float increment = range() / 20;
			r = radius;
			for (int i = 0; i < 21; i++) {
				g.stroke(0);
				int s = 1;
				if ((i) % (4) == 0) {
					g.fill(0);
					String time = PApplet.nfc((float) (((i * increment) + lowest()) * .001), 2);
					g.textAlign(CENTER, CENTER);
					g.textFont(font, (fontsize - 2 * PApplet.log((float) ((p.width * 1.45) / p.height)))*hratio);
					if (remap((i * increment) + lowest(), x, r) > x + r - p.textWidth(time)) {
						g.textAlign(RIGHT, CENTER);
					} else if (i == 0)
						g.textAlign(LEFT, CENTER);
					g.text(time, remap((i * increment) + lowest(), x, r), y + 15*hratio);
					s = 2;
				}
				g.strokeWeight(s);
				g.line(remap((i * increment) + lowest(), x, r), y + 8*hratio, remap((i * increment) + lowest(), x, r), y + 6*hratio);
			}
		}
		g.strokeWeight(1);
	}

	float range() {
		return(highest() - lowest());
	}

	float lowest() {
		if (outliers.size() > 0 && outliers.min() < q0) return(outliers.min());
		return(q0);
	}

	float highest() {
		if (outliers.size() > 0 && outliers.max() > q4) return(outliers.max());
		return(q4);
	}

	float remap(float n, float x, float w) {
		return(PApplet.map(n, lowest(), highest(), (x - w), (x + w)));
	}

	float remap(float n, float shrink, float x, float w) {
		return(PApplet.map(n / shrink, lowest(), highest(), (x - w), (x + w)));
	}

	FloatList outs(FloatList ints) {
		if (ints.size() > 0) {
			for (int i = 0; i < ints.size(); i++) {
				if (ints.get(i) > q1 + iqr * 1.5 && ints.get(i) < q3 + iqr * 1.5) {
					ints.remove(i);
					i--;
				}
				if (i == ints.size()) return ints;
			}
		}
		return ints;
	}

}
