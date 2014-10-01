package me.max.chartly.charts;

import java.util.HashMap;
import java.util.Iterator;

import me.max.chartly.Chartly;
import processing.core.PConstants;

public class PieChart implements Chart {
	
	private HashMap<String, Float> data;
	private float x,y,radius;
	
	public PieChart(float radius) {
		data = new HashMap<String, Float>();
		
		//Default to center
		x = Chartly.app.width/2;
		y = Chartly.app.height/2;
		this.radius = radius;
	}
	
	public void draw() {
		draw(x,y);
	}
	
	@Override
	public void draw(float x, float y) {
		this.x = x; 
		this.y = y;
		
		float r = PConstants.PI / 2; //90 degrees	
		Iterator<String> it = data.keySet().iterator();	
		int color = 0;
		
		while(it.hasNext()) {
			String current = it.next();
			float dr = Chartly.percentToRadians(data.get(current));
			
			Chartly.app.stroke(color);
			Chartly.app.fill(color);
			
			Chartly.app.arc(x, y, radius, radius, r, r+dr);
			
			r+=dr;
			color += 255/data.keySet().size();
		}
	}

	@Override
	public void refresh() {
		draw();
	}

	@SuppressWarnings("static-access")
	@Override
	public void setData(String[] keys, Float[] values) {
		if (keys.length != values.length) {
			Chartly.app.println("CHARTLY ERROR: UNEQUAL AMOUNTS OF KEYS AND DATA PROVIDED!");
			return;
		}

		data.clear();

		for (int i = 0; i < keys.length; i++) {
			data.put(keys[i], values[i]);
		}
	}

	@Override
	public HashMap<String, Double> getData() {
		return null;
	}

}
