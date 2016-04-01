package funGUI;
import processing.data.*;
interface Stats {
	// Basic functions - mean, median, mode, standard deviation, and variance
	public float mean();
	public float median();
	public float mode();
	public float stdDev();
	public float variance();
	
	// Quartiles
	public float q0();
	public float q1();
	public float q2();
	public float q3();
	public float q4();
	public float iqr();
	public boolean outlier(int index);
	public float outlierLimit();
	
	// Weird pieces of data, if no outliers throws NullPointerException
	public FloatList outliers() throws NullPointerException;
	
	// Good overall data
	public float max();
	public float min();
	public float range();
}
