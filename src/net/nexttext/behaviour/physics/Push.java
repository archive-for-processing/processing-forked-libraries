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

package net.nexttext.behaviour.physics;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * This action gives the object a one-time velocity push in a given
 * direction.
 */
public class Push extends PhysicsAction {

    /** 
     * Default constructor. Force is equal to 3.
     */
    public Push() {
        init(new Vector3(1.0, 0.0, 0.0), 3);
    }
    
    public Push(Vector3 direction, float force) {
    	init(direction, force);
    }
    
    public void init(Vector3 direction, float force) {
        properties().init("Direction", new Vector3Property( direction ) );
        properties().init("Force", new NumberProperty( force ) );
    }

    public ActionResult behave( TextObject to ) {
        
        // get a push vector in a random direction
    	Vector3 push = ((Vector3Property)properties.get("Direction")).get();
        double force = ((NumberProperty)properties().get("Force")).get();
        push.normalize();
        push.scalar(force);
        
        // add the push vector to the velocity
        Vector3Property velProp = getVelocity(to);
        Vector3 vel = velProp.get();
        vel.add(push);
        velProp.set(vel);
               
        // all done
        return new ActionResult(true, true, true);
    }
}
