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

package net.nexttext.behaviour.dform;

import net.nexttext.CoordinateSystem;
import net.nexttext.Locatable;
import net.nexttext.TextObjectGlyph;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorListProperty;
import net.nexttext.property.PVectorProperty;

import java.util.Iterator;

import processing.core.PVector;

/**
 * A DForm which pulls the TextObject towards the mouse.
 */
/* $Id$ */
public class Pull extends DForm implements TargetingAction {
    
    Locatable target;

    public void setTarget( Locatable target ) {
        this.target = target;
    }

    /**
     * @param speed is the speed with which the points are pulled.
     * @param reach will pull farther points faster with a higher value.
     */
    public Pull(Locatable target, float speed, float reach) {
        this.target = target;
        properties().init("Speed", new NumberProperty(speed));
        properties().init("Reach", new NumberProperty(reach));
    }

    public ActionResult behave(TextObjectGlyph to) {
        float speed = ((NumberProperty)properties().get("Speed")).get();
        float reach = ((NumberProperty)properties().get("Reach")).get();

        // Get the position of the target relative to the TextObject.
        CoordinateSystem ac = to.getAbsoluteCoordinateSystem();
        PVector targetV = target.getLocation();
        targetV = ac.transformInto( targetV );
         
        // Traverse the control points of the glyph, determine the distance
        // from it to the target and move it part way there.
        PVectorListProperty cPs = getControlPoints(to);
        Iterator<PVectorProperty> i = cPs.iterator();
        while (i.hasNext()) {
        	PVectorProperty cP = i.next();
        	PVector p = cP.get();

        	PVector offset = new PVector(targetV.x, targetV.y, targetV.z);
            offset.sub(p);

            offset.mult(1 / (float)Math.pow(1 + 1 / reach, offset.mag() / speed));

            p.add(offset);
            cP.set(p);
        }
        return new ActionResult(false, false, false);
    }
}
