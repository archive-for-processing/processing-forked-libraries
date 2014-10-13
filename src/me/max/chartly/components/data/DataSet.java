package me.max.chartly.components.data;

import java.util.ArrayList;

import me.max.chartly.charts.BarChart;
import me.max.chartly.charts.PieChart;
import processing.core.PApplet;

public class DataSet {
	
	public ArrayList<DataPair> data;
	
	public DataSet() {
		this.data = new ArrayList<DataPair>();
	}
	
	public DataSet(String[] keys, Float[] values) {
		this.data = new ArrayList<DataPair>();

		if (keys.length != values.length) {
			PApplet.println("CHARTLY ERROR: UNEQUAL AMOUNTS OF KEYS AND DATA PROVIDED!");
		}
		data.clear();
		for (int i = 0; i < keys.length; i++) {
			data.add(new DataPair(keys[i], values[i]));
		}
	}
	
	public void setData(String[] keys, Float[] values) {
		if (keys.length != values.length) {
			PApplet.println("CHARTLY ERROR: UNEQUAL AMOUNTS OF KEYS AND DATA PROVIDED!");
		}
		data.clear();
		for (int i = 0; i < keys.length; i++) {
			data.add(new DataPair(keys[i], values[i]));
		}
	}
	
	public ArrayList<DataPair> getData() {
		return data;
	}
	
	public BarChart toBarChart(float x, float y, float dx, float dy) {
		return new BarChart(dx, dy).setData(this);
	}
	
	public PieChart toPieChart(float r) {
		return new PieChart(r).setData(this);
	}
	
}
