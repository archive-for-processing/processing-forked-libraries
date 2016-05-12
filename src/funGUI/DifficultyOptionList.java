package funGUI;

import processing.core.PApplet;

/**
 * The DifficultyOptionList is a class specifically
 * for displaying differing grades of difficulty. A
 * game, for example, might have the modes Easy,
 * Medium, or Hard. There are several different ones
 * to choose from. Opting for different difficulty
 * choices can be done in the constructor.
 * The chocies are displayed to the user in a small
 * list.
 * @author audrey
 *
 */
public class DifficultyOptionList extends OptionList {
	public static enum Grades {
		THREE,
		FOUR,
		FIVE;
	}
	public static final String [] levels0 = {
			"Easy", "Medium", "Hard"
	};
	public static final String [] levels1 = {
			"Easy", "Medium", "Difficult", "Hard"
	};
	
	public static final String [] levels2 = {
			"Easy", "Intermediate", "Medium", "Difficult", "Hard"
	};
	
	/**
	 * Default constructor. You get a list with three chocies: Easy, Medium, or Hard.
	 * @param p			PApplet, the parent of the sketch
	 * @param ex		float, the x-coordinate of the list's upper left-hand corner
	 * @param why		float, the y-coordinate of the list's upper left-hand corner
	 * @param d			Display, the display to contain the list within
	 */
	public DifficultyOptionList(PApplet p, float ex, float why, Display d) {
		super(p, ex, why, levels0.length, levels0, "", d, 1);
	}
	
	/**
	 * Constructor. Has options to choose between 3, 4, or 5 levels of difficulty.
	 * @param p			PApplet, the parent of the sketch
	 * @param ex		float, the x-coordinate of the list's upper left-hand corner
	 * @param why		float, the y-coordinate of the list's upper left-hand corner
	 * @param d			Display, the display to contain the list within
	 * @param level		int, a number from 1, you get the basic 3 grades of difficulty, to 3 –> 5 total levels of difficulty
	 */
	public DifficultyOptionList(PApplet p, float ex, float why, Display d, int level) {
		super(p, ex, why, (level == 1) ? levels0.length : (level == 2) ? levels1.length : levels2.length, (level == 1) ? levels0 : (level == 2) ? levels1 : levels2, "", d, 1);
	}
	
	/**
	 * Constructor.
	 * @param p			PApplet, the parent of the sketch
	 * @param ex		float, the x-coordinate of the list's upper left-hand corner
	 * @param why		float, the y-coordinate of the list's upper left-hand corner
	 * @param numOptions
	 * @param optionLabels
	 * @param t
	 * @param d			Display, the display to contain the list within
	 * @param numColumns
	 */
	public DifficultyOptionList(PApplet p, float ex, float why, int numOptions, String [] optionLabels, String t, Display d, int numColumns) {
		super(p, ex, why, numOptions, optionLabels, t, d, numColumns);
	}
	
	public int difficultyRating() {
		int n = 0;
		switch(options[selectedOption].label.charAt(0)) {
		case 'E':
			n = 1;
			break;
		case 'I':
			n = 2;
			break;
		case 'M':
			n = 3;
			break;
		case 'D':
			n = 4;
			break;
		case 'H':
			n = 5;
			break;
		default:
			n = -1;
			break;
		}
		return(n);
	}
}
