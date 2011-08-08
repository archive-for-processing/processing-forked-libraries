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

package net.nexttext.behaviour.standard;

import java.util.Map;

import processing.core.PApplet;
import processing.core.PConstants;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.Property;

/**
 * Causes TextObjects to follow their left or right siblings. If the objects follow their LEFT
 * sibling a loop forms where the left most object is set to follow the right most object and each 
 * object in between follows the object to their left. Very nice when use in conjunction with
 * {@link net.nexttext.behaviour.physics.Approach} and a means of moving the text objects around. If the text object are set to follow 
 * their RIGHT sibling the behaviour is quite different, the object at the end of a word will not 
 * follow anything while objects in the word will follow their right sibling. I have yet to 
 * figure out how to fix this abnormality, nonetheless you can still getting interesting behaviours.
 *
 * <p>The provided TargettingAction is used to have TextObjects follow either their
 * left or right siblings.  </p>
 */
/* $Id$ */
public class FollowSibling extends AbstractAction {

    TargetingAction action;
    int siblingDirection; // LEFT or RIGHT 
    
    /**
     * Follow default left sibling. 
     * @param action targeting action with any target. Follow sibling will set all text objects
     * to have their left sibling as their target.
     */
    public FollowSibling ( TargetingAction action ) {
    	this(PConstants.LEFT, action);
    } 
    
    /**
     * Follow left or right sibling.
     * @param siblingDirection LEFT or RIGHT sibling
     * @param action targeting action with any target. Each text object 
     * will have their sibling set as their target.
     */
    public FollowSibling ( int siblingDirection, TargetingAction action ) {
    	//make sure LEFT or RIGHT sibling is specified
    	if ((siblingDirection != PConstants.LEFT) && (siblingDirection != PConstants.RIGHT)) {
    		PApplet.println("Warning: the first argument of FollowSibling must be LEFT or RIGHT. Using default LEFT.");
    		siblingDirection = PConstants.LEFT;
    	}
    	
    	this.siblingDirection = siblingDirection;
    	this.action = action;
    }
    
    public ActionResult behave( TextObject to ) {
        
        TextObject sibling = null;
        if (siblingDirection == PConstants.LEFT) 
        	sibling = to.getLeftSibling();
        else if (siblingDirection == PConstants.RIGHT) 
        	sibling = to.getRightSibling();
        
        if ( sibling != null ) {   
             action.setTarget( sibling );
             return action.behave(to);
        }
        return action.behave(to);
    }

    public Map<String, Property> getRequiredProperties() {
        return action.getRequiredProperties();
    }
    
    /**
     * Change the targeting action to be used by text objects to target their siblings. Remember the
     * target of the targeting action does not matter when it is used for FollowSibling, because the 
     * target will be reset to the sibling
     * @param action targeting action with any target. Follow sibling will set all text objects
     * to have their left sibling as their target.
     */
    public void set ( TargetingAction action ) {
    	this.action = action;
    } 
    
    /**
     * Change the targeting action and the sibling direction.
     * @param siblingDirection LEFT or RIGHT sibling
     * @param action targeting action with any target. Each text object 
     * will have their sibling set as their target.
     */
    public void set ( int siblingDirection, TargetingAction action ) {
    	//make sure LEFT or RIGHT sibling is specified
    	if ((siblingDirection != PConstants.LEFT) && (siblingDirection != PConstants.RIGHT)) {
    		PApplet.println("Warning: the first argument of FollowSibling must be " +
    				"LEFT or RIGHT. Using default LEFT.");
    		siblingDirection = PConstants.LEFT;
    	}
    	
    	this.siblingDirection = siblingDirection;
    	this.action = action;
    }
    
}