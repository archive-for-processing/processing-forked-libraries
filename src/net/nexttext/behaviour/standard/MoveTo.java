//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.Locatable;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.Vector3ArithmeticException;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * Move an object to the location.
 */
public class MoveTo extends AbstractAction implements TargetingAction {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/standard/MoveTo.java,v 1.2 2005/05/16 16:55:47 dissent Exp $";

    protected Locatable target;

    /**
     * 
     */
    public MoveTo(int x, int y, long speed) {
    	this(new Vector3(x, y), speed);
    }
    
    /**
	 * @param speed The speed of the approach represented as the number of
	 * pixels to move in each frame.  Use a very large number for instant
	 * travel.
     */
    public MoveTo( Locatable target, long speed ) {
        this.target = target;
        properties().init("Speed", new NumberProperty(speed));
    }

    /**
     * Add a vector to the position to bring it closer to the target.
     *
     * <p>Result is complete if it has reached its target. </p>
     */
    public ActionResult behave(TextObject to) {
        double speed = ((NumberProperty)properties().get("Speed")).get();

        // get the vector from the position to the target
        Vector3 pos = to.getPositionAbsolute();
        Vector3 newDir = target.getLocation();
	 	newDir.sub(pos);

        ActionResult result = new ActionResult(true, true, false);

	 	// Scale the vector down to the speed if needed.
        if (newDir.length() > speed) {
            try {
                newDir.normalize();
            } catch (Vector3ArithmeticException v3ae) {
                // some silly person set a negative speed, and the object had
                // already arrived at it's location, just ignore the problem.
            }
            newDir.scalar(speed);
            result.complete = false;
        }
        Vector3Property posProp = getPosition(to);
        posProp.add(newDir);
        return result;
    }

    /**
     * Sets a target to approach.
     */
    public void setTarget(Locatable target) {
       	this.target = target;
    }
}
