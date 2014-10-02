package me.max.chartly.charts;

import processing.core.PConstants;
import me.max.chartly.Chartly;

public abstract class AxisChart implements Chart {

	public void drawAxis(float x, float y, float dx, float dy, float w, String[] xlabels, String[] ylabels) {

		Chartly.app.stroke(this.getColorScheme().getAxisColor());
		Chartly.app.fill(this.getColorScheme().getAxisColor());

		// X Axis
		Chartly.app.rect(x, y, dx, w);
		Chartly.app.beginShape();
		Chartly.app.vertex((float) (x + (1.1 * dx)), (float) (y + (.5 * w)));
		Chartly.app.vertex(x + dx, y - w);
		Chartly.app.vertex(x + dx, (float) (y + (1.5 * w)));
		Chartly.app.endShape();

		// Labels
		float xincr = dx / xlabels.length;
		int xcount = 0;
		for (String label : xlabels) {
			Chartly.app.textAlign(PConstants.RIGHT);
			Chartly.app.text(label, x + (xcount * xincr), y + 20);
			xcount++;
		}

		// Y Axis
		Chartly.app.rect(x, y, w, -dy);
		Chartly.app.beginShape();
		Chartly.app.vertex((float) x - w / 2, (float) (y - (1.1 * dy)));
		Chartly.app.vertex(x - w, y - dy);
		Chartly.app.vertex((float) (x - (1.5 * w)), y - dy);
		Chartly.app.endShape();

		// Labels
		float yincr = dy / ylabels.length;
		int ycount = 0;
		for (String label : ylabels) {
			Chartly.app.textAlign(PConstants.RIGHT);
			Chartly.app.text(label, x - 3, y - (ycount * yincr));
			ycount++;
		}
	}

}
