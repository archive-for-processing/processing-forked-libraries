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

import java.util.Iterator;

import net.nexttext.Locatable;
import net.nexttext.TextObjectGlyph;
import net.nexttext.Vector3;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.Vector3PropertyList;

/** 
 * ChaosPull is similar to {@link Pull} except that the control points get into a chaotic state when
 * they reach the target.
 *
 * TODO: add parameters.
 */
/* $Id$ */
public class ChaosPull extends DForm implements TargetingAction{

    Locatable target;
    int chaosStrength;
    
    public void setTarget( Locatable target ) {
        this.target = target;
    }
    
    public ChaosPull( Locatable target ) {
        this(target, 1200);
    }
    
    public ChaosPull( Locatable target, int chaosStrength ) {
        this.target = target;        
        this.chaosStrength = chaosStrength;
    }
    
    /* (non-Javadoc)
     * @see net.nexttext.behaviour.dform.DForm#behave(net.nexttext.TextObjectGlyph)
     */
    public ActionResult behave(TextObjectGlyph to) {    
        
        // Get the position of the target relative to the TextObject.
        Vector3 toAbsPos = to.getPositionAbsolute();
        Vector3 targetV = target.getLocation();
        targetV.sub(toAbsPos);

        // Traverse the control points of the glyph, determine the distance
        // from it to the target and move it part way there.
        Vector3PropertyList cPs = getControlPoints(to);
        Iterator i = cPs.iterator();
        while (i.hasNext()) {
            
            Vector3Property cP = (Vector3Property) i.next();
            Vector3 p = cP.get();
            
            Vector3 offset = new Vector3(targetV);
            offset.sub(p);
            
            double pullForce = chaosStrength/(offset.length()+25);
            offset.scalar( pullForce / offset.length() );
            
            p.add(offset);
            cP.set(p);  
        }
        
        return new ActionResult(false, false, false);
    }

    public int getChaosStrength() {
        return chaosStrength;
    }

    /**
     * Sets the 'strength' of the chaosPull, stronger chaosPull results in 
     * larger deformations and faster pulling.
     * 
     * <p>The default value is 1200.</p>
     */
    public void setChaosStrength(int chaosStrength) {
        this.chaosStrength = chaosStrength;
    }
}

