package feib.gm4p;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class InputHelper {

	private PApplet p;

	public InputHelper(PApplet parentApplet){
		this.p = parentApplet;

		if (GM.debugMode) 
			PApplet.println("InputHelper created");
	}

	// Arrays of booleans. One for each key code
	public boolean [] lastFrameKeysDown = new boolean[256];
	public boolean [] keysDown = new boolean[256];
	public boolean [] keysPressed = new boolean[256];
	public boolean [] keysReleased = new boolean[256];

	public boolean leftMouseDown = false;
	public boolean centerMouseDown = false;
	public boolean rightMouseDown = false;

	public boolean lastLeftMouseDown = false;
	public boolean lastCenterMouseDown = false;
	public boolean lastRightMouseDown = false;

	public boolean leftMouseClicked = false;
	public boolean centerMouseClicked = false;
	public boolean rightMouseClicked = false;

	public boolean leftMouseReleased = false;
	public boolean centerMouseReleased = false;
	public boolean rightMouseReleased = false;

	public int lastKeyPressed=0;

	// Call this method after each update in order to remember
	// which keys were pressed in the last frame
	public void update() {

		for (int iKey=0; iKey<keysDown.length; iKey++) {
			if (!lastFrameKeysDown[iKey] && keysDown[iKey])
				keysPressed[iKey] = true;
			else
				keysPressed[iKey] = false;


			if (lastFrameKeysDown[iKey] && !keysDown[iKey])
				keysReleased[iKey] = true;
			else
				keysReleased[iKey] = false;

			lastFrameKeysDown[iKey] = keysDown[iKey];
		}


		leftMouseClicked = !lastLeftMouseDown && leftMouseDown;
		rightMouseClicked = !lastRightMouseDown && rightMouseDown;
		centerMouseClicked = !lastCenterMouseDown && centerMouseDown;

		leftMouseReleased= lastLeftMouseDown && !leftMouseDown;
		rightMouseReleased = lastRightMouseDown && !rightMouseDown;
		centerMouseReleased = lastCenterMouseDown && !centerMouseDown;

		lastLeftMouseDown = leftMouseDown;
		lastRightMouseDown = rightMouseDown;
		lastCenterMouseDown = centerMouseDown;
	}

	public void mouseEvent(MouseEvent event){

		int x = event.getX();
		int y = event.getY();

		switch (event.getAction()) {
		case MouseEvent.PRESS:
			mousePressed();
			break;
		case MouseEvent.RELEASE:
			 mouseReleased();
			break;		
		}

	}

	public void keyEvent(KeyEvent event){

	}
	//keyPressed is a Processing specific "callback" method
	//that gets called when a key is pressed
	//Set the boolean at the index of "keyCode" to true
	void keyPressed() {
		lastKeyPressed = (p.keyCode == 0) ? p.key : p.keyCode;
		keysDown[lastKeyPressed] = true;
	}

	//keyPressed is a Processing specific "callback" method
	//that gets called when a key is released
	//Set the boolean at the index of "keyCode" to true
	void keyReleased() {
		keysDown[(p.keyCode == 0) ? p.key : p.keyCode] = false;
	}

	void mousePressed() {
		leftMouseDown = (p.mouseButton == PApplet.LEFT);
		centerMouseDown = (p.mouseButton == PApplet.CENTER);
		rightMouseDown =  (p.mouseButton == PApplet.RIGHT);
	}

	void mouseReleased() {
		leftMouseDown = !(p.mouseButton == PApplet.LEFT);
		centerMouseDown = !(p.mouseButton == PApplet.CENTER);
		rightMouseDown =  !(p.mouseButton == PApplet.RIGHT); 
	}
}
