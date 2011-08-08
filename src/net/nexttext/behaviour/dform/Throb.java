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

import net.nexttext.TextObjectGlyph;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorListProperty;
import net.nexttext.property.PVectorProperty;

import java.awt.Rectangle;

import java.util.Iterator;

import processing.core.PVector;

/**
 * A DForm which throbs the TextObject.
 *
 * <p>Think of throb as a multiplication of the size of the object, which
 * changes over time.  </p>

 * <p>In order to improve interoperability with other DForms, the period of the
 * throb is tracked as a frame count specific to each TextObjectGlyph.  This
 * way, each time the behaviour is called it modifies the control points of the
 * Glyph by multiplying them by the appropriate factor, thus preserving any
 * other modifications.  </p>
 *
 * <p>The following calculation defines throb with a period <code>p</code> and
 * scale of <code>s</code>.  Given a vector <code>c</code> from the center of
 * the glyph to one of its control points, it's throbbed value for frame
 * <code>f</code> is 
 * <pre>p * ( ( s - 1 ) * ( ( cos( f / p * 2PI - PI ) ) + 1 ) + 1 ) </pre>
 * </p>
 *
/* $Id$ */
public class Throb extends DForm {
	
	//used this variable to record when the period and scale are set to be changed
	private boolean changeParams;
	private float scale;
	private int period;
    
    /**
     * @param scale is amount the object's size will increase, as a multiplier.
     * @param period is the period of the throb, in frames.
     */
    public Throb(float scale, int period) {
        properties().init("Scale", new NumberProperty(scale));
        properties().init("Period", new NumberProperty(period));
        //These variables are only used when the user wishes to change the period and scale once
        //the behaviour has started
        changeParams = false;
        this.scale = scale;
        this.period = period;
    }

    public ActionResult behave(TextObjectGlyph to) {
        // Get the cached previous frameCount
        Integer fCObj = (Integer) (textObjectData.get(to));
        if (fCObj == null) { fCObj = new Integer(0); }
        int fC = fCObj.intValue() + 1;
        textObjectData.put(to, new Integer(fC));

        // The amount to multiply each control point by.  The factor to
        // generate the current frame from the origin, divided by the factor to
        // generate the previous frame from the origin.
        float scale = ((NumberProperty) properties().get("Scale")).get();
        long period = ((NumberProperty) properties().get("Period")).getLong();

        float factor = tF(fC, scale, period) / tF(fC - 1, scale, period);

        // Determine the center of to, in the same coordinates as the control
        // points will be.
        PVector toAbsPos = to.getPositionAbsolute();
        Rectangle bb = to.getBoundingPolygon().getBounds();
        PVector center = new PVector((float)bb.getCenterX(), (float)bb.getCenterY());
        center.sub(toAbsPos);

        // Traverse the control points of the glyph, applying the
        // multiplication factor to each one, but offset from the center, not
        // the position.
        PVectorListProperty cPs = getControlPoints(to);
        Iterator<PVectorProperty> i = cPs.iterator();
        while (i.hasNext()) {
        	PVectorProperty cP = i.next();
            // Get the vector from the center of the glyph to the control point.
        	PVector p = cP.get();
            p.sub(center);

            // Scale the control point by the appropriate factor
            p.mult(factor);

            // Return p to the original coordinates
            p.add(center);

            // Install p as the property
            cP.set(p);
        }
        if (fC % period == 0) {
        	if(changeParams) {
            	((NumberProperty)properties().get("Scale")).set(this.scale);
                ((NumberProperty)properties().get("Period")).set(this.period);
                changeParams = false;
            }
            return new ActionResult(false, false, true);
        } else {
            return new ActionResult(false, false, false);
        }
    }

    /**
     * The amount to multiply a vector by on the specified frame.
     */
    private float tF(int frame, float scale, long period) {
        float phase = (float)(Math.PI * 2 * frame / period);
        return ((float)(Math.cos(phase - Math.PI) + 1) * (scale - 1 )) + 1;
    }
    
    /**
     * Set function to change the period and scale
     *@param scale
     *@param period
     */
    public void set(float scale, int period) {
    	this.scale = scale;
    	this.period = period;
    	changeParams = true;
        
    }
    
     
}
