package me.max.chartly;

import java.awt.Font;

import processing.core.PFont;

/**
 * Restores original processing settings (font, stroke, and fill)
 * after drawing the graph.
 *
 *@author Max Johnson
 */
public class Cleaner {
	private Font font;
	private int stroke, fill;
	
	public Cleaner() {
		this.load();
	}
	
	public void load() {
		this.font = Chartly.app.getFont();
		this.stroke = Chartly.app.g.strokeColor;
		this.fill = Chartly.app.g.fillColor;
	}
	
	public void restore() {
		if (font != null) {
			Chartly.app.textFont(new PFont(font, true));
		}
		
		Chartly.app.stroke(stroke);
		Chartly.app.fill(fill);
	}
}
