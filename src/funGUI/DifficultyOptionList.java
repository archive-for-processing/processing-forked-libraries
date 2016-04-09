package funGUI;

import processing.core.PApplet;

public class DifficultyOptionList extends OptionList {
	public static final String [] levels0 = {
			"Easy", "Medium", "Hard"
	};
	public static final String [] levels1 = {
			"Easy", "Medium", "Difficult", "Hard"
	};
	
	public static final String [] levels2 = {
			"Easy", "Intermediate", "Medium", "Difficult", "Hard"
	};
	
	public DifficultyOptionList(PApplet p, float ex, float why, Display d) {
		super(p, ex, why, levels0.length, levels0, "", d, 1);
	}
	
	public DifficultyOptionList(PApplet p, float ex, float why, Display d, int level) {
		super(p, ex, why, (level == 1) ? levels0.length : (level == 2) ? levels1.length : levels2.length, (level == 1) ? levels0 : (level == 2) ? levels1 : levels2, "", d, 1);
	}
	
	
	public DifficultyOptionList(PApplet p, float ex, float why, int numOptions, String [] optionLabels, String t, Display d, int numColumns) {
		super(p, ex, why, numOptions, optionLabels, t, d, numColumns);
	}
	
	public int difficultyRating() {
		int n = 0;
		switch(options[selectedOption].label) {
		case "Easy":
			n = 1;
			break;
		case "Intermediate":
			n = 2;
			break;
		case "Medium":
			n = 3;
			break;
		case "Difficult":
			n = 4;
			break;
		case "Hard":
			n = 5;
			break;
		default:
			n = -1;
			break;
		}
		return(n);
	}
}
