package me.max.chartly.charts;

import java.util.HashMap;

public interface Chart {

	public void draw(float x, float y);
	public void refresh();
	
	public void setData(String[] keys, Float[] values);
	public HashMap<String, Double> getData();
	
}
