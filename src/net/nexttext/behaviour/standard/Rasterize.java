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

package net.nexttext.behaviour.standard;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.renderer.pages.TextObjectRasterizer;

/**
 * Action to send textObjects to a TextObjectRasterizer.
 * 
 * <p>This allows the textObject to be rasterized and removed from the
 * textObject tree. </p>
 * 
 */
/* $Id$ */
public class Rasterize extends AbstractAction {
   
    TextObjectRasterizer rasterizer;
    ActionResult result = new ActionResult(true, true, false);

    public Rasterize(TextObjectRasterizer rasterizer){
        this.rasterizer = rasterizer;
    }
    
    public ActionResult behave( TextObject to ) {
        rasterizer.rasterize((TextObjectGroup)to);
        return result;
    }
}
