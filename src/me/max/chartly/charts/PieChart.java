package me.max.chartly.charts;

import java.util.Iterator;

import me.max.chartly.Chartly;
import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataPair;
import me.max.chartly.components.data.DataSet;
import processing.core.PConstants;
import processing.core.PFont;

public class PieChart implements Chart {
	
	private DataSet data;
	private float x,y,radius;
	private PFont font;
	private ColorScheme colorScheme;
	
	public PieChart(float radius) {
		data = new DataSet();
		
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
		Iterator<DataPair> it = data.getData().iterator();	
		
		while(it.hasNext()) {
			//Chart
			DataPair current = it.next();
			float dr = Chartly.percentToRadians(current.value);
			
			Chartly.app.stroke(colorScheme.getAxisColor());
			Chartly.app.fill(colorScheme.next());
			
			Chartly.app.arc(x, y, radius * 2, radius * 2, r, r+dr);
			
			//Labeling
			double tx = (1.1 * radius) * Math.cos(r + dr/2); 
			double ty = (1.1 * radius) * Math.sin(r + dr/2);
			
			Chartly.app.pushMatrix();
			Chartly.app.translate((float) tx, (float) ty);
			Chartly.app.textAlign(tx+x > x ? PConstants.LEFT : PConstants.RIGHT);
			Chartly.app.text(current + " " + Chartly.trimNumber(current.value) + "%", x, y);
			Chartly.app.popMatrix();

			//incrementation
			r+=dr;			
		}
	}
	
	@Override
	public void refresh() {
		draw();
	}

	@Override
	public PieChart setData(DataSet data) {
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
	public PieChart setColorScheme(ColorScheme scheme) {
		this.colorScheme = scheme;
		return this;
	}
	
	

}
