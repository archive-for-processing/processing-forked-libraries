package mazeGenerator;
/**
The class RecursiveStrategy provides the Template Methods for the classes Kruskal and RecursiveBacktracker.
The methods setUpStructure() and basicMode() are shared among Kruskal and RecursiveBacktracker and therefore implemented.
The methods huntMode() and handleCases() differ and therefore remain abstract here. 
*/
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RecursiveStrategy implements MazeCreator {
	Stack<Cell> backTrackingStack; // stack stores the already visited cells for backtracking
	ArrayList<Cell> unvisitedCells; // stores unvisited cells
	Cell[][] mazeFields;

	// set up structure necessary for maze generation
	public void setUpStructure(Cell[][] mazeFields) {
		backTrackingStack = new Stack<Cell>(); // new empty stack
		this.mazeFields = mazeFields;
		unvisitedCells = new ArrayList<Cell>(); // new ArrayList

		// List of unvisited cells is as a default value filled with all cells from maze
		// class
		for (int i = 0; i < mazeFields.length; i++) {
			for (int j = 0; j < mazeFields.length; j++) {
				unvisitedCells.add(mazeFields[i][j]);
			}
		}
	}

	public void createMaze(Cell startingField, Cell endpoint) {
		Cell current = startingField; // some cell is set as the starting field

		// set status of current cell to visited
		current.setVisited(true);

		// delete cell from unvisited
		unvisitedCells.remove(current);

		// visited cell is pushed to stack
		backTrackingStack.push(current);

		// loop as long as there are unvisited cells
		while (!unvisitedCells.isEmpty()) {

			// System.out.println("Position: " + current.toString());

			// find the neighbours of the cell
			ArrayList<Cell> neighbourList = new ArrayList<Cell>();

			neighbourList = current.getNeighbours();
			// System.out.println("Found following neighbours: " +
			// neighbourList.toString());

			// remove neighbors that have already been visited
			for (int i = 0; i < neighbourList.size(); i++) {
				if (!unvisitedCells.contains(neighbourList.get(i))) {
					// System.out.println("Deleting: " + neighbourList.get(i) + " from
					// neighbourList");
					neighbourList.remove(i);
					i--;
				}
			}

			current = handleCases(neighbourList, current);
		}

		Wall[] wallsOfGoalCell = endpoint.getWalls();
		wallsOfGoalCell[1].setPath();

	}

	abstract Cell handleCases(ArrayList<Cell> neighbours, Cell current);

	public Cell basicMode(ArrayList<Cell> neighbours, Cell current) {
		// temp variable saves the current cell
		Cell temp = current;

		// a random neighbour of the current set is selected and set as the new current
		// cell
		current = neighbours.get(ThreadLocalRandom.current().nextInt(0, neighbours.size()));

		// the two cells are connected by path
		temp.addConnection(current);
		current.addConnection(temp);

		// System.out.println("I connected cell " + temp.toString() + " with cell " +
		// current.toString());

		// set status of current cell to visited
		current.setVisited(true);

		backTrackingStack.push(current);

		// delete cell from unvisited
		unvisitedCells.remove(current);

		return current;
	}

	abstract Cell huntMode(ArrayList<Cell> neighbours, Cell current);

}
