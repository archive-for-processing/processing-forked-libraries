package funGUI;

import processing.core.PApplet;
import processing.core.PFont;
import processing.data.FloatList;

public class ComplexDropDownMenu extends DropDownMenu {
	DropDownSubMenuItem [] submenus;
	int submenuChoice = -1;
	int stagger = 0;
	int clicked;
	int oldClick = 0;
	boolean printing = false;
	
	public ComplexDropDownMenu(PApplet p, float ex, float why, String [] optionLabels, String [][] submenus) {
		super(p, ex, why, optionLabels);
		this.items = new DropDownMenuItem [optionLabels.length - submenus.length];
		PFont font = this.p.createFont("Helvetica", 13);
		p.textFont(font);
		FloatList textSizes = new FloatList();
		for (int i = 0; i < optionLabels.length; i++) {
			textSizes.append((float) (p.g.textWidth(optionLabels[i]) * 1.2));
		}
		for (int i = 0; i < optionLabels.length - submenus.length; i++) {
			items[i] = new DropDownMenuItem(p, ex, why, optionLabels[i], 1, textSizes.max(), i, this);
		}
		
		this.submenus = new DropDownSubMenuItem [submenus.length];
		int start = optionLabels.length - submenus.length;
		stagger = start;
		for (int i = 0; i < submenus.length; i++) {
			this.submenus[i] = new DropDownSubMenuItem(p, ex, why, optionLabels[start + i], 1, textSizes.max(), start + i, this, submenus[i]);
		}
		
		w = items[0].w;
		this.g = this.p.g;
		x = ex;
		y = why;
	}

	void report(String s) {
		if (printing) {
			PApplet.println("ComplexDorpDownMenu:    " + s);
		}
	}
	
	@Override
	public void draw() {
		report("Draw");
		super.draw();
		for (int i = 0; i < submenus.length; i++) {
			if (submenus[i].selectionComplete() || dropDownSelected) {
				g.pushMatrix();
				if (!dropDownSelected) {
					g.translate(0,  -1 * (stagger + i) * 18);
				}

				if (submenus[i].m.selectedOption < 0) PApplet.println("Oh no, cannot draw.");
				submenus[i].draw(dropDownSelected);
				g.popMatrix();
//				if (submenus[i].selectionComplete() && submenus[i].m.selectedOption > 0 && submenus[i].m.selectedOption != submenuChoice && dropDownSelected) {
//					dropDownSelected = false;
//				}
			}
			mutuallyExclusive(stagger + i);
		}
		hide();
	}
	
	void hide() {
		if (p.mousePressed && !allPressed() && !clicked() && !clickedWithinBox()) {
			dropDownSelected = false;
		}
	}
	
	boolean clickedWithinBox() {
		return(p.mouseX < x + w && p.mouseX > x && p.mouseY < y + 18 && p.mouseY > y && p.mousePressed);
	}
	
	boolean allPressed() {
		boolean [] bools = new boolean [items.length + submenus.length];
		int i = 0;
		for (DropDownMenuItem item : items) {
			bools[i] = item.clicked();
			i++;
		}
		
		for (DropDownSubMenuItem item : submenus) {
			bools[i] = item.clicked();
			i++;
		}
		
		boolean total = false;
		for (int n = 0; n < bools.length; n++) {
			total = total || bools[n];
		}
		return(total);
	}
	
	void dropDownSelected() {

		report("DropDownSelected");
		if (!dropDownPressed && clicked()) {
			dropDownSelected = !dropDownSelected;
			for (int i = 0; i < submenus.length; i++) {
				submenus[i].showingOptions = dropDownSelected;
			}
			clicked++;
		}
		dropDownPressed = clicked();
	}

	public void printOptions() {

		report("PrintOptions");
		if (clicked != oldClick) {
			for (int i = 0; i < submenus.length; i++) {
				report(i + ": " + submenus[i].showing() + " " + submenus[i].selectionComplete() + " " + clicked + "  ");
			}
			report("");
			oldClick = clicked;
		}
	}
	
	boolean subMenuChoiceWasSelected(int i) {
		boolean appropriate;
		appropriate = (i - stagger >= 0) ? true : false;
		if (appropriate) {
			return(submenus[i - stagger].m.selectedOption > 0 && submenus[i - stagger].m.selectedOption != submenuChoice);
		}
		return(false);
	}

	@Override
	protected void mutuallyExclusive(int i) {

		report("MutuallyExclusive");
		report("i: " + i);
		report("i - stagger: " + (i - stagger));
		report("i != selectedOption: " + (i != selectedOption));
		if (i < stagger && (i != selectedOption || subMenuChoiceWasSelected(i))) {
			report("A");
			if (items[i].selected) {

				if (selectedOption < 0) {
					selectedOption = i;
					if (dropDownSelected) {
						dropDownSelected = false;
					}
				} else if (selectedOption != i) {
					if (selectedOption >= stagger) {
						submenus[selectedOption - stagger].deselect();
						submenus[selectedOption - stagger].deselectSubMenu();
						PApplet.println("A - Deselected Other Submenu Properly");
					} else {
						items[selectedOption].selected = false;
					}
					deselection();

					selectedOption = i;
					submenuChoice = -1;
					if (dropDownSelected) {
						dropDownSelected = false;
					}
				}
			}
		} else if (i - stagger >= 0) {
			report(Integer.toString((i - stagger)));
			if (submenus[i - stagger].selectionComplete()) {
				if (i != selectedOption) {
					for (int j = 0; j < items.length; j++) {
						items[j].selected = false;
					}
				}
				if (selectedOption < 0) {
					selectedOption = i;
					report("B");
					//				PApplet.println("Completed Selection");
					//				PApplet.println(submenuChoice);
					if (selectedOption < 0) {
						selectedOption = i;
						report("Selected Option: " + i);
						submenuChoice = submenus[i - stagger].m.selectedOption;
						if (dropDownSelected) {
							dropDownSelected = false;
							submenus[i - stagger].showingOptions = false;
						}
					} else if (selectedOption != i || (submenus[i - stagger].m.selectedOption > 0 && submenus[i - stagger].m.selectedOption != submenuChoice)) {

						if (selectedOption >= stagger && i != selectedOption) {
							submenus[selectedOption - stagger].deselect();
							submenus[selectedOption - stagger].deselectSubMenu();
							report("B - Deselected Other Submenu Properly");
						} else {
							items[selectedOption].selected = false;
						}
					} else if (submenuChoice < 0) {
						deselection();

						selectedOption = i;
						submenuChoice = submenus[i - stagger].m.selectedOption;
						if (dropDownSelected) {
							dropDownSelected = false;
							submenus[i - stagger].showingOptions = false;
						}
						report("Selected Option: " + i);
						report("done");
					} else if (submenus[i - stagger].m.selectedOption >= 0 && submenus[i - stagger].m.selectedOption != submenuChoice) {
						deselection();

						selectedOption = i;
						submenuChoice = submenus[i - stagger].m.selectedOption;
						if (dropDownSelected) {
							dropDownSelected = false;
							submenus[i - stagger].showingOptions = false;
						}

						report("Selected Option: " + i);
						report("done");
					}
					//				clicked++;
					//				PApplet.println("Incremented in Mutually Exclusive");
				}
			}
		}
	}

	void deselection() {
		report("Deselection");
		if (selectedOption >= stagger) {
			submenus[selectedOption - stagger].deselect();
			//			submenus[selectedOption - stagger].deselectSubMenu();
			report("Deselected Other Submenu Properly");
		} else if (selectedOption < stagger) {
			items[selectedOption].selected = false;
		}
	}
}
