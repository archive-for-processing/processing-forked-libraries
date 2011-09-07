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

/**
 * A DForm which reverts TextObject to its original shape.
 *
 * <p>Different ways of reforming the glyphs are provided, which given
 * different visual effects.  </p>
 */
/* $Id$ */
public class Reform extends DForm {
    
    public static final int STYLE_LINEAR = 1;
    public static final int STYLE_EXPONENTIAL = 2;    
    
    int style = STYLE_LINEAR;
    
    float linearSpeed = 0.05f;
    float exponentialSpeed = 2000;

    /**
     * Constructs a default Reform of linear style with a default speed of 0.05.
     */
    public Reform() {
    }
    
    /**
     * Constructs a custom Reform with given style and appropriate speed.
     *
     * <p>In exponential style, smaller values give faster reforms, the default 
     * value is 2000.</p>
     *
     * <p>In linear style, smaller values give slower reforms, the default 
     * value is 0.05</p>.
     *
     * @param speed the speed value according to the chosen style
     * @param style the type of reformation (linear or exponential)
     */
    public Reform(float speed, int style) {
        
        if (style == STYLE_LINEAR) {
            this.style = STYLE_LINEAR;
            linearSpeed = speed;
            
        } else if (style == STYLE_EXPONENTIAL) {
            this.style = STYLE_EXPONENTIAL;
            exponentialSpeed = speed;
            
        }
    }

    /**
     *  Traverse the verts of the glyph, determine the distance
     *  from its current location to the origin and move it part way there.
     */
    public ActionResult behave(TextObjectGlyph to) {         
        boolean done = true;
        
        // if the glyph is not deformed, don't waste time reforming it
        if (!to.isDeformed())
            return new ActionResult(false, false, false);
        
        float[][] verts = to.getTessData().get().vertices;
        float[][] origs = to.getTessData().getOriginal().vertices;
        float[] offset = new float[2];
        float mag, scalar;
        for (int i = 0; i < verts.length; i++) {
        	offset[0] = origs[i][0] - verts[i][0];
        	offset[1] = origs[i][1] - verts[i][1];
        	
        	mag = (float)Math.sqrt(offset[0]*offset[0] + offset[1]*offset[1]);
        	
        	// In order not to produce gratuitous property change events, if
            // the offset is short, nothing is done.
            if (mag < 0.1f) continue;
            
            // The reform algorithm is very slow when the points are close, so
            // once we reach a distance of 0.8 we just snap it back to its
            // original.
            if (mag > 0.8f) {
            	done = false;
            	
            	if (style == STYLE_EXPONENTIAL) {
            		scalar = 1 - (float)Math.pow(Math.E, -mag / exponentialSpeed);
            		offset[0] *= scalar;
                	offset[1] *= scalar;
                } else {
                	offset[0] *= linearSpeed;
                	offset[1] *= linearSpeed;
                }
            }
            
            verts[i][0] += offset[0];
            verts[i][1] += offset[1];
		}

        if (done) {
        	to.setDeformed(false);
            return new ActionResult(true, true, false);
        }
        
        return new ActionResult(false, true, false);
    }    

    /**
     * Sets the speed and the style of the reform
     * @param speed
     * @param style
     */
    public void set(float speed, int style) {
        
        if (style == STYLE_LINEAR) {
            this.style = STYLE_LINEAR;
            linearSpeed = speed;
            
        } else if (style == STYLE_EXPONENTIAL) {
            this.style = STYLE_EXPONENTIAL;
            exponentialSpeed = speed;
            
        }
    }
    
}
