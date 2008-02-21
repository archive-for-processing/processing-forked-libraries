//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour.standard;

import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;

/**
 * Causes TextObjects to follow their left siblings.
 *
 * <p>The provided TargettingAction is used to have TextObjects follow their
 * left siblings.  </p>
 */
public class Follow extends AbstractAction {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/standard/Follow.java,v 1.5 2005/10/18 20:51:07 dissent Exp $";

    TargetingAction action;
    
    public Follow ( TargetingAction action ) {
        this.action = action;
    }
    
    public ActionResult behave( TextObject to ) {
        
        TextObject sibling = to.getLeftSibling();
        
        if ( sibling != null ) {   
             action.setTarget( sibling );
             return action.behave(to);
        }
        return new ActionResult(false, false, false);
    }

    public Map getRequiredProperties() {
        return action.getRequiredProperties();
    }
}
