package mazeGenerator;
/**
 * 
 * This class represents the Strategy interface. It makes the class Maze (=context) independent
 * on how the maze algorithms are implemented.
 * 
 *  @author AmazingGroup
 *  */

public interface MazeCreator {
	public void setUpStructure(Cell[][] mazeFields);

	public void createMaze(Cell startingField, Cell endField);
}

