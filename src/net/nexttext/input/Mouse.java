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

import net.nexttext.*;

import java.awt.event.MouseEvent;

/**
 * An input source for mouse information.
 */
/* $Id$ */
public abstract class Mouse extends InputSource implements Locatable {

    static public int BUTTON1 = MouseEvent.BUTTON1;
    static public int BUTTON2 = MouseEvent.BUTTON2;
    static public int BUTTON3 = MouseEvent.BUTTON3;

    /**
     * Get if the specified button is pressed or not
     *
     * @param button is a static button definition.
     * @return  if the specified button is pressed.
     */
    public abstract boolean isPressed(int button);
    
    /**
     * Get the current x position of the mouse
     */
    public abstract int getX();
    
    /**
     * Get the current y position of the mouse
     */
    public abstract int getY();

    public Vector3 getPosition() { return new Vector3(getX(),getY()); }

    /**
     * Locatable interface.
     */
    public Vector3 getLocation() { return getPosition(); }
}
