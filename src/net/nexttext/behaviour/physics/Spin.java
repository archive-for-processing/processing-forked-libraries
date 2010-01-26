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
import net.nexttext.behaviour.Action.ActionResult;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;
import processing.core.PVector;

/**
 * This action gives the object a one-time angular spin.
 */
public class Spin extends PhysicsAction {
    /** 
     * Default constructor. Angular force is equal to 0.1.
     */
    public Spin() {
        init(0.1f);
    }

    /** 
     * Default constructor. Angular force is equal to 0.1.
     */
    public Spin(float force) {
        init(force);
    }
    
    public void init(float force) {
        properties().init("Force", new NumberProperty( force ) );
    }

    public ActionResult behave( TextObject to ) {
        // get a push vector in a random direction
        float force = ((NumberProperty)properties().get("Force")).get();
        
        // apply the angular force
        this.applyAngularForce(to, force);
               
        // all done
        return new ActionResult(true, true, true);
    }
}
