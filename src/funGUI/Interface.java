package funGUI;
import java.util.HashMap;

import processing.core.*;

/**
 * The Interface class can help you organize all of your buttons,
 * displays, etc. so that everything is neat. Interface just takes
 * a number of rows and a number of columns.
 * @author audrey
 *
 */
public class Interface {
	int cols = 0;
	int rows = 0;
	public float defaultHeight = 50;
	public float defaultWidth = 100;
	float rowHeight = 0;
	float columnWidth = 0;
	PApplet p;
	int [] standardColors = {p.color(100, 100, 230), p.color(190, 190, 190), p.color(240, 240, 240)};
//	ArrayList<Button> buttons = new ArrayList<Button>();
	HashMap<String, Button> buttons = new HashMap<String, Button>();
	HashMap<String, List> lists = new HashMap<String, List>();
	
	/**
	 * Instantiates a new Interface, given the number of columns and rows.
	 * If numColumns is -1, then it spaces all its buttons/displays
	 * and such by their width. If numRows is less than -1, each widget is
	 * limited by their width and is allowed to grow in the y direction.
	 * Specifying both results in an even array-like format.
	 * @param p				PApplet, the parent of the sketch (usually this)
	 * @param numColumns	int, the number of columns (or -1, if columns don't matter)
	 * @param numRows		int, the number of rows (or -1, if rows don't matter)
	 */
	public Interface(PApplet p, int numColumns, int numRows) {
		this.p = p;
		this.cols = numColumns;
		this.rows = numRows;
		if (cols > 0) {
			columnWidth = (p.width - 50) / cols;
		}
		if (rows > 0) {
			rowHeight = (p.height - 50) / rows;
		}
	}
	
	public void makeRectButton(int column, int row, String name) {
		buttons.put(index(column, row),  new RectButton(p, column * columnWidth, row * rowHeight, .8f * columnWidth, .8f * rowHeight, standardColors, name));
	}
	
	public void makeList(int column, int row, String [] names) {
		
	}
	
	public void makeRectButton(int column, int row, float length, String name) {
		float height = .8f * rowHeight;
		float width = .8f * columnWidth;
		if (rowHeight == 0) {
			height = defaultHeight;
			width = length;
		} else if (columnWidth == 0) {
			width = defaultWidth;
			height = length;
		}
		buttons.put(index(column, row),  new RectButton(p, column * columnWidth, row * rowHeight, width, height, standardColors, name));
	}
	
	public void makeToggleButton(int column, int row, String name) {
		
	}
	
	public Button getButton(int column, int row) {
		return(buttons.get(index(column, row)));
	}
	
	private String index(int col, int row) {
		return(PApplet.str(col) + ", " + PApplet.str(row));
	}
}
