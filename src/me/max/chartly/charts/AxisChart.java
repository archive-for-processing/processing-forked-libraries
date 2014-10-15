package me.max.chartly.charts;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import me.max.chartly.Chartly;
import me.max.chartly.components.data.DataPair;

public abstract class AxisChart implements Chart {

	public void drawAxis(float x, float y, float dx, float dy, float w, float yend, float yincr, ArrayList<DataPair> data) {

		Chartly.app.stroke(this.getColorScheme().getAxisColor());
		Chartly.app.fill(this.getColorScheme().getAxisColor());

		// X Axis
		Chartly.app.rect(x, y, dx, w);

		// Labels
		int xcount = 0;
		for (DataPair pair : data) {
			Chartly.app.textAlign(PConstants.CENTER);
			Chartly.app.text(pair.label, x + getTextDistanceFactor(xcount, dx, data), y + 20);
			xcount++;
		}

		// Y Axis
		Chartly.app.rect(x, y, w, -dy);

		// Labels
		int ycount = 0;
		float pxincr = incrToPixels(yincr, 0, yend, dy);
		for (int i = 0; i <= dy; i+=pxincr) {
			Chartly.app.textAlign(PConstants.RIGHT);
			Chartly.app.text(Chartly.trimNumber(yincr * ycount), x - 3, y - (pxincr * ycount) + Chartly.app.getFont().getSize()/2);
			ycount++;
		}
	}
	
	protected float getHeightFactor(float value, float yend, float dy) {
		return PApplet.map(value, 0, yend, 0, dy);
	}
	
	private float incrToPixels(float incr, float ystart, float yend, float dy) {
		return PApplet.map(incr, ystart, yend, 0, dy);
	}
	
	private float getTextDistanceFactor(int count, float dx, ArrayList<DataPair> data) {
		float w = (float) (dx/(1.5 * data.size() + .5)); //Math done on whiteboard.
		return (float) (w + w * 1.5 * (count));
	}

}
