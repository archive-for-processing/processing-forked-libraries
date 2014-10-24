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
 * @author Max Johnson
 */
public class DataSet {
	public ArrayList<DataPair> data;

	/**
	 * Initializes a new, empty DataSet
	 */
	public DataSet() {
		this.data = new ArrayList<DataPair>();
	}

	/**
	 * Initializes a new DataSet with data stored as
	 * DataPairs, with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
	public DataSet(String[] keys, Float[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, values);
	}
	
	/**
	 * Initializes a new DataSet with data stored as
	 * DataPairs, with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
	public DataSet(String[] keys, float[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, DataUtils.primitiveToObjectFloatArray(values));
	}
	
	/**
	 * Initializes a new DataSet with data stored as
	 * DataPairs, with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
	public DataSet(String[] keys, int[] values) {
		this.data = new ArrayList<DataPair>();
		this.setData(keys, DataUtils.intToFloatArray(values));
	}
	
	/**
	 * Stores data as DataPairs with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
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
	
	/**
	 * Stores data as DataPairs with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
	public void setData(String[] keys, float[] values) {
		setData(keys, DataUtils.primitiveToObjectFloatArray(values));
	}
	
	/**
	 * Stores data as DataPairs with each key at a given index
	 * being stored with the value of its respective index in the
	 * values array. Can throw an exception if there are 
	 * unequal amounts of keys and values.
	 * 
	 * @param keys The labels
	 * @param values the values of the labels (Must be in respective order)
	 */
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
	
	/**
	 * Gets the sum of all of the values in the values array
	 * @return the sum
	 */
	public float getTotal() {
		float total = 0;
		for (DataPair pair : data) {
			total += pair.value;
		}
		return total;
	}
	
	/**
	 * 
	 * @return the size of the DataPair arraylist
	 */
	public int size() {
		return data.size();
	}

	/**
	 * 
	 * @return Whether the DataPair arraylist is empty
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * Uses quicksort to sort the data by highest first.
	 */
	public void sortByHighest() {
		sort(true);
	}
	
	/**
	 * Uses quicksort to sort the data by lowest first.
	 */
	public void sortByLowest() {
		sort(false);
	}
	
	private void sort(boolean highest) {
		DataPair[] pairs = data.toArray(new DataPair[0]);
		qsort(pairs, 0, pairs.length-1, highest);
		this.data = new ArrayList<DataPair>(Arrays.asList(pairs));
	}

	/**
	 * Heavily modified quicksort based off of Lars Vogels's implementation.
	 * 
	 * Orgininal code can be found here: 
	 * - http://www.vogella.com/tutorials/JavaAlgorithmsQuicksort/article.html
	 * 
	 * Cont variable explained:
	 * - If sorting by lowest first
	 * - Then it checks whether it has found a number greater than the pivot
	 * - If so it moves on to the higher index and looks for a number less
	 * - If it finds one they are switched.
	 * - Highest first does the opposite.
	 * 
	 * @param pairs Data to quicksort
	 * @param low low-bound index
	 * @param high high-bound index
	 * @param lowestFirst Whether or not to sort by lowest first (As opposed to highest)
	 */
	private void qsort(DataPair[] pairs, int low, int high, boolean lowestFirst) {
		int j = low;
		int i = high;
		DataPair pivot = pairs[pairs.length/2];
		
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
	
	/**
	 * Randomly orders the data based on the Java Collections
	 * shuffle algorithm.
	 */
	public void randomizeOrder() {
		Collections.shuffle(data);
	}
	
}
