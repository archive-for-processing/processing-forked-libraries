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

import java.util.HashMap;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Property;

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
/* $Id$ */
public abstract class PhysicsAction extends AbstractAction {

    /**
     * Returns a Map containing a set of Vector3Properties required by all
     * PhysicActions
     */
    public Map<String, Property> getRequiredProperties() {

        Map<String, Property> properties = new HashMap<String, Property>();

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
