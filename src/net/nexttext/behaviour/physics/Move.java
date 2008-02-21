//
// Copyright (C) 2004,2005 Jason Lewis
//

package net.nexttext.behaviour.physics;

import net.nexttext.PropertySet;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.Action;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * This is the basic Move; actions from the physics package will have no effect
 * unless this behaviour is applied to objects.
 *
 * <p>It implements basic mechanics, updating position and velocity, rotation,
 * and angular velocity, based on force, angular force (torque), and mass.  On
 * each frame the force and angular force are reset to zero.  In addition, each
 * Move has its own drag coefficient, which is applied to every object's
 * velocity on each frame.  </p>
 *
 * <p>Physics actions will typically add to the force on an object on each
 * frame.  </p>
 */

public class Move extends PhysicsAction {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/physics/Move.java,v 1.4 2005/05/24 16:44:00 dissent Exp $";

    public Move() {
        this(0, 0);
    }

    /**
     * New Move action with specified drag.
     *
     * <p>Drag is a fraction of the velocity by which it will be scaled back.
     * So a drag of 1 will cause objects to halt completely, and 0 will have no
     * effect.  </p>
     */
    public Move(float drag, float angularDrag) {
        properties().init("Drag", new NumberProperty(drag));
        properties().init("AngularDrag", new NumberProperty(angularDrag));
    }

    /**
     * Applies Euler motion to a TextObject.
     */
    public ActionResult behave(TextObject to) {

        // Determine the acceleration to apply to the object.
        Vector3 acceleration = ((Vector3Property) to.getProperty("Force")).get();
        acceleration.scalar( 1 / getMass(to).get());

        // Update velocity based on the acceleration
        Vector3Property velocity = getVelocity(to);
        velocity.add(acceleration);

        // Apply the drag to the velocity
        velocity.scalar(1 - ((NumberProperty) properties.get("Drag")).get());

        // Update position based on velocity
        getPosition(to).add(velocity.get());

        // Reset the force to zero for the next frame.
        ((Vector3Property) to.getProperty("Force")).set(new Vector3());


        // Determine angular acceleration
        double angAcc = (((NumberProperty) to.getProperty("AngularForce")).get()
                         / getMass(to).get());

        // Update angular velocity based on the accelaration
        NumberProperty angVel = getAngularVelocity(to);
        angVel.add(angAcc);

        // Apply drag to angular velocity
        double angDrag = ((NumberProperty) properties.get("AngularDrag")).get();
        angVel.set(angVel.get() * (1 - angDrag));

        // Update rotation based on angular velocity
        getRotation(to).add(angVel.get());

        // Reset the force to zero for the next frame.
        ((NumberProperty) to.getProperty("AngularForce")).set(0);
        
        return new ActionResult(false, false, false);
    }
}
