//
// Copyright 2004 Jason Lewis
//

package net.nexttext.input;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * An interface for the mouse.
 *
 * <p>The mouse interface listen to every mouse event and stores them in a 
 * list as {@link MouseEvent} objects.  It also keeps the current status of 
 * the mouse buttons and the current x and y position.</p>
 */

public class MouseDefault extends Mouse implements MouseListener,
                                                   MouseMotionListener {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/input/MouseDefault.java,v 1.1.4.1 2005/03/30 19:04:59 david_bo Exp $";

	// The current x position of the mouse
	int x = 0;
	
	// The current y position of the mouse
	int y = 0;
	
	// The status of the buttons
	boolean button1 = false;
	boolean button2 = false;
	boolean button3 = false;

	/**
	 * Class constructor
	 *
	 * @param	component	the component the mouse is added to
	 */
	public MouseDefault(Component component) {
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
	}
	
	/**
	 * Get if the specified button is pressed or not
	 *
	 * @param	button	the button to return the status
	 * @return			true if the specified button is pressed
	 */
	public boolean isPressed(int button) {
		if (button == MouseEvent.BUTTON1) { return button1; }
		else if (button == MouseEvent.BUTTON2) { return button2; }
		else if (button == MouseEvent.BUTTON3) { return button3; }
		else { return false; }
	}
	
	/**
	 * Get the current x position of the mouse
	 */
	public int getX() {	return x; }
	
	/**
	 * Get the current y position of the mouse
	 */
	public int getY() {	return y; }

	// Mouse listener
	public void mouseClicked(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
	}

	public void mouseEntered(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
	}

	public void mouseExited(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
	}

	public void mousePressed(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
		if (event.getButton() == MouseEvent.BUTTON1) { button1 = true; }
		else if (event.getButton() == MouseEvent.BUTTON2) {	button2 = true;	}
		else if (event.getButton() == MouseEvent.BUTTON3) {	button3 = true;	}
	}

	public void mouseReleased(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
		if (event.getButton() == MouseEvent.BUTTON1) { button1 = false; }
		else if (event.getButton() == MouseEvent.BUTTON2) {	button2 = false; }
		else if (event.getButton() == MouseEvent.BUTTON3) {	button3 = false; }
	}
	
	// Mouse motion listener
	public void mouseMoved(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
		x = event.getX();
		y = event.getY();
	}
	public void mouseDragged(MouseEvent event) {
		addEvent(new MouseInputEvent(event));
		x = event.getX();
		y = event.getY();
	}

}
