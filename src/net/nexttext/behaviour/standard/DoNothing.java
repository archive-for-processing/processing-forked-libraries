//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.PropertySet;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.NumberProperty;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * Does nothing to a TextObject.
 */
public class DoNothing extends AbstractAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/standard/DoNothing.java,v 1.3 2005/10/18 20:51:07 dissent Exp $";

    ActionResult result;

    /**
     * Do nothing, returning the default ActionResult(false, false, false).
     */
    public DoNothing() {
        this(false, false, false);
    }

    /**
     * Do nothing, returning an ActionResult constructed with the given values.
     */
    public DoNothing(boolean complete, boolean canComplete, boolean event) {
        this.result = new Action.ActionResult(complete, canComplete, event);
    }

    /**
     * Does nothing to the TextObject.
     */
    public ActionResult behave(TextObject to) {
        return result;
    }
}
