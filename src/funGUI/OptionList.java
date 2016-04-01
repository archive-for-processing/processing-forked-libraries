package funGUI;

import processing.core.PApplet;

public class OptionList extends List {
	protected int selectedOption = 0;
	String selectedLabel = "";
	String type;
//	PApplet p;
	public OptionList(PApplet p, float ex, float why, int numOptions, String [] optionLabels, String t, Display d, int numColumns) {
		super(p, ex, why, numOptions, optionLabels, t, d, numColumns);
		type = t;
		init(ex, why, optionLabels, d, numColumns);
	}
	
	protected void init(float ex, float why, String [] optionLabels, Display d, int numColumns) {
		options = new Option [optionLabels.length];
		for (int i = 0; i < optionLabels.length; i++) {
			options[i] = new Option(p, ex, why + i * 20, optionLabels[i], d, numColumns);
		}
	}

	public void draw() {
		limitwidth();
		for (int i = 0; i < options.length; i++) {
			options[i].display();
			mutuallyExclusive(i);
		}
	}
	
	protected void mutuallyExclusive(int i) {
		if (p.mousePressed) {
			if (options[i].selected) {
				if (i != selectedOption) {
					options[selectedOption].selected = false;
					selectedOption = i;
					selectedLabel = options[i].label;
				}
			}
		}
	}

	void limitwidth() {
		float range = options[0].r2 * 2;
		if (options[0].y - range < options[1].y + options[1].r2 || options[1].y + range > options[0].y - options[0].r2) {
			float newwidth = PApplet.abs(options[0].y - options[1].y) - options[1].hratio * 3;
			newwidth *= .5;
			for (int i = 0; i < options.length; i++) {
				options[i].r1 = newwidth;
				options[i].r2 = (float) (newwidth  * 1.5);
			}
		}
	}

	boolean greaterThanZero(int i) {
		return(i > 0);
	}

	String str() {
		return(selectedLabel + type);
	}
}
