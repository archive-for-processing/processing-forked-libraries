package manoloide.Input;

import processing.core.*;

public class Key {
	public boolean press, click;
	public int clickCount;
	
	public void update() {
		/*
		if (!focused)
			release();*/
		click = false;
		if (press)
			clickCount++;
	}

	public void press() {
		if (!press) {
			click = true;
			press = true;
			clickCount = 0;
		}
	}

	public void release() {
		press = false;
	}

	public  void event(boolean estado) {
		if (estado)
			press();
		else
			release();
	}
}
