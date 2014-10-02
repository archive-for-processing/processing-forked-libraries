package me.max.chartly.charts;

import java.util.HashMap;

import me.max.chartly.Chartly;
import processing.core.PApplet;
import processing.core.PFont;

public class BarChart extends AxisChart {
	
	private HashMap<String, Float> data;
	private String[] ylabels;
	private float x,y,dx,dy;
	private PFont font;
	private ColorScheme colorScheme;
	
	public BarChart(float x, float y, float dx, float dy) {
		data = new HashMap<String, Float>();
		font = Chartly.app.createFont("Helvetica", 12);
		colorScheme = ColorScheme.getDefaultColorScheme();
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		ylabels = new String[]{"0", "10", "20", "30", "40", "50"};
	}
	
	@Override
	public void draw(float x, float y) {
		Chartly.app.textFont(font);
		
		float w = (float) (dx/(1.5 * data.keySet().size() + .5)); //Math done on whiteboard.
		int count = 0;
		Float[] valueArray = data.values().toArray(new Float[0]);
		Chartly.quickSort(valueArray, 0, valueArray.length-1);
		for (String label : data.keySet()) {
			int c = colorScheme.next();
			Chartly.app.stroke(c);
			Chartly.app.fill(c);
			Chartly.app.rect(
					(float) (x + .5 * w + w * 1.5 * (count)), y, 
					w, -1 * getHeightFactor(valueArray,data.get(label)) // Reverse the Y
			);
			count++;
		}
		// count := 0, f(c) = w, f(0) = x - .5w; f(1) = x + w + 1.5(
		this.drawAxis(x, y, dx, dy, 2F, data.keySet().toArray(new String[0]), ylabels);
	}
	
	public float getHeightFactor(Float[] values, float value) {
		return dy * (value/values[values.length-1]);
	}
	
	public void setYLabels(String... labels) {
		this.ylabels = labels;
	}
	
	public void resize(float dx, float dy) {
		this.dx = dx == 0 ? this.dx : dx;
		this.dy = dy == 0 ? this.dy : dy;
	}
	
	@Override
	public void refresh() {
		draw(x,y);
	}

	@Override
	public void setData(String[] keys, Float[] values) {
		if (keys.length != values.length) {
			PApplet.println("CHARTLY ERROR: UNEQUAL AMOUNTS OF KEYS AND DATA PROVIDED!");
			return;
		}
		data.clear();
		for (int i = 0; i < keys.length; i++) {
			data.put(keys[i], values[i]);
		}
	}

	@Override
	public HashMap<String, Float> getData() {
		return data;
	}

	@Override
	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	@Override
	public void setColorScheme(ColorScheme scheme) {
		this.colorScheme = scheme;
	}

}
