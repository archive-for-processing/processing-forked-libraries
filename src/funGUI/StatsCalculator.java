package funGUI;
import processing.data.*;

class StatsCalculator implements Stats {
	public static enum DataType {
		FLOAT,
		INT;
	}
	FloatList floats;
	FloatList unsortedFloats;
	IntList ints;
	DataType t;

	public StatsCalculator(DataType t) {
		this.t = t;
		switch(t) {
		case FLOAT:
			floats = new FloatList();
			unsortedFloats = new FloatList();
			break;
		case INT:
			ints = new IntList();
			break;
		default:
			break;
		}
	}
	
	public void add(int i) {
		ints.append(i);
		ints.sort();
	}
	
	public void add(float f) {
		floats.append(f);
		unsortedFloats.append(f);
		floats.sort();
	}
	
	public int size() {
		return(floats.size());
	}

	@Override
	public float mean() {
		float total = 0;
		for (int i = 0; i < floats.size(); i++) {
			total += floats.get(i);
		}
		return(total / (float)(floats.size()));
	}

	@Override
	public float median() {
		return(q2());
	}

	@Override
	public float mode() {
		// Do I even need this?
		return 0;
	}

	@Override
	public float stdDev() {
		return((float) (Math.sqrt(variance())));
	}

	@Override
	public float variance() {
		float total = 0;
		float mean = mean();
		for (int i = 0; i < floats.size(); i++) {
			total += Math.pow(floats.get(i) - mean, 2);
		}
		total /= (float) (floats.size());
		return(total);
	}

	@Override
	public float q0() {
		FloatList outs = outliers();
		if (outs != null) {
			outs.sort();
			for (int i = outs.size() - 1; i > -1; i--) {
				if (outs.get(i) > q2()) {
					outs.remove(i);
					i++;
				} else if (outs.get(i) < q1() - outlierLimit()) {
					break;
				}
			}
			int numOuts = outs.size();
			return(floats.get(floats.size() - 1 - numOuts));
		}
		return(min());
	}

	@Override
	public float q1() {
		float pos = 0.25f;
		int position = (int) (pos * floats.size());
		float q1 = 0;
		if (floats.size() % 4 == 0) {
			q1 = (floats.get(position) + floats.get(position + 1)) / 2.0f;
		} else {
			q1 = floats.get(position);
		}
		return(q1);
	}

	@Override
	public float q2() {
		int medianIndex = (int) (floats.size() / 2.0);
		float median = 0;
		if (floats.size() % 2 == 0) {
			median = (floats.get(medianIndex) + floats.get(medianIndex + 1)) / 2.0f;
		} else {
			median = floats.get(medianIndex);
		}
		return(median);
	}

	@Override
	public float q3() {
		float pos = 0.75f;
		int position = (int) (pos * floats.size());
		float q3 = 0;
		if (floats.size() % 4 == 0) {
			q3 = (floats.get(position) + floats.get(position + 1)) / 2.0f;
		} else {
			q3 = floats.get(position);
		}
		return(q3);
	}

	@Override
	public float q4() {
		FloatList outs = outliers();
		if (outs != null) {
			outs.sort();
			for (int i = 0; i < outs.size(); i++) {
				if (outs.get(i) < q2()) {
					outs.remove(i);
					i--;
				} else if (outs.get(i) > q3() + outlierLimit()) {
					break;
				}
			}
			int numOutliers = outs.size();
			return(floats.get(floats.size() - 1 - numOutliers));
		}
		return(max());
	}
	
	

	@Override
	public float iqr() {
		return(q3() - q1());
	}

	@Override
	public boolean outlier(int index) {
		if (index < q1()) {
			return(floats.get(index) < q1() - outlierLimit());
		}
		return(floats.get(index) > q3() + outlierLimit());
	}

	@Override
	public float outlierLimit() {
		return(iqr() * 1.5f);
	}

	@Override
	public FloatList outliers() throws NullPointerException {
		FloatList outs = new FloatList();
		for (int i = 0; i < floats.size() * .25; i++) {
			if (outlier(i)) {
				outs.append(floats.get(i));
			}
		}
		for (int i = (int) (floats.size() * .75); i < floats.size(); i++) {
			if (outlier(i)) {
				outs.append(floats.get(i));
			}
		}
		if (outs.size() != 0) return(outs);
		return null;
	}

	@Override
	public float max() {
		return(floats.max());
	}

	@Override
	public float min() {
		return(floats.min());
	}

	@Override
	public float range() {
		return(max() - min());
	}
	
	
}
