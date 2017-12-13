package mazeGenerator;
/**
 * This class represents the walls in the maze grid. There exists walls 
 * on all outer edges and between each pair of adjacent cells.
 */

import processing.core.PApplet;

public class Wall {
	private int posX;
	private int posY;
	private boolean solid; // wall solid or passage
	private PApplet parent;
	private int wallWidth; // width of wall
	private int wallHeight;// height of wall

	public Wall(int widthPos, int heightPos, PApplet p, boolean horizontal, int cellS) {
		solid = true;
		posX = widthPos; // position of wall is passed by constructor
		posY = heightPos;

		parent = p;

		if (horizontal) {
			wallWidth = cellS;
			wallHeight = cellS / 8;
			posY = posY - cellS / 8;
		}

		else {
			wallWidth = cellS / 8;
			wallHeight = cellS;
			posX = posX - cellS / 8;
		}
	}

	public void draw() {
		parent.stroke(0, 0, 0);
		parent.fill(0, 255, 0);
		parent.rect(posX, posY, wallWidth, wallHeight);
	}

	public String toString() {
		String s = "Wall: " + posX + " ," + posY;
		return s;
	}

	/**
	 * 
	 * Getter and Setter methods
	 * 
	 */

	// sets a wall to be possible to walk through
	public void setPath() {
		solid = false;
	}

	public boolean isSolid() {
		return solid;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getWallHeight() {
		return wallHeight;
	}

	public int getWallWidth() {
		return wallWidth;
	}
}

