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
import net.nexttext.Vector3;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3PropertyList;
import net.nexttext.property.Vector3Property;

import java.util.Iterator;

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
    public Pull(Locatable target, double speed, double reach) {
        this.target = target;
        properties().init("Speed", new NumberProperty(speed));
        properties().init("Reach", new NumberProperty(reach));
    }

    public ActionResult behave(TextObjectGlyph to) {
        double speed = ((NumberProperty)properties().get("Speed")).get();
        double reach = ((NumberProperty)properties().get("Reach")).get();

        // Get the position of the target relative to the TextObject.
        CoordinateSystem ac = to.getAbsoluteCoordinateSystem();
        Vector3 targetV = target.getLocation();
        targetV = ac.transformInto( targetV );
         
        // Traverse the control points of the glyph, determine the distance
        // from it to the target and move it part way there.
        Vector3PropertyList cPs = getControlPoints(to);
        Iterator<Vector3Property> i = cPs.iterator();
        while (i.hasNext()) {
            Vector3Property cP = i.next();
            Vector3 p = cP.get();

            Vector3 offset = new Vector3(targetV);
            offset.sub(p);

            offset.scalar(1 / Math.pow(1 + 1 / reach, offset.length() / speed));

            p.add(offset);
            cP.set(p);
        }
        return new ActionResult(false, false, false);
    }
}
