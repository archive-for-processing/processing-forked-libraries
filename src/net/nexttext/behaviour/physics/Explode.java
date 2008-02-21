//
//Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.physics;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * This action gives the object a one-time velocity push in a random
 * direction.
 */
public class Explode extends PhysicsAction {
    
    /** 
     * Default constructor. Force is equal to 3.
     */
    public Explode() {
        init(3,0);
    }

    public Explode( double force ) {
        init(force, 0);
    }
    
    public Explode( double force, double angularForce ) {
        init(force,angularForce);
    }
    
    private void init( double force, double angularForce ) {
        properties().init("Force", new NumberProperty( force ) );
        properties().init("AngularForce", new NumberProperty( angularForce ) );
    }
    
    public ActionResult behave( TextObject to ) {
        
        // get a push vector in a random direction
        Vector3 push = new Vector3(1,1);        
        double angle = Math.random()*(2*Math.PI);
        push.rotate(angle);        
        double force = ((NumberProperty)properties().get("Force")).get();        
        push.scalar(force);
        
        // add the push vector to the velocity
        Vector3Property velProp = getVelocity(to);
        Vector3 vel = velProp.get();
        vel.add(push);
        velProp.set(vel);
        
        //Add the angular force
        double angForce = ((NumberProperty)properties().get("AngularForce")).get();   
        ((NumberProperty)to.getProperty("AngularForce")).set(angForce);
        
        // all done
        return new ActionResult(true, true, true);
    }
}
