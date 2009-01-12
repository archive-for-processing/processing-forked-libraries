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

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.Property;

/**
 * Causes TextObjects to follow their left siblings.
 *
 * <p>The provided TargettingAction is used to have TextObjects follow their
 * left siblings.  </p>
 */
/* $Id$ */
public class Follow extends AbstractAction {

    TargetingAction action;
    
    public Follow ( TargetingAction action ) {
        this.action = action;
    }
    
    public ActionResult behave( TextObject to ) {
        
        TextObject sibling = to.getLeftSibling();
        
        if ( sibling != null ) {   
             action.setTarget( sibling );
             return action.behave(to);
        }
        return new ActionResult(false, false, false);
    }

    public Map<String, Property> getRequiredProperties() {
        return action.getRequiredProperties();
    }
}
