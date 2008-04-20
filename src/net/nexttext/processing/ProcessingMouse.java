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

package net.nexttext.processing;

import net.nexttext.input.Mouse;
import net.nexttext.input.MouseInputEvent;
import processing.core.PApplet;
import java.awt.event.MouseEvent;

/**
 * The ProcessingMouse is a Mouse InputSource for Processing 
 * which is automatically updated as the sketch is running.
 */
/* $Id$ */
public class ProcessingMouse extends Mouse {
	
	private int mX;
	private int mY;
	private boolean[] buttonPressStatus;
	
    /**
     * Instantiates the ProcessingMouse.
     * 
     * @param p the parent PApplet
     */
	public ProcessingMouse(PApplet p) {
		p.registerMouseEvent(this);
		
		buttonPressStatus = new boolean[4];
	}
	
	/**
     * Get the current x position of the mouse.
     */
    public int getX() {
    	return mX;
    }
    
    /**
     * Get the current y position of the mouse.
     */
    public int getY() {
    	return mY;
    }
    
    /**
     * Gets whether the specified mouse button is pressed.
     */
    public boolean isPressed(int button) {
    	return buttonPressStatus[button];
    }

    /**
     * Handles a MouseEvent.
     * <p>Registered to be called automatically by the PApplet.</p>
     * 
     * @param event
     */
	public void mouseEvent(MouseEvent event) {
		mX = event.getX();
		mY = event.getY();
		addEvent(new MouseInputEvent(event));

		switch (event.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				buttonPressStatus[event.getButton()] = true;
				break;
			case MouseEvent.MOUSE_RELEASED:
				buttonPressStatus[event.getButton()] = false;
				break;
			case MouseEvent.MOUSE_CLICKED:
				break;
			case MouseEvent.MOUSE_DRAGGED:
				break;
			case MouseEvent.MOUSE_MOVED:
				break;
		}
	}
}
