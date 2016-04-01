package funGUI;
import processing.core.*;
import processing.data.FloatList;

class DotPlot extends Plot {
	float hratio, wratio, incr;
	FloatList times = new FloatList();
	FloatList yos;
	int bg;
	
	DotPlot(PApplet p, float ex, float why, float w, float h, PFont f, float fsize) {
		super(p, ex, why, f, fsize);
		hratio = 1;
		wratio = 1;
		this.w = w;
		this.h = h;
		yos = new FloatList();
		bg = g.color(250, 250, 250);
	}
	
	DotPlot(PApplet p, float ex, float why, PFont f, float fsize) {
		super(p, ex, why, f, fsize);
		hratio = 1;
		wratio = 1;
		yos = new FloatList();
		bg = g.color(250, 250, 250);
	}

	DotPlot(PApplet p, float ex, float why, PFont f, float fsize, float wrat, float hrat) {
		super(p, ex, why, f, fsize);
		hratio = hrat;
		wratio = wrat;
		yos = new FloatList();
		bg = g.color(250, 250, 250);
	}

	void update(float ex, float why, float wrat, float hrat, float q00, float q44, FloatList time) {
		if (x != ex) x = ex;
		if (y != why) y = why;
		if (wratio != wrat) wratio = wrat;
		if (hratio != hrat) hratio = hrat;
		if (q0 != q00) q0 = q00;
		if (q4 != q44) q4 = q44;
		times = time.copy();
		//	    if (times.size() < time.size() || times.size() > time.size()) {
		//	      times.clear();
		//	      for (int i = 0; i < time.size(); i++) {
		//	        int temp = time.get(i);
		//
		//	        times.append(PApplet.round(temp));
		//	      }
		//	    }
	}


	void draw() {
		float radius = (w / 2) - 5 * wratio;
		if (range() > 0) {
			float r = radius;
			display(wratio, hratio, times, radius);
			incr = range() / 20;

			times.sort();
			g.noFill();
			g.stroke(90);
			g.rectMode(CORNERS);
			//surrounding box
			g.rect(x - wr(70), y - hr(80), x + wr(70), y + hr(22));

			g.line(x - wr(65), y + hr(7.5f), x + wr(65), y + hr(7.5f));

			yos.clear();

			for (int i = 0; i < 21; i++) {
				int yo = 0;
				for (int j = 0; j < times.size(); j++) {
					if (withinRange(i, j)) {
						yo++;
					}
				}
				yos.append(yo);
			}
			float max = yos.max();
			max --;
			float a = 1;
			if (y + 4 * hratio - max * 5 * hratio < hlimit()) a = 2;
			if (y + 2 * hratio - max * 2.5 * hratio < hlimit()) a = 3;
			for (int i = 1; i < max; i++) {
				if (y + (4 / i) * hratio - max * (5 / i) * hratio < hlimit()) a = i;
			}
			a = maxLimit(max);
			g.pushStyle();
			g.fill(0);
			g.textSize(10);
			g.textAlign(CENTER, CENTER);
			g.text(a, x + wr(60), y - hr(60));
			g.popStyle();

			for (int i = 0; i < 21; i++) {
				g.pushStyle();
				g.fill(100f, 180f + (75*((float) (i + 1)/21.0f)), 255f);
				g.noStroke();
				if (yos.get(i) < a && a > 1 && yos.get(i) > 0) {
					float ratio = (float) ((yos.get(i) % a)) /  (a);
					g.ellipse(dotx(i, r), doty(0), 4 * wratio, 4 * hratio);
					if (yos.get(i) != a) {
						partialDots(ratio, i, r, -1);
					}
				} else {
					for (int l = 0; l < PApplet.round(yos.get(i) / a); l++) {
						g.ellipse(dotx(i, r), doty(l), 4 * wratio, 4 * hratio);
						if (yos.get(i) % a != 0 && l == PApplet.round(yos.get(i) / a) - 1 && a > 1) {
							float ratio = (float) (yos.get(i) % a) /  (float) (a);
							PApplet.println(ratio);
							partialDots(ratio, i, r, l);
						}
					}
				}
				g.popStyle();
			}
		}
	}

	void partialDots(float ratio, int i, float r, int l) {
		float xi = dotx(i, r);
		float yi = doty(l + 1);
		g.ellipse(xi, yi, 4 * wratio, 4 * hratio);
		g.pushStyle();
		g.noStroke();
		g.fill(bg);
		g.rectMode(CORNER);
		g.rect(xi - 2.5f * wratio, yi - 2.2f * hratio, 5f * wratio, 4.2f * hratio - ratio * 4.2f * hratio);
		g.popStyle();
	}

	float doty(int l) {
		return(y + 4 * hratio - l * 5 * hratio);
	}
	float dotx(int i, float r) {
		return(remap(increment(i), x, r));
	}

	float maxLimit(float max) {
		for (int i = 1; i < max; i++) {
			if (y + (4) * hratio - (max / i) * 5 * hratio > hlimit()) return(i);
		}
		return(1);
	}

	boolean withinRange(int i, int j) {
		return(times.get(j) <= increment(i) && times.get(j) > increment(i - 1));
	}

	float hlimit() {
		return(y - hr(80));
	}

	float increment(int i) {
		return((i * incr) + lowest());
	}

	float wr(float n) {
		return(wratio * n);
	}

	float hr(float n) {
		return(hratio * n);
	}
}