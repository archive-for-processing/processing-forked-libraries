package funGUI;

import processing.core.*;
import processing.data.FloatList;

/**
 * The DropDownMenu class provides all of your GUI 
 * @author audrey
 *
 */
public class DropDownMenu extends Menu implements PConstants {
//	float w;
	PGraphics g;
	int c = 0x00FFFF;
//	float x;
//	float y;
	DropDownMenuItem [] items;
	
	boolean dropDownSelected = false;
	boolean dropDownPressed = false;
	
	
	public DropDownMenu(PApplet p, float ex, float why, String [] optionLabels) {
		super(p, ex, why, optionLabels, "");
		init(ex, why, optionLabels);
//		this.items = new DropDownMenuItem [optionLabels.length];
//		PFont font = this.p.createFont(REG_SANSS_TXT, REGTXTSIZE);
//		p.textFont(font);
//		FloatList textSizes = new FloatList();
//		for (int i = 0; i < optionLabels.length; i++) {
//			textSizes.append((float) (p.g.textWidth(optionLabels[i]) * 1.2));
//		}
//		for (int i = 0; i < optionLabels.length; i++) {
//			items[i] = new DropDownMenuItem(p, ex, why, optionLabels[i], 1, textSizes.max(), i, this);
//		}
//		
//		this.w = items[0].w;
//		this.h = optionLabels.length * 20;
//		this.g = this.p.g;
//		this.x = ex;
//		this.y = why;
	}
	
	public DropDownMenu(PApplet p, float ex, float why, String [] optionLabels, String t) {
		super(p, ex, why, optionLabels, t);
		init(ex, why, optionLabels);
	}
	
	@Override
	public void draw() {
		for (int i = 0; i < items.length; i++) {
			if (items[i].selected || dropDownSelected) {
				g.pushMatrix();
				if (!dropDownSelected) {
					g.translate(0, -i * 18);
				}
				items[i].draw(dropDownSelected);
				g.popMatrix();
			}
			mutuallyExclusive(i);
		}
		drawDropDown();
	}
	
	void drawDropDown() {
		g.pushStyle();
		g.fill(c);
		g.stroke(0);
		g.rectMode(CORNER);
		g.rect(x, y, w + 15, 16);
		triangle();
		g.popStyle();
		dropDownSelected();
	}
	
	void triangle() {
		float x1 = (float) (x + w + 7.5);
		float y1 = (float) (y + 9);
		
		float amt = 5;
		g.fill(30);
		g.beginShape(TRIANGLES);
		g.vertex(x1 - amt, y1 - amt);
		g.vertex(x1, y1 + amt);
		g.vertex(x1 + amt, y1 - amt);
		g.endShape();
	}
	
	@Override
	protected void mutuallyExclusive(int i) {
		if (items[i].selected) {
			if (selectedOption < 0 || selectedOption != i) {
				PApplet.println("Selected option: " + selectedOption + " New Selected Option: " + i);
				if (selectedOption != -1) {
					items[selectedOption].selected = false;
				}
				selectedOption = i;
				if (dropDownSelected) {
					dropDownSelected = false;
				}
			}
		}
	}
	
	void dropDownSelected() {
		if (!dropDownPressed && clicked()) {
			dropDownSelected = !dropDownSelected;
			
		}
		dropDownPressed = clicked();
	}

	boolean clicked() {
		float x1 = (float) (x + w + 7.5);
		float y1 = (float) (y + 9);

		float amt = 5;
		return(p.mouseX > x1 - amt && p.mouseX < x1 + amt && p.mouseY > y1 - amt && p.mouseY < y1 + amt && p.mousePressed);

	}
	
	void init(float ex, float why, String [] optionLabels) {
		this.items = new DropDownMenuItem [optionLabels.length];
		PFont font = this.p.createFont(REG_SANSS_TXT, REGTXTSIZE);
		p.textFont(font);
		FloatList textSizes = new FloatList();
		for (int i = 0; i < optionLabels.length; i++) {
			textSizes.append((float) (p.g.textWidth(optionLabels[i]) * 1.2));
		}
		for (int i = 0; i < optionLabels.length; i++) {
			items[i] = new DropDownMenuItem(p, ex, why, optionLabels[i], 1, textSizes.max(), i, this);
		}
		this.h = optionLabels.length * 20;
		this.w = items[0].w;
		this.g = this.p.g;
		x = ex;
		y = why;
	}
}
