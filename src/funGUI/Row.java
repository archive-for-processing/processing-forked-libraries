package funGUI;
import java.util.ArrayList;

import processing.core.*;

/**
 * Row is solely for the purpose of organizing a matrix.
 * Instead of having a 2D array, an ArrayList holds all of
 * the objects. You can specify a width to limit the objects
 * to, or you can just let it be. It will automatically assume
 * that the width of the page is the limit.
 * @author audreyseo
 *
 */
public class Row implements FunGUIObject {
	ArrayList<Frame> objects = new ArrayList<Frame>();
	PApplet p;
	float x, y;
	float w, h;
	
	public Row(PApplet p, float x, float y) {
		this.p = p;
		this.x = x;
		this.y = y;
		w = p.width;
		h = -1; // Negative means that height is unlimited
	}
	
	/**
	 * The constructor of the class.
	 * @param p		PApplet, the parent of the sketch (usually "this")
	 */
	public Row(PApplet p) {
		this.p = p;
		x = p.width / 2;
		y = p.width / 2;
		w = p.width;
		h = -1; // Negative means that height is unlimited
	}
	
	public void draw() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).draw();
		}
	}
	
	public void addChoiceList() {
		
	}
	
	public void addDropDownMenu(float ex, float why, String [] optionLabels) {
		objects.add(new DropDownMenu(p, ex, why, optionLabels));
	}
	
	public void addGraph() {
		
	}
	
	public void addLabel() {
		
	}
	
	public void addObjectLabel() {
		
	}
	
	public void addList() {
		
	}
	
	public void addOptionList() {
		
	}
	
	public void addMenu() {
		
	}
	
	public void addMessage() {
		
	}
	
	public void addNumOutput() {
		
	}
	
	public void addPhotoButton() {
		
	}
	
	public void addRectButton() {
		
	}
	
	public void addScrollMenu() {
		
	}
	
	public void addSlider() {
		
	}
	
	public void addRow() {
		
	}
	
	public void addSliderOutput() {
		
	}
	
	public void addTextBox() {
		
	}
	
	public void addTextDisplay() {
		
	}
	
	public void addTextStrip() {
		
	}
	
	public void addToggleButton() {
		
	}

	@Override
	public float x() {
		return x;
	}

	@Override
	public float y() {
		return y;
	}

	@Override
	public float h() {
		return h;
	}

	@Override
	public float w() {
		return w;
	}

	@Override
	public String returnName() {
		return "Row";
	}

	@Override
	public void record() {
		PApplet.println("FunGUI Object: " + returnName());
		
	}
	
	@Override
	public void record(String s) {
		PApplet.println("FunGUI Object: " + returnName() + ": " + s);
	}
}
