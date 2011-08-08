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

import processing.core.PVector;
import net.nexttext.TextObject;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;

/**
 * This action gives the object a one-time velocity push in a random
 * direction. A random angular velocity push can also be added, but 
 * note that the angular push is a little computationally expensive, 
 * thus don't use it with too many letters. 
 */
/* $Id$ */
public class Explode extends PhysicsAction {
    
    /** 
     * Default constructor. Force is equal to 3. Angular force is zero.
     */
    public Explode() {
        init(3,0);
    }
    /**
     * Creates an explode with a specified force and default angular fore of zero.
     * @param force strength of the explosion
     */
    public Explode( float force ) {
        init(force, 0);
    }
    
    /**
     * Creates an explode with specified force and angular force.
     * @param force strength of the explosion
     * @param angularForce average strength of the angualr force on objects
     */
    public Explode( float force, float angularForce ) {
        init(force,angularForce);
    }
    
    private void init( float force, float angularForce ) {
        properties().init("Force", new NumberProperty( force ) );
        properties().init("AngularForce", new NumberProperty( angularForce ) );
    }
    
    public ActionResult behave( TextObject to ) {
        
        // get a push vector in a random direction
    	PVector push = new PVector(1,1);
    	
        // because the screen coordinate are mirrored along the X axis (ie: a 
        // negative Y means going out of the screen), we must first negate angle
        // to get clockwise rotation.  
        // the result will be a rotation that is looks clockwise on screen.
        float angle = -(float)(Math.random()*(2*Math.PI));
               
        //rotate
        push.set(push.x * (float)Math.cos(angle) + push.y * (float)Math.sin(angle),
        		 - push.x * (float)Math.sin(angle) + push.y * (float)Math.cos(angle),
        		 0);
        
        float force = ((NumberProperty)properties().get("Force")).get();        
        push.mult(force);
        
        // add the push vector to the velocity
        PVectorProperty velProp = getVelocity(to);
        PVector vel = velProp.get();
        vel.add(push);
        velProp.set(vel);
        
        //Add a random angular force whose average depends on our chosen value
        float angForce = ((NumberProperty)properties().get("AngularForce")).get(); 
        angForce = angForce * (float)(Math.random());
        ((NumberProperty)to.getProperty("AngularForce")).set(angForce);
        
        // all done
        return new ActionResult(true, true, true);
    }
    /**
     * Changes the force of the explosion at any time 
     * @param force new strength of the random push
     */
    public void set(float force) {
    	((NumberProperty)properties().get("Force")).set(force);
    }
    /**
     * Changes the force and the angular force of the explosion at any time
     * @param force new strength of the random push
     * @param angularForce new average angular force of the explosion
     */
    public void set(float force, float angularForce) {
    	((NumberProperty)properties().get("AngularForce")).set(angularForce);
    }
}
