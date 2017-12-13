package mazeGenerator;

/**
 * Stores and manages entire mazes consisting of walls and cells
 * 
 *  @author AmazingGroup
 */

import processing.core.PApplet;

public class Maze {
	private int width; // width of maze
	private int height; // height of maze
	private Cell[][] mazeFields; // array-field storing the cells within maze
	private Wall[][] verticalWalls; // array-field storing the vertical walls of the maze
	private Wall[][] horizontalWalls; // array-field storing the horizontal walls of the maze
	private MazeCreator creator; // instance of interface mazeCreator
	private PApplet parent; // PApplet for processing window
	private int cellSize; // size of one cell

	public Maze(int wid, int hei, PApplet par, MazeCreator mazeCreator, int cSize) {
		this.width = wid; // width of maze is set
		this.height = hei; // height of maze is set
		mazeFields = new Cell[width][height]; // create Array for cells
		horizontalWalls = new Wall[width][height + 1]; // initialize empty array for horizontal walls
		verticalWalls = new Wall[width + 1][height]; // initialize empty array for vertical walls
		cellSize = cSize; // set cell size
		parent = par; // set PApplet for processing window

		// fill Array with horizontal Wall-elements
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height + 1; j++) {
				horizontalWalls[i][j] = new Wall(i * cellSize, j * cellSize, parent, true, cellSize); // calls
																										// constructor
																										// of class Wall
			}
		}

		// fill Array with vertical Wall-elements
		for (int i = 0; i < width + 1; i++) {
			for (int j = 0; j < height; j++) {
				verticalWalls[i][j] = new Wall(i * cellSize, j * cellSize, parent, false, cellSize); // calls
																										// constructor
																										// of class Wall
			}
		}

		// fill Array with Cell-elements
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mazeFields[i][j] = new Cell(i, j); // calls constructor of class Cell

				// connect cell with its walls
				mazeFields[i][j].setWalls(horizontalWalls[i][j], horizontalWalls[i][j + 1], verticalWalls[i][j],
						verticalWalls[i + 1][j]);
			}
		}

		// neighbors of the cells are set; a neighbor is a cell, that has an index of
		// the cell width +/-1 or cell height +/-1
		// diagonal cells are not considered neighbors

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (j - 1 >= 0) {
					mazeFields[i][j].setNeighbour(mazeFields[i][j - 1]);
				}
				if (j + 1 < width) {

					mazeFields[i][j].setNeighbour(mazeFields[i][j + 1]);
				}
				if (i - 1 >= 0) {
					mazeFields[i][j].setNeighbour(mazeFields[i - 1][j]);
				}
				if (i + 1 < height) {
					mazeFields[i][j].setNeighbour(mazeFields[i + 1][j]);
				}

			}
		}
		creator = mazeCreator;// mazeCreator is set
		creator.setUpStructure(mazeFields);
	}

	// creates output of maze on processing window
	public void draw() {
		parent.rect(0, 0, width * cellSize + 3, height * cellSize);

		// print all solid horizontal walls
		for (int i = 0; i < horizontalWalls.length; i++) {
			for (int j = 0; j < horizontalWalls[0].length; j++)
				if (horizontalWalls[i][j].isSolid()) {
					horizontalWalls[i][j].draw();
				}
		}

		// print all solid vertical walls
		for (int i = 0; i < verticalWalls.length; i++) {
			for (int j = 0; j < verticalWalls[0].length; j++)
				if (verticalWalls[i][j].isSolid()) {
					verticalWalls[i][j].draw();
				}
		}
	}

	/**
	 * 
	 * Getter methods.
	 *
	 */

	public int getHeight() {
		return height;
	}

	public int getCellSize() {
		return cellSize;
	}

	public Cell getCell(int widthPos, int heightPos) {
		return mazeFields[widthPos][heightPos];
	}

	public Wall[][] getVerticalWalls() {
		return verticalWalls;
	}

	public Wall[][] getHorizontalWalls() {
		return horizontalWalls;
	}

	public MazeCreator getMazeCreator() {
		return creator;
	}
}
