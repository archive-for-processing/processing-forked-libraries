package me.max.chartly.charts;

import java.util.HashMap;
import java.util.Iterator;

import me.max.chartly.Chartly;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class PieChart implements Chart {
	
	private HashMap<String, Float> data;
	private float x,y,radius;
	private PFont font;
	private ColorScheme colorScheme;
	
	public PieChart(float radius) {
		data = new HashMap<String, Float>();
		
		//Default to center
		x = Chartly.app.width/2;
		y = Chartly.app.height/2;
		this.radius = radius;
		
		font = Chartly.app.createFont("Helvetica", 12);
	}
	
	public void draw() {
		draw(x,y);
	}
	
	@Override
	public void draw(float x, float y) {
		
		Chartly.app.textFont(font);
		
		this.x = x; 
		this.y = y;
		
		float r = PConstants.PI / 2; //90 degrees	
		Iterator<String> it = data.keySet().iterator();	
		
		while(it.hasNext()) {
			//Chart
			String current = it.next();
			float dr = Chartly.percentToRadians(data.get(current));
			
			Chartly.app.stroke(colorScheme.getAxisColor());
			Chartly.app.fill(colorScheme.next());
			
			Chartly.app.arc(x, y, radius * 2, radius * 2, r, r+dr);
			
			//Labeling
			double tx = (1.1 * radius) * Math.cos(r + dr/2); 
			double ty = (1.1 * radius) * Math.sin(r + dr/2);
			
			Chartly.app.pushMatrix();
			Chartly.app.translate((float) tx, (float) ty);
			Chartly.app.textAlign(tx+x > x ? PConstants.LEFT : PConstants.RIGHT);
			Chartly.app.text(current + " " + data.get(current) + "%", x, y);
			Chartly.app.popMatrix();
			
//			PApplet.println("Key: " + current);
//			PApplet.println(">> Translate x: " + tx);
//			PApplet.println(">> Translate y: " + ty);
//			PApplet.println(">> Alignment: " + (tx+x > x ? PConstants.RIGHT : PConstants.LEFT));

			//incrementation
			r+=dr;			
		}
	}
	
	@Override
	public void refresh() {
		draw();
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
	public HashMap<String, Double> getData() {
		return null;
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
