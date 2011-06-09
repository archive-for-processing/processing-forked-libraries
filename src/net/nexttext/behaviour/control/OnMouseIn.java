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
import net.nexttext.property.BooleanProperty;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;

/**
 * A Condition which is true when the mouse moves over the TextObject i.e. a 
 * single true result is returned if the mouse was off of the TextObject and
 * moved over it.
 */
/* $Id$ */
public class OnMouseIn extends OnMouseOver {
    
    private BooleanProperty isOver;
    private boolean wasOver;
    
    /**
     * Creates an OnMouseIn which performs the given Action when the mouse
     * moves over the TextObject.
     *
     * @param trueAction the Action to perform when the mouse moves over the TextObject
     */
    public OnMouseIn(Action trueAction) {
        super(trueAction, new DoNothing());
        
        isOver = null;
        wasOver = false;
    }

    /**
     * Checks whether or not the mouse moved over the given TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
    	//create a BooleanProperty for each TextObject to avoid shared variables 
    	if(to.getProperty("isOver") == null)
        	to.init("isOver", new BooleanProperty(false));
    	
    	//get the "isOver" property which is true when the mouse is over the text object
    	isOver = (BooleanProperty)to.getProperty("isOver");
    	
    	wasOver = isOver.get();
        isOver.set(super.condition(to));
        if (!wasOver && isOver.get()) {
            return true;
        }
        return false;
    }
}
