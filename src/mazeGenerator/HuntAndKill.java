package mazeGenerator;

/** 
 * This class implements the Hunt and Kill algorithm for creating mazes.
 * It implements the concrete steps of the Template Method in the class RecursiveStrategy.
  */

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class HuntAndKill extends RecursiveStrategy {

	// defines when basic mode and hunting mode are called
	public Cell handleCases(ArrayList<Cell> neighbours, Cell current) {
		int random = ThreadLocalRandom.current().nextInt(0, 10);

		// if the cell has neighbours, that have not been visited yet and no random
		// initializing of hunting mode
		if (neighbours.size() > 0 && !(random % 5 == 0)) {
			return basicMode(neighbours, current);
		}

		// handle hunting mode
		else {
			return huntMode(neighbours, current);
		}

	}

	public Cell huntMode(ArrayList<Cell> neighbours, Cell current) {
		// System.out.println("Dead End! Moving into hunting mode");

		mainLoop:

		// iterate over all cells until one is found, which has a already visited
		// neighbour
		for (int i = 0; i < mazeFields.length; i++) {
			for (int j = 0; j < mazeFields.length; j++) {

				if (mazeFields[i][j].getStatus() == false) {

					ArrayList<Cell> neighbourList = mazeFields[i][j].getNeighbours();
					for (Cell c : neighbourList) {
						if (c.getStatus() == true) {
							c.addConnection(mazeFields[i][j]);

							current = mazeFields[i][j];

							// set status of current cell to visited
							current.setVisited(true);

							// delete cell from unvisited
							unvisitedCells.remove(current);

							break mainLoop;
						}
					}
				}

			}
		}
		return current;
	}
}
