//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.physics;

import net.nexttext.PropertySet;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.Action;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * Stop an object from moving by setting its velocity and angular velocity to 0.
 */

public class Stop extends PhysicsAction {

    static final String REVISION = "$CVSHeader$";

    public ActionResult behave(TextObject to) {

        Vector3Property velocity = getVelocity(to);
        velocity.set(new Vector3());

        NumberProperty angVel = getAngularVelocity(to);
        angVel.set(0);

        return new ActionResult(true, true, false);
    }
}
