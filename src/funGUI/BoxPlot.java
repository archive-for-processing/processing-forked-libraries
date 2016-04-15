package funGUI;
import processing.core.*;
import processing.data.*;

class BoxPlot extends Plot {
	float hratio, wratio;

	BoxPlot(PApplet p, float ex, float why, PFont f, float fsize) {
		super(p, ex, why, f, fsize);
		hratio = 1;
		wratio = 1;
	}
	
	BoxPlot(PApplet p, float ex, float why, PFont f, float fsize, float wrat, float hrat) {
		super(p, ex, why, f, fsize);
		hratio = hrat;
		wratio = wrat;
	}
	
	BoxPlot(PApplet p, float ex, float why, float w, float h, PFont f, float fsize) {
		super(p, ex, why, f, fsize);
		this.w = w;
		this.h = h;
		hratio = 1;
		wratio = 1;
	}

	void update(float ex, float why, float wrat, float hrat) {
		if (x != ex) x = ex;
		if (y != why) y = why;
		if (wratio != wrat) wratio = wrat;
		if (hratio != hrat) hratio = hrat;
	}

	void draw(FloatList outs, float radius) {
		radius -= 4 * wratio;
		outliers = outs;
		plot(radius);
	}
	
	void draw(FloatList outs) {
		draw(outs, w / 2);
	}

	void plot(float radius) {
		if (range() > 0) {
			float r = radius;
			display(wratio, hratio, outliers, radius);



			g.strokeWeight(1);
			if (outliers.size() != 0) {
				g.textFont(font, (fontsize+1)*hratio);
				g.textAlign(CENTER, CENTER);
				g.fill(0);
				outliers.sort();

				for (int i = 0; i < outliers.size(); i++) {
					g.textFont(font, fontsize+5);
					g.text("*", remap(outliers.get(i), x, r), y);
				}
			}

			//println("A1 Q0: " + q0 + " Q1: " + q1 + " Q2: "  + q2 + " Q3: " + q3 + " Q4: " + q4 + " R: " + r);


			//for (int i = 0; i < iqrs.length; i++) print("Q" + (i) + ": " + iqrs[i] + " ");

			//println("");
			//println("R: " + r + " X: " + x + " MouseX: " + mouseX);
			box();
		}
	}

	void box() {
		float shrink = .001f;
		float [] iqrs = {q0, q1, q2, q3, q4};

		for (int i = 0; i < iqrs.length; i++)
			iqrs[i] *= shrink;
		//for (int i = 0; i < iqrs.length; i++) print("Q" + (i) + ": " + iqrs[i] + " ");
		//println("");

		for (int i = 0; i < iqrs.length; i++) iqrs[i] = remap(iqrs[i], shrink, x, r);
		// This was only necessary because my original algorithm for lowest/highest sucked
		//if (outliers.size() == 0)  for (int i = 0; i < iqrs.length; i++) iqrs[i] = remap(iqrs[i], shrink, x, r);
		//else                       for (int i = 0; i < iqrs.length; i++) iqrs[i] = remap(iqrs[i], shrink, x, r);

		g.noFill();
		g.stroke(90);
		g.textAlign(CENTER, CENTER);
		g.rectMode(CORNERS);

		//surrounding box
		g.strokeWeight(1);
		g.rect(x- r - 5, y - 9, x + r + 5, y + 22);
		g.line(x-r, y+7, x + r, y+7); //the line underneath

		//verticals
		g.stroke(0);
		g.line(iqrs[0], y-4, iqrs[0], y+4); //q0 line
		g.line(iqrs[2], y-4, iqrs[2], y+4); //median line
		g.line(iqrs[4], y-4, iqrs[4], y+4); //q4 line

		//box
		g.strokeWeight(2);
		g.rect(iqrs[1], y-4, iqrs[3], y + 4);

		//horizontals
		g.strokeWeight(1);
		g.line(iqrs[0], y, iqrs[1], y); //lower whisker
		g.line(iqrs[3], y, iqrs[4], y); //higher whisker
	}
}