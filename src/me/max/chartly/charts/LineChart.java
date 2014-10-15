package me.max.chartly.charts;

import processing.core.PFont;
import me.max.chartly.Chartly;
import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;

public class LineChart extends AxisChart {
	
	private DataSet data;
	private float x,y,dx,dy,yend,yincr;
	private PFont font;
	private ColorScheme colorScheme;
	
	public LineChart(float dx, float dy) {
		data = new DataSet();
		font = Chartly.app.createFont("Helvetica", 12);
		colorScheme = ColorScheme.getDefaultColorScheme();
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public void draw(float x, float y) {
		
		this.drawAxis(x, y, dx, dy, 2F, yend, yincr, data.getData());
		
		this.x = x;
		this.y = y;
		
		Chartly.app.textFont(font);
		
		float w = (float) (dx/(1.5 * data.getData().size() + .5)); //Math done on whiteboard.
		int count = 0;
		float[] previous = new float[]{Float.MAX_VALUE, Float.MIN_VALUE}; //placeholders
		for (DataPair pair : data.getData()) {
			float xFactor =  (float) (x + w * (1 + 1.5 * count));
			float yFactor = y + -1 * this.getHeightFactor(pair.value, yend, dy);
			int c = colorScheme.next();
			
			Chartly.app.stroke(c);
			Chartly.app.fill(c);
			Chartly.app.strokeWeight(2);
			Chartly.app.ellipse(xFactor, yFactor, 3, 3);
			
			if (previous[0] != Integer.MAX_VALUE && previous[1] != Integer.MIN_VALUE) {
				Chartly.app.line(previous[0], previous[1], xFactor, yFactor);
			}
			
			previous[0] = xFactor;
			previous[1] = yFactor;
			count++;
		}
	}

	@Override
	public void refresh() {
		draw(x,y);	
	}
	
	public LineChart setYLabels(float end, float incr) {
		this.yend = end;
		this.yincr = incr;
		return this;
	}

	@Override
	public LineChart setData(DataSet data) {
		this.data = data;
		return this;
	}

	@Override
	public DataSet getData() {
		return data;
	}

	@Override
	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	@Override
	public LineChart setColorScheme(ColorScheme scheme) {
		this.colorScheme = scheme;
		return this;
	}

}
