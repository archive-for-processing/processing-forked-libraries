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

package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.input.MouseDefault;
//adding a Property to the text objects
import net.nexttext.property.BooleanProperty;

/**
 * A Condition which is true when a mouse button is pressed i.e. a single true result 
 * is returned if the button was up and got pressed down.
 */
/* $Id$ */
public class OnMousePressed extends OnMouseDepressed {
    
    private BooleanProperty isPressed;
    private boolean wasPressed;
    
    /**
     * Creates an OnMousePressed which performs the given Action when the mouse 
     * button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     */
    public OnMousePressed(Action trueAction) {
        this(MouseDefault.LEFT, trueAction);
    }
    
    /**
     * Creates an OnMousePressed which performs the given Action when the selected
     * mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     */
    public OnMousePressed(int buttonToCheck, Action trueAction) {
        super(buttonToCheck, trueAction);
        
        isPressed = null;
        wasPressed = false;
    }
    
    /**
     * Checks whether or not the selected mouse button is pressed.
     * 
     * @param to the TextObject to consider (unused)
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
    	//create a BooleanProperty for each TextObject to avoid shared variables 
        if(to.getProperty("isPressed") == null)
        	to.init("isPressed", new BooleanProperty(false));
        
        //get the "isPressed" property of the text object. It is true when the mouse is pressed.
    	BooleanProperty isPressed = (BooleanProperty)to.getProperty("isPressed");
    	
    	//compare wasPressed and isPressed
    	wasPressed = isPressed.get();
        isPressed.set( super.condition(to) );
        if (!wasPressed && isPressed.get() ) {
            
        	return true;
        }
        return false;
    }
    
}
