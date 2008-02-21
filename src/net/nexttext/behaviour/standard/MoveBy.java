//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.Locatable;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.Vector3Property;

/**
 * Move an object by the given vector amount.
 *
 * <p>The vector amount is added to its position.  The value of the Locatable
 * is checked each time behave() is called, so a variable amount can be
 * used.</p>
 */
public class MoveBy extends AbstractAction implements TargetingAction {

    static final String REVISION = "$CVSHeader$";

    protected Locatable offset;

    public MoveBy(double x, double y) {
    	this(new Vector3(x, y));
    }
    
    public MoveBy(Locatable offset) {
        this.offset = offset;
    }

    public ActionResult behave(TextObject to) {
        Vector3Property posProp = getPosition(to);
        posProp.add(offset.getLocation());
        return new ActionResult(false, false, false);
    }

    /**
     * Sets an offset to move by.
     */
    public void setTarget(Locatable offset) {
        this.offset = offset;
    }
}
