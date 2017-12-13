package mazeGenerator;
/**
 * The class Cell is the basic entity a maze consists of.
 */

import java.util.ArrayList;

public class Cell {
	private boolean visited; // visited by algorithm
	private int widthPosition; // cell's width position in the grid
	private int heightPosition; // cell's height position in the grid
	private ArrayList<Cell> neighbours; // stores between two and four neighboring cells of the Cell
	private Wall[] walls; // walls[0]: up; walls[1]: right; walls[2]:down; walls[3]:left

	// constructor; arguments: position of cell, PApplet
	public Cell(int width, int height) {

		walls = new Wall[4];

		// set position
		widthPosition = width;
		heightPosition = height;

		// set cell's status to the default value of unvisited
		visited = false;

		// empty ArrayList for storing neighbors is initialized
		neighbours = new ArrayList<Cell>();

		// empty ArrayList for storing connected cells is initialized
		// connectedCells = new ArrayList<Cell>();
	}

	/**
	 * 
	 * Getter and Setter Methods
	 * 
	 */

	// setter-method for setting status of cell (visited/ unvisited)
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	// getter-method for reading status of cell
	public boolean getStatus() {
		return visited;
	}

	// getter-method returning all neighbors of a cell
	public ArrayList<Cell> getNeighbours() {
		return neighbours;
	}

	// setter-method for adding one neighbor to the list of neighboring cells
	public void setNeighbour(Cell neighbour) {
		neighbours.add(neighbour);
	}

	// setter-method, adds one connection
	public void addConnection(Cell connectedCell) {
		// the wall between the cells is set as unsolid
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.walls[i].equals(connectedCell.walls[j])) {
					this.walls[i].setPath();
				}
			}
		}
	}

	public Wall[] getWalls() {
		return walls;
	}

	// setter-method, set all walls around cell
	public void setWalls(Wall up, Wall down, Wall left, Wall right) {
		walls[0] = up;
		walls[1] = right;
		walls[3] = left;
		walls[2] = down;
	}

	// DEBUG overriding default toString-method
	public String toString() {
		return "[" + widthPosition + "][" + heightPosition + "]";
	}
}
