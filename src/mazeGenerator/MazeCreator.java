package mazeGenerator;
/**
 * @author AmazingGroup 
 * This class represents the Strategy interface. Which
 * makes the maze class(context) independent on how the maze algorithms
 * are implemented.
 */

public interface MazeCreator {
	public void setUpStructure(Cell[][] mazeFields);

	public void createMaze(Cell startingField, Cell endField);
}

