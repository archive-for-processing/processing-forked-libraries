package funGUI;
import processing.core.*;
import processing.data.*;

/**
 * 
 * @author audrey
 *
 */
public class Menu extends List implements FunGUIObject {
	MenuItem [] items;
//	public float x;
//	public float y;
//	public float w;
//	public float h;
	
	/**
	 * Constructor for the class Menu, a pop-up list of items to choose from
	 * @param p				PApplet, the parent sketch, usually "this'
	 * @param ex			float, the x-coordinate of the top left corner of the menu
	 * @param why			float, the y-coordinate of the top left corner of the menu
	 * @param optionLabels	String, the names for the various menu items
	 */
	public Menu(PApplet p, float ex, float why, String [] optionLabels) {
		super(p, ex, why, optionLabels, "", new Display(ex, why, 400, 1000), 1);
		this.x = ex;
		this.y = why;
		this.h = 20 * optionLabels.length;
		
		items = new MenuItem [optionLabels.length];
		PFont font = this.p.createFont("Helvetica", 13);
		p.textFont(font);
		FloatList textSizes = new FloatList();
		for (int i = 0; i < optionLabels.length; i++) {
			textSizes.append((float) (p.g.textWidth(optionLabels[i]) * 1.2));
		}
		this.w = textSizes.max();
		for (int i = 0; i < optionLabels.length; i++) {
			items[i] = new MenuItem(p, ex, why, optionLabels[i], 1, textSizes.max(), i, this);
		}
	}
	
	/**
	 * Constructor for the class Menu, a pop-up list of items to choose from
	 * @param p				PApplet, the parent sketch, usually "this'
	 * @param ex			float, the x-coordinate of the top left corner of the menu
	 * @param why			float, the y-coordinate of the top left corner of the menu
	 * @param optionLabels	String, the names for the various menu items
	 * @param t				String, the "type" of the items, ex. "food"
	 */
	public Menu(PApplet p, float ex, float why, String [] optionLabels, String t) {
		super(p, ex, why, optionLabels, t, new Display(ex, why, 400, 1000), 1);
		this.x = ex;
		this.y = why;
		this.h = 20 * optionLabels.length;
		items = new MenuItem [optionLabels.length];
		PFont font = this.p.createFont("Helvetica", 13);
		p.textFont(font);
		FloatList textSizes = new FloatList();
		for (int i = 0; i < optionLabels.length; i++) {
			textSizes.append((float) (p.g.textWidth(optionLabels[i]) * 1.2));
		}
		this.w = textSizes.max();
		for (int i = 0; i < optionLabels.length; i++) {
			items[i] = new MenuItem(p, ex, why, optionLabels[i], 1, textSizes.max(), i, this);
		}
	}
	
	/**
	 * Constructor for the class Menu, a pop-up list of items to choose from
	 * @param p				PApplet, the parent sketch, usually "this'
	 * @param ex			float, the x-coordinate of the top left corner of the menu
	 * @param why			float, the y-coordinate of the top left corner of the menu
	 * @param optionLabels	String, the names for the various menu items
	 * @param t				String, the "type" of the items, ex. "food"
	 * @param d				Display, the display that will contain the entire list
	 * @param numColumns	int, the number of columns that this list will have
	 */
	public Menu(PApplet p, float ex, float why, String [] optionLabels, String t, Display d, int numColumns) {
		super(p, ex, why, optionLabels, t, d, numColumns);
		this.x = ex;
		this.y = why;
		this.h = 20 * optionLabels.length;
		items = new MenuItem [optionLabels.length];
		for (int i = 0; i < optionLabels.length; i++) {
			items[i] = new MenuItem(p, ex, why, optionLabels[i], numColumns, d.w, i, this);
		}
		this.w = d.w;
	}
	
	@Override
	public void draw() {
		limitwidth();
		for (int i = 0; i < options.length; i++) {
			items[i].draw();
			
			mutuallyExclusive(i);
		}
	}

	@Override
	protected void mutuallyExclusive(int i) {
		if (items[i].selected) {
			if (selectedOption < 0) {
				selectedOption = i;
			} else if (selectedOption != i) {
				items[selectedOption].selected = false;
				selectedOption = i;
			}
		}
	}
	
	public float x() {
		return(x);
	}
	
	public float y() {
		return(y);
	}
	
	public float w() {
		return(w);
	}
	
	public float h() {
		return(h);
	}
	
	public String returnName() {
		return("Menu");
	}
}
