//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.physics;

import java.util.HashMap;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.NumberProperty;

/**
 * Every Action in this package should be a descendant of this class.
 *
 * <p>PhysicsAction introduces a new set of physics properties, and
 * provide the usual accessors for them.  </p>
 *
 * <ul>
 * 	  <li>Mass</li>
 *    <li>Velocity</li>
 * 	  <li>Force</li>
 * 	  <li>AngularVelocity</li>
 * 	  <li>AngularForce  (Torque)</li>
 * </ul>
 *
 * <p>Properties to add in the future include elasticity and mass.  A method to
 * get absolute velocity may also be useful.  </p>
 */
public abstract class PhysicsAction extends AbstractAction {

    /**
     * Returns a Map containing a set of Vector3Properties required by all
     * PhysicActions
     */
    public Map getRequiredProperties() {

        Map properties = new HashMap();

        NumberProperty mass = new NumberProperty(1);
        properties.put("Mass", mass);
        Vector3Property velocity = new Vector3Property(0,0,0);
        properties.put("Velocity", velocity);
        Vector3Property force = new Vector3Property(0,0,0);
        properties.put("Force", force);
        NumberProperty angularVelocity = new NumberProperty(0);
        properties.put("AngularVelocity", angularVelocity);
        NumberProperty angularForce = new NumberProperty(0);
        properties.put("AngularForce", angularForce);

        return properties;
    }

    public NumberProperty getMass(TextObject to) {
        return (NumberProperty) to.getProperty("Mass");
    }

    public Vector3Property getVelocity(TextObject to) {
        return (Vector3Property) to.getProperty("Velocity");
    }

    public NumberProperty getAngularVelocity(TextObject to) {
        return (NumberProperty) to.getProperty("AngularVelocity");
    }

    /**
     * Applies a force to a TextObject.
     *
     * <p>The mass of the object will affect the resulting acceleration.  </p>
     */
    public void applyForce(TextObject to, Vector3 force) {
        Vector3Property totalForce = (Vector3Property) to.getProperty("Force");
        totalForce.add(force);
    }

    /**
     * Applies an acceleration to a TextObject.
     *
     * <p>This acceleration is independent of the mass of the object.  </p>
     */
    public void applyAcceleration(TextObject to, Vector3 acceleration) {
        Vector3Property totalForce = (Vector3Property) to.getProperty("Force");

        Vector3 newForce = new Vector3(acceleration);
        newForce.scalar(getMass(to).get());

        Vector3 newTotalForce = totalForce.get();
        newTotalForce.add(newForce);

        totalForce.set(newTotalForce);
    }

    /**
     * Applies an angular force (torque) to a TextObject.
     *
     * <p>The mass of the object will affect the resulting angular
     * acceleration.  </p>
     */
    public void applyAngularForce(TextObject to, double angularForce) {
        NumberProperty totalAngularForce =
            (NumberProperty) to.getProperty("AngularForce");
        totalAngularForce.set(totalAngularForce.get() + angularForce);
    }

    /**
     * Applies an angular acceleration to a TextObject.
     *
     * <p>This acceleration is independent of the mass of the object.  </p>
     */
    public void applyAngularAcceleration(TextObject to, double angAcc) {
        NumberProperty totalAngForce =
            (NumberProperty) to.getProperty("AngularForce");

        double newAngForce = angAcc * getMass(to).get();

        totalAngForce.set(totalAngForce.get() + newAngForce);
    }

}
