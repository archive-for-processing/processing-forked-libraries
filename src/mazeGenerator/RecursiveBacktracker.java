package mazeGenerator;
import java.util.ArrayList;
/** 
 * This class implements the Recursive Backtracking algorithm for creating mazes.
 * It creates perfect mazes with a long winding solution path and few dead ends.
 * 
 * @author AmazingGroup
 */


public class RecursiveBacktracker extends RecursiveStrategy {

  public boolean handleCases(ArrayList<Cell> neighbours) {

    // if the cell has neighbours, that have not been visited yet
    if (neighbours.size() > 0) {
      return true;
    }

    else {
      return false;
    }
  }

  Cell huntMode(ArrayList<Cell> neighbours, Cell current) {
    // System.out.println("Dead End!"); //debug
    backTrackingStack.pop();
    current = backTrackingStack.peek();
    return current;
  }

}