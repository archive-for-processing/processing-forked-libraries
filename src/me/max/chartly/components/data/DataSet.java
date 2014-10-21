package me.max.chartly.components.data;

import java.util.ArrayList;

import me.max.chartly.charts.BarChart;
import me.max.chartly.charts.LineChart;
import me.max.chartly.charts.PieChart;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.KeyValueAmountException;

/**
 * Used to contain sets of data to be given to charts
 * @author maxjohnson
 */
public class DataSet {
	
	public ArrayList<DataPair> data;
	
	public DataSet() {
		this.data = new ArrayList<DataPair>();
	}
	
	public DataSet(String[] keys, Float[] values) {
		this.data = new ArrayList<DataPair>();
		if (keys.length != values.length) {
			try {
				throw new KeyValueAmountException("Unequal amounts of keys and values!");
			} catch (KeyValueAmountException ex) {
				ExceptionWriter.write(ex);
			}
		}
		for (int i = 0; i < keys.length; i++) {
			data.add(new DataPair(keys[i], values[i]));
		}
	}
	
	public void setData(String[] keys, Float[] values) {
		data.clear();
		if (keys.length != values.length) {
			try {
				throw new KeyValueAmountException("Unequal amounts of keys and values!");
			} catch (KeyValueAmountException ex) {
				ExceptionWriter.write(ex);
			}
		}
		for (int i = 0; i < keys.length; i++) {
			data.add(new DataPair(keys[i], values[i]));
		}
	}
	
	public ArrayList<DataPair> getData() {
		return data;
	}
	
	public LineChart toLineChart(float dx, float dy) {
		return new LineChart(dx, dy).setData(this);
	}
	
	public BarChart toBarChart(float dx, float dy) {
		return new BarChart(dx, dy).setData(this);
	}
	
	public PieChart toPieChart(float r) {
		return new PieChart(r).setData(this);
	}
	
	public float getTotal() {
		float total = 0;
		for (DataPair pair : data) {
			total += pair.value;
		}
		return total;
	}
}
