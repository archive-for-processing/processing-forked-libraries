package mazeGenerator;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/** 
 * This class implements the Hunt and Kill algorithm for creating mazes. It creates perfect mazes with
 * rather many and long dead ends. It implements the concrete steps of the Template Method in the class 
 * RecursiveStrategy.
 * 
 * @author AmazingGroup
 */

public class HuntAndKill extends RecursiveStrategy {

  // defines when basic mode and hunting mode are called
  public boolean handleCases(ArrayList<Cell> neighbours) {
	
    int random = ThreadLocalRandom.current().nextInt(0, 10);

    // if the cell has neighbours, that have not been visited yet and randomly in 20% of the cases
    if (neighbours.size() > 0 && !(random % 5 == 0)) {
      return true;
    }

    // handle hunting mode
    else {
      return false;
    }

  }

  public Cell huntMode(ArrayList<Cell> neighbours, Cell current) {

	  mainLoop:

    // iterate over all cells
    for (int i = 0; i < mazeFields.length; i++) {
      for (int j = 0; j < mazeFields.length; j++) {

    	//find a cell that has not been visited yet
        if (mazeFields[i][j].getStatus() == false) {

          ArrayList<Cell> neighbourList = mazeFields[i][j].getNeighbours();
          
          //iterates over all its neighbours
          for (Cell c : neighbourList) {
        	  
        	//if one neighbour is found, that has been visited
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
