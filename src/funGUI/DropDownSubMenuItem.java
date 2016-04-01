package funGUI;

import processing.core.*;

public class DropDownSubMenuItem extends DropDownMenuItem {
	Menu m;
	boolean showingOptions = false;
	boolean wasShowing = false;
	int oldOption = -1;
	
	public DropDownSubMenuItem(PApplet p, float ex, float why, String label, int numColumns, float width, int ordinal, Menu menu, String [] optionLabels) {
		super(p, ex, why, label, numColumns, width, ordinal, menu);
		m = new Menu(p, x() + w, y(), optionLabels);
		this.selected = false;
		c = 0xFF3213;
	}

	public void draw(boolean selected) {
		g.pushStyle();
		display(selected);
		text();
		g.popStyle();
	}
	
	
	protected void display(boolean okayToSelect) {
		g.pushStyle();
		g.rectMode(CORNER);
		if (selected && okayToSelect) {
			if (showingOptions) m.draw();
			g.fill(210);
		} else {
			g.fill(c);
		}
		g.stroke(0);
		itemDisplay();
		g.popStyle();

		if (okayToSelect) {
			selected();
		}
		printlnChanges();
//		showOptions();
	}
	
	public boolean showing() {
		return(showingOptions);
	}
	
	public void showOptions() {
		if ((!wasShowing || oldOption != this.m.selectedOption) && selected) {
			showingOptions = !showingOptions;
		}
		
		wasShowing = selected;
		oldOption = this.m.selectedOption;
	}
	
	public void deselectSubMenu() {
		for (int i = 0; i < m.items.length; i++) {
			m.items[i].selected = false;
			if (i != m.selectedOption) m.items[i].selected = false;
		}
	}
	
	public void deselect() {
		this.selected = false;
		this.m.selectedOption = -1;
//		for (int i = 0; i < m.items.length; i++) {
//			m.items[i].selected = false;
//		}
		oldOption = -1;
	}

	public boolean selectionComplete() {
		return(selected && this.m.selectedOption != oldOption);
	}
	
	protected void itemDisplay() {
//		if (!selected) {
//			g.rect(x, y(), w, h);
//			
//		} else {
			float point = 5;
			g.beginShape();
			g.vertex(x, y());
			g.vertex(x + w, y());
			g.vertex(x + w + point, y() + h * .5f);
			g.vertex(x + w, y() + h);
			g.vertex(x, y() + h);
			g.endShape(CLOSE);
//		}
	}
	
	boolean oldChange = false;
	public void printlnChanges() {
		if (oldChange != selected) {
			PApplet.println("Selected: " + m.selectedOption);
			if (m.selectedOption > -1 && m.selectedOption < m.items.length) PApplet.println("Selected?: " + m.items[m.selectedOption].selected);
			PApplet.println("SelectedSubMenuOption: " + m.selectedOption + "   Selected???" + selected);
			if (m.selectedOption >= 0) PApplet.println("Selected?: " + m.items[m.selectedOption].selected);
		}
		oldChange = selected;
	}
}
