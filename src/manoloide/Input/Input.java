package manoloide.Input;

import java.util.ArrayList;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Input {
	private PApplet a;
	public boolean click, dclick, press, released, kclick, kpress, kreleased;
	public int key;
	public int amouseX, amouseY, pmouseX, pmouseY, mouseX, mouseY;
	public int pressCount, mouseWheel, timepress;
	public Key ENTER, BACKSPACE, ALT, CTRL, SHIFT, UP, DOWN, LEFT, RIGHT;

	public Input(PApplet a) {
		this.a = a;
		click = dclick = released = press = false;
		kclick = kreleased = kpress = false;
		pressCount = 0;

		ENTER = new Key();
		BACKSPACE = new Key();
		ALT = new Key();
		CTRL = new Key();
		SHIFT = new Key();
		UP = new Key();
		DOWN = new Key();
		LEFT = new Key();
		RIGHT = new Key();

		a.registerMethod("keyEvent", this);
		a.registerMethod("mouseEvent", this);
		a.registerMethod("draw", this);
	}

	public void draw(){
		update();
	}

	public void keyEvent(KeyEvent e) {
		key = e.getKey();
		switch (e.getAction()) {
			case KeyEvent.PRESS:
			event(true);
			break;
			case KeyEvent.TYPE:
			break;
			case KeyEvent.RELEASE:
			event(false);
			break;
		}
	}

	public void mouseEvent(MouseEvent e) {
		a.println(a.frameCount, "input mouse");
		pmouseX = mouseX;
		pmouseY = mouseY;
		mouseX = e.getX();
		mouseY = e.getY();

		switch (e.getAction()) {
			case MouseEvent.PRESS:
			mpress(mouseX, mouseY);
			break;
			case MouseEvent.RELEASE:
			mreleased(mouseX, mouseY);
			break;
			case MouseEvent.CLICK:
			break;
			case MouseEvent.DRAG:
			break;
			case MouseEvent.MOVE:
			break;
		}
	}

	public void update() {
		mouseWheel = 0;
		if (press) {
			pressCount++;
		}
		click = dclick = released = false;
		kclick = kreleased = false;

		ENTER.update();
		BACKSPACE.update();
		ALT.update();
		CTRL.update();
		SHIFT.update();
		UP.update();
		DOWN.update();
		LEFT.update();
		RIGHT.update();
	}

	public void mpress(int mx, int my){
		amouseX = mx;
		amouseY = my;
		click = true;
		press = true;
	}

	public void mreleased(int mx, int my) {
		pressCount = 0;
		released = true;
		press = false;
		if (a.millis() - timepress < 400)
			dclick = true;
		timepress = a.millis();
	}

	public void event(boolean estado) {
		if (estado) {
			kclick = true;
			kpress = true;
		} else {
			kreleased = true;
			press = false;
		}
		if (a.keyCode == 10) ENTER.event(estado);
		if (a.keyCode == 8) BACKSPACE.event(estado);
		if (a.keyCode == 18) ALT.event(estado);
		if (a.keyCode == 17) CTRL.event(estado);
		if (a.keyCode == 16) SHIFT.event(estado);
		if (a.keyCode == a.UP) UP.event(estado);
		if (a.keyCode == a.DOWN) DOWN.event(estado);
		if (a.keyCode == a.LEFT) LEFT.event(estado);
		if (a.keyCode == a.RIGHT) RIGHT.event(estado);
	}
}