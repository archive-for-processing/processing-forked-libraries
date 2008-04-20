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

import net.nexttext.Locatable;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.Vector3ArithmeticException;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * Move an object to the location.
 */
/* $Id$ */
public class MoveTo extends AbstractAction implements TargetingAction {

    protected Locatable target;

    /**
     * 
     */
    public MoveTo(int x, int y, long speed) {
    	this(new Vector3(x, y), speed);
    }
    
    /**
	 * @param speed The speed of the approach represented as the number of
	 * pixels to move in each frame.  Use a very large number for instant
	 * travel.
     */
    public MoveTo( Locatable target, long speed ) {
        this.target = target;
        properties().init("Speed", new NumberProperty(speed));
    }

    /**
     * Add a vector to the position to bring it closer to the target.
     *
     * <p>Result is complete if it has reached its target. </p>
     */
    public ActionResult behave(TextObject to) {
        double speed = ((NumberProperty)properties().get("Speed")).get();

        // get the vector from the position to the target
        Vector3 pos = to.getPositionAbsolute();
        Vector3 newDir = target.getLocation();
	 	newDir.sub(pos);

        ActionResult result = new ActionResult(true, true, false);

	 	// Scale the vector down to the speed if needed.
        if (newDir.length() > speed) {
            try {
                newDir.normalize();
            } catch (Vector3ArithmeticException v3ae) {
                // some silly person set a negative speed, and the object had
                // already arrived at it's location, just ignore the problem.
            }
            newDir.scalar(speed);
            result.complete = false;
        }
        Vector3Property posProp = getPosition(to);
        posProp.add(newDir);
        return result;
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(Locatable target) {
       	this.target = target;
    }
}
