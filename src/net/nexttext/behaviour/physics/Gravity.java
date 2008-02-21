//
//Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.physics;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.property.NumberProperty;

/**
 * Applies a constant downwards acceleration to an object.
 */
public class Gravity extends PhysicsAction {

    public Gravity ( double strength ) {
        properties().init( "Strength", new NumberProperty(strength) );
    }
    
    public ActionResult behave( TextObject to) {
        
        Vector3 acc = new Vector3(0, ((NumberProperty)properties().get("Strength")).get() );
        applyAcceleration(to, acc);
        
        return new ActionResult(false, false, false);
    }
}
