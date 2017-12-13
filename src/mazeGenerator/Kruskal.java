package mazeGenerator;

/** 
 * This class implements the Kruskal algorithm for creating mazes. It implements the
 * concrete strategy to the interface MazeCreator. 
 * Kruskal creates mazes with many, but rather short dead ends and is based on the Kruskal
 * algorithm for calculating the minimum spanning tree in a graph.
 * 
 * 
 *  @author AmazingGroup
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Kruskal implements MazeCreator {

	// stack of all edges between the cells, edge is defined as a set including the
	// cell "from" and the cell "to"
	private Stack<Set<Cell>> allEdges;

	// the set saves sets that include cells
	private Set<Set<Cell>> setsOfCells;

	// creates the initial structure necessary for running the algorithm
	public void setUpStructure(Cell[][] mazeFields) {
		allEdges = new Stack<Set<Cell>>();
		setsOfCells = new HashSet<Set<Cell>>();

		// go over all cells
		for (int i = 0; i < mazeFields.length; i++) {
			for (int j = 0; j < mazeFields.length; j++) {

				// create new set with cell in it
				Set<Cell> cellSet = new HashSet<Cell>();
				cellSet.add(mazeFields[i][j]);

				// by default setsOfCells contains every cell in a separate set
				setsOfCells.add(cellSet);

				// find the neighbours of the cell
				for (Cell c : mazeFields[i][j].getNeighbours()) {

					// add the edge to stack if not yet added
					Set<Cell> current = new HashSet<Cell>();

					current.add(mazeFields[i][j]);
					current.add(c);
					if (!allEdges.contains(current)) {
						allEdges.add(current);
					}

				}
			}
		}
		// stack is shuffled to randomize order of edges
		Collections.shuffle(allEdges);
	}

	// implementation of the Kruskal algorithm
	public void createMaze(Cell startingField, Cell endpoint) {

		// do for all edges
		while (!allEdges.empty()) {

			// select first edge
			Set<Cell> current = allEdges.peek();

			// pop edge from stack
			allEdges.pop();

			// saves the two cells in edge into Array
			Cell[] contents = new Cell[2];
			contents = current.toArray(contents);

			// temporary variable to save the sets in which the cells can be currently found
			Set<Cell> firstSet = new HashSet<Cell>();
			Set<Cell> secondSet = new HashSet<Cell>();

			// iterate over all sets in setsOfCells to find the one including the cell of
			// edge
			for (Set<Cell> iterator : setsOfCells) {
				if (iterator.contains(contents[0])) {
					firstSet = iterator;
				}
			}

			for (Set<Cell> iterator : setsOfCells) {
				if (iterator.contains(contents[1])) {
					secondSet = iterator;
				}
			}

			// if cells are not in one set already:
			if (!secondSet.equals(firstSet)) {

				// create set to combine the two sets
				Set<Cell> combinedSet = new HashSet<Cell>();

				// add both sets to the combined set
				combinedSet.addAll(secondSet);
				combinedSet.addAll(firstSet);

				// set the connection between the cells in the cell objects
				contents[0].addConnection(contents[1]);

				// set the shared wall to unsolid
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						Wall[] wallsCellOne = contents[0].getWalls();
						Wall[] wallsCellTwo = contents[1].getWalls();
						if (wallsCellOne[i].equals(wallsCellTwo[j])) {
							wallsCellOne[i].setPath();
						}
					}
				}
				// remove the two separate cells from setsOfCells
				setsOfCells.remove(secondSet);
				setsOfCells.remove(firstSet);

				// add the combined set to setsOfCells
				setsOfCells.add(combinedSet);
			}
		}

		// set the exit wall to unsolid
		Wall[] wallsOfGoalCell = endpoint.getWalls();
		wallsOfGoalCell[1].setPath();
	}
}
