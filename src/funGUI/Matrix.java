package funGUI;

import processing.core.*;

public class Matrix {
	public static enum ClassType {
		GUIFRAME, SLIDER, TEXTDISPLAY, TOGGLER;
	}

	public float colw, rowh;
	public float x, y;
	public int rows, cols;
	public Frame[][] matrix;
	private PApplet p;

	public Matrix(PApplet p, float w, float h, int row, int col) {
		init(w, h, row, col);
		this.p = p;
	}

	public Matrix(PApplet p, float x, float y, float w, float h, int row, int col) {
		init(w, h, row, col);
		this.x = x;
		this.y = y;
		this.p = p;
	}

	public void assignToggler(int xi, int yi, String label) {
		float newx = x + xi * colw;
		float newy = y + yi * rowh;
		matrix[xi][yi] = new ToggleButton(p, newx, newy, label);
	}

	public void assignSlider(int xi, int yi, float w, float minimum, float maximum, String label) {
		float newx = x + xi * colw;
		float newy = y + yi * rowh;
		matrix[xi][yi] = new Slider(p, newx, newy, w, minimum, maximum);
	}
	
	public void assignSlider(int xi, int yi, float w, String label) {
		float newx = x + xi * colw;
		float newy = y + yi * rowh;
		matrix[xi][yi] = new Slider(p, newx, newy, w, label);
	}
	
	public void assignSlider(int xi, int yi, float w) {
		float newx = x + xi * colw;
		float newy = y + yi * rowh;
		matrix[xi][yi] = new Slider(p, newx, newy, w);
	}
	
	public Frame getSlider(int xi, int yi) throws NullPointerException {
		if (matrix[xi][yi].returnName().equals("Slider")) {
			return(matrix[xi][yi]);
		}
		else return(null);
	}

	public void draw() {
		for (	int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] != null) {
//					PApplet.println("tried: (" + i + ", " + j + ")");
					matrix[i][j].draw();
//					PApplet.println("finished");
				}
			}
		}
	}

	public void init(float w, float h, int row, int col) {
		this.rows = row;
		this.cols = col;
		this.colw = w / (float)(cols);
		this.rowh = h / (float)(rows);
		matrix = new Frame[cols][rows];
	}
}
