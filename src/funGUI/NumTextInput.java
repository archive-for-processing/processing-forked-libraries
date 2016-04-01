package funGUI;
import processing.core.*;

/**
 * Really just a TextStrip that only allows numerical inputs such
 * as 10E-2, otherwise known as 10 times 10 to the power of 2, or 3.14,
 * etc.
 * @author Audrey Seo
 */
public class NumTextInput extends TextStrip implements PConstants {
	boolean scientific = true;
	/**
	 * Constructor of the numerics-only text strip class.
	 * @param p		PApplet, the parent sketch, usually "this"
	 * @param x		float, the x-coordinate of the number input box
	 * @param y		float, the y-coordinate of the number input box
	 * @param w		float, the width of the number input box
	 * @param h		float, the height of the number input box
	 */
	public NumTextInput(PApplet p, float x, float y, float w, float h) {
		super(p, x, y, w, h);
	}
	
	/**
	 * Constructor of the numerics-only text strip class.
	 * @param p		PApplet, the parent sketch, usually "this"
	 * @param x		float, the x-coordinate of the number input box
	 * @param y		float, the y-coordinate of the number input box
	 * @param w		float, the width of the number input box
	 * @param h		float, the height of the number input box
	 * @param name	String, the instructions for the user that will be to the left of the number input box, e.g. "Birthdate: "
	 */
	public NumTextInput(PApplet p, float x, float y, float w, float h,
			String name) {
		super(p, x, y, w, h, name);
	}
	
	/**
	 * Constructor of the numerics-only version of text strip class.
	 * @param p				PApplet, the parent sketch, usually "this"
	 * @param x				float, the x-coordinate of the number input box
	 * @param y				float, the y-coordinate of the number input box
	 * @param w				float, the width of the number input box
	 * @param h				float, the height of the number input box
	 * @param scientific	boolean, whether or not this input box allows scientific notation in the form of "1x10^exponent"
	 */
	public NumTextInput(PApplet p, float x, float y, float w, float h, boolean scientific) {
		super(p, x, y, w, h);
		this.scientific = scientific;
	}
	
	@Override
	protected boolean addingConditions() {
		boolean notCoded = p.key != CODED;
		boolean notDeleting = p.key != DELETE && p.key != BACKSPACE;
		boolean okay = !pressed;
		boolean numeric = false;
		switch(p.key) {
		case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '0':
			numeric = true;
			break;
		case '-': case '.':
			numeric = true;
			break;
		case 'e':
			numeric = (scientific) ? true : false;
			break;
		default: break;
		}
		
		return(okay && notCoded && notDeleting && numeric);
	}
	
	@Override
	protected String addition() {
		String news = "";
		switch(p.key) {
		case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '0':
			news += PApplet.str(p.key);
			break;
		case '-': case '.':
			news += PApplet.str(p.key);
			break;
		case 'e':
			if (scientific) {
				news += "x10^";
			}
			break;
		default: break;
		}
		return(news);
	}
}
