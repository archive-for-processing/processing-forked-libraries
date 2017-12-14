package mazeGenerator;
import java.util.ArrayList;
/**
 * The class Cell is the basic entity a maze consists of.A cell represents one square field in the grid
 * that later forms a maze. A cell has up to four neighbours, * meaning diagonally adjacent cells are
 * not considered neighbours (von Neumann neighbourhood).
 *  
 * @author AmazingGroup
 */

public class Cell {
	private boolean visited; // visited by algorithm
	private int widthPosition; // cell's width position in the grid
	private int heightPosition; // cell's height position in the grid
	private ArrayList<Cell> neighbours; // stores between two and four neighboring cells of the Cell
	private Wall[] walls; // walls[0]: up; walls[1]: right; walls[2]:down; walls[3]:left

	public Cell(int width, int height) {

		walls = new Wall[4];

		// set position
		widthPosition = width;
		heightPosition = height;

		// set cell's status to the default value of unvisited
		visited = false;

		// empty ArrayList for storing neighbors is initialized
		neighbours = new ArrayList<Cell>();

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

	public boolean getStatus() {
		return visited;
	}

	// returning all neighbors of a cell
	public ArrayList<Cell> getNeighbours() {
		return neighbours;
	}

	// setter-method for adding one neighbor to the list of neighboring cells
	public void setNeighbour(Cell neighbour) {
		neighbours.add(neighbour);
	}

	// setter-method, add one connection between two adjacent cells
	public void addConnection(Cell connectedCell) {
		// the wall both cells share is found and set as unsolid
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

	public String toString() {
		return "[" + widthPosition + "][" + heightPosition + "]";
	}
}
