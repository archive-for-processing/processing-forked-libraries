package me.max.chartly.charts;

import me.max.chartly.Chartly;
import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import processing.core.PApplet;
import processing.core.PFont;

public class BarChart extends AxisChart {
	
	private DataSet data;
	private float x,y,dx,dy,yend,yincr;
	private PFont font;
	private ColorScheme colorScheme;
	
	public BarChart(float dx, float dy) {
		data = new DataSet();
		font = Chartly.app.createFont("Helvetica", 12);
		colorScheme = ColorScheme.getDefaultColorScheme();
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public void draw(float x, float y) {
		this.x = x;
		this.y = y;
		
		Chartly.app.textFont(font);
		
		float w = (float) (dx/(1.5 * data.getData().size() + .5)); //Math done on whiteboard.
		int count = 0;
		for (DataPair pair : data.getData()) {
			int c = colorScheme.next();
			Chartly.app.stroke(c);
			Chartly.app.fill(c);
			Chartly.app.rect(
					(float) (x + w * (.5 + 1.5 * count)), y, 
					w, -1 * getHeightFactor(pair.value) // Reverse the Y
			);
			count++;
		}
		
		this.drawAxis(x, y, dx, dy, 2F,  yend, yincr, data.getData());
	}
	
	public float getHeightFactor(float value) {
		return PApplet.map(value, 0, yend, 0, dy);
	}
	
	public BarChart setYLabels(float end, float incr) {
		this.yend = end;
		this.yincr = incr;
		return this;
	}
	
	public BarChart resize(float dx, float dy) {
		this.dx = dx == 0 ? this.dx : dx;
		this.dy = dy == 0 ? this.dy : dy;
		return this;
	}
	
	@Override
	public void refresh() {
		draw(x,y);
	}

	public BarChart setData(DataSet data) {
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
	public BarChart setColorScheme(ColorScheme scheme) {
		this.colorScheme = scheme;
		return this;
	}

}
