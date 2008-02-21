//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;

/**
 * The Kill action flags an object for removal from the {@link net.nexttext.Book}, causing it 
 * to cease to exist completely. 
 * 
 * <p>Note that the object is not immediately removed for synchronization 
 * reasons.  Rather, it will be eliminated at the end of the current frame. </p>
 * 
 * <p>Killing a {@link net.nexttext.TextObjectGroup} will destroy all of its children as 
 * well. </p>
 * 
 * $Id$
 */
public class Kill extends AbstractAction {
    
    /**
     * Kills a TextObject.   
     * 
     * <p>See class comments for details on the exact time of death. </p>
     * 
     * @return ActionResult will always be set as complete, although this is
     * really a formality because apply Kill will cause an object to be removed
     * from any Behaviour regardless.
     * 
     * @throws NullPointerException if the object has not been attached to 
     * a Book. 
     */
    public ActionResult behave(TextObject to) {
         
        // find the TextObject root
        to.getBook().removeObject(to);            
        return new ActionResult(true, true, false);
    }
}
