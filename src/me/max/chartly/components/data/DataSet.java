package me.max.chartly.components.data;

import java.util.ArrayList;

import me.max.chartly.charts.BarChart;
import me.max.chartly.charts.PieChart;
import me.max.chartly.exceptions.KeyValueAmountException;

public class DataSet {
	
	public ArrayList<DataPair> data;
	
	public DataSet() {
		this.data = new ArrayList<DataPair>();
	}
	
	public DataSet(String[] keys, Float[] values) {
		this.data = new ArrayList<DataPair>();
		try {
			for (int i = 0; i < keys.length; i++) {
				data.add(new DataPair(keys[i], values[i]));
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			try {
				throw new KeyValueAmountException("Check your DataSet(s)!", aioobe);
			} catch (KeyValueAmountException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setData(String[] keys, Float[] values) throws KeyValueAmountException {
		data.clear();
		try {
			for (int i = 0; i < keys.length; i++) {
				data.add(new DataPair(keys[i], values[i]));
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new KeyValueAmountException("Check your DataSet(s)!", aioobe);
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
