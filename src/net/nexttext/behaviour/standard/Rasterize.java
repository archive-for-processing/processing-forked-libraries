//
//  Copyright (C) 2006 Jason Lewis
//

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
public class Rasterize extends AbstractAction {
    
    static final String REVISION = "$CVSHeader:";

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
