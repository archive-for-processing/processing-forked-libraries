package manoloide.Input;

import java.util.ArrayList;
import processing.core.*;

public class Input {
	private PApplet myParent;
	public boolean click, dclick, press, released, kclick, kpress, kreleased;
	public int amouseX, amouseY;
	public int pressCount, mouseWheel, timepress;
	public Key ENTER, BACKSPACE, ALT, CTRL, SHIFT, ARRIBA, ABAJO, IZQUIERDA, DERECHA;

	public Input(PApplet myParent) {
		this.myParent = myParent;
		click = dclick = released = press = false;
		kclick = kreleased = kpress = false;
		pressCount = 0;
	}

	public void act() {
		mouseWheel = 0;
		if (press) {
			pressCount++;
		}
		click = dclick = released = false;
		kclick = kreleased = false;
		
		ENTER = new Key();
	    BACKSPACE = new Key();
	    ALT = new Key();
	    CTRL = new Key();
	    SHIFT = new Key();
	    ARRIBA = new Key();
	    ABAJO = new Key();
	    IZQUIERDA = new Key();
	    DERECHA = new Key();
	}

	public void mpress() {
		amouseX = myParent.mouseX;
		amouseY = myParent.mouseY;
		click = true;
		press = true;
	}

	public void mreleased() {
		released = true;
		press = false;
		if (myParent.millis() - timepress < 400)
			dclick = true;
		timepress = myParent.millis();
	}

	public void event(boolean estado) {
		if (estado) {
			kclick = true;
			kpress = true;
		} else {
			kreleased = true;
			press = false;
		}
	    if (myParent.keyCode == 10) ENTER.event(estado);
	    if (myParent.keyCode == 8) BACKSPACE.event(estado);
	    if (myParent.keyCode == 18) ALT.event(estado);
	    if (myParent.keyCode == 17) CTRL.event(estado);
	    if (myParent.keyCode == 16) SHIFT.event(estado);
	    if (myParent.keyCode == myParent.UP) ARRIBA.event(estado);
	    if (myParent.keyCode == myParent.DOWN) ABAJO.event(estado);
	    if (myParent.keyCode == myParent.LEFT) IZQUIERDA.event(estado);
	    if (myParent.keyCode == myParent.RIGHT) DERECHA.event(estado);
	}
}