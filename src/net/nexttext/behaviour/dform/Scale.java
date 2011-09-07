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

import java.awt.Rectangle;
import net.nexttext.TextObjectGlyph;
import processing.core.PVector;

/**
 * A DForm which scales the size of a TextObject.
 *
 */
/* $Id$ */
public class Scale extends DForm {
    
    private float scale;
    
    /**
     * @param scale is amount the object's size will increase, as a multiplier. 
     */
    public Scale(float scale) {
        this.scale = scale;        
    }

    public ActionResult behave(TextObjectGlyph to) {
    	// Determine the center of to, in the same coordinates as the verts will be.
    	PVector toAbsPos = to.getPositionAbsolute();
        Rectangle bb = to.getBoundingPolygon().getBounds();
        PVector center = new PVector((float)bb.getCenterX(), (float)bb.getCenterY());
        center.sub(toAbsPos);
        
        // Traverse the verts of the glyph, applying the multiplication factor to each one, 
        // but offset from the center, not the position.
        float[][] verts = to.getTessData().get().vertices;
        for (int i = 0; i < verts.length; i++) {
        	verts[i][0] = ((verts[i][0] - center.x) * scale) + center.x;
        	verts[i][1] = ((verts[i][1] - center.y) * scale) + center.y;
		}
        
        to.setDeformed(true);
        
        return new ActionResult(true, true, false);       
    }
    
    public void set(float scale) {
        this.scale = scale;
    }
}
