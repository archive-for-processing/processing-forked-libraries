package funGUI;
import processing.core.*;

/**
 * The ChoiceList inherits from the OptionList class. Instead of making up a list of options,
 * only one of which can be chose, the ChoiceList makes a list of check boxes, each of which
 * can be checked or left unchecked.
 * @author audrey
 *
 */
public class ChoiceList extends OptionList {
	
	/**
	 * The constructor for the ChoiceList
	 * @param p					PApplet, the parent of the sketch (usually "this")
	 * @param ex				float, the x-coordinate of the list's upper left-hand corner
	 * @param why				float, the y-coordinate of the list's upper left-hand corner
	 * @param numOptions		int, the number of options being given
	 * @param optionLabels		String [], a list of the names of the options to be displayed
	 * @param t					String, the type of things being displayed, such as "food"
	 * @param d					Display, the display that will hold these things
	 * @param numColumns		int, the number of columns in the list
	 */
	public ChoiceList(PApplet p, float ex, float why, int numOptions, String [] optionLabels, String t, Display d, int numColumns) {
		super(p, ex, why, numOptions, optionLabels, t, d, numColumns);
	}
	
	
	@Override
	protected void init(float ex, float why, String [] optionLabels, Display d, int numColumns) {
		for (int i = 0; i < optionLabels.length; i++) {
			options[i] = new Choice(p, ex, why + i * 20, optionLabels[i], d, numColumns);
		}
	}
	
	@Override
	protected void mutuallyExclusive(int i) {
		// Hi there, obsolete code...
	}
	
	
}
