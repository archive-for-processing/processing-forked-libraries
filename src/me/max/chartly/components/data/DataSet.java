package me.max.chartly.components.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.max.chartly.DataUtils;
import me.max.chartly.charts.BarChart;
import me.max.chartly.charts.LineChart;
import me.max.chartly.charts.PieChart;
import me.max.chartly.exceptions.ExceptionWriter;
import me.max.chartly.exceptions.KeyValueAmountException;

/**
 * Used to contain sets of data to be given to charts
 * 
 * @author maxjohnson
 */
public class DataSet {
	public ArrayList<DataPair> data;

	public DataSet() {
		this.data = new ArrayList<DataPair>();
	}

	public DataSet(String[] keys, Float[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, values);
	}

	public DataSet(String[] keys, float[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, DataUtils.primitiveToObjectFloatArray(values));
	}

	public DataSet(String[] keys, int[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, DataUtils.intToFloatArray(values));
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

	// Not sure if needed
	public void setData(String[] keys, float[] values) {
		setData(keys, DataUtils.primitiveToObjectFloatArray(values));
	}

	// Not sure if needed
	public void setData(String[] keys, int[] values) {
		setData(keys, DataUtils.intToFloatArray(values));
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

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void sortByHighest() {
		sort(true);
	}

	public void sortByLowest() {
		sort(false);
	}
	
	private void sort(boolean highest) {
		DataPair[] pairs = data.toArray(new DataPair[0]);
		qsort(pairs, 0, pairs.length-1, highest);
		this.data = new ArrayList<DataPair>(Arrays.asList(pairs));
	}

	// Quicksort implementation
	private void qsort(DataPair[] pairs, int low, int high, boolean lowestFirst) {
		int j = low;
		int i = high;
		DataPair pivot = pairs[(int) pairs.length/2];
		
		boolean cont;
		while (j <= i) {

			cont = lowestFirst ? (pairs[j].value < pivot.value) : (pairs[j].value > pivot.value);
			while (cont) {
				j++;
				cont = lowestFirst ? (pairs[j].value < pivot.value) : (pairs[j].value > pivot.value);
			}

			cont = lowestFirst ? (pairs[i].value > pivot.value) : (pairs[i].value < pivot.value);
			while (cont) {
				i--;
				cont = lowestFirst ? (pairs[i].value > pivot.value) : (pairs[i].value < pivot.value);
			}

			if (j <= i) {
				DataPair temp = pairs[i];
				pairs[i] = pairs[j];
				pairs[j] = temp;
				i--;
				j++;
			}
		}

		if (low < i) {
			qsort(pairs, low, i, lowestFirst);
		}
		if (high > j) {
			qsort(pairs, j, high, lowestFirst);
		}
	}

	public void randomizeOrder() {
		Collections.shuffle(data);
	}
	
}
