/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

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
/* $Id$ */
public class MouseDefault extends Mouse implements MouseListener,
                                                   MouseMotionListener {

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
