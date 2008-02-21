//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextObjectRoot;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * This control applies an Action when an object overlaps with another object.
 * 
 * <p>Objects affected by a OnCollision action must be added to the SpatialList,
 * otherwise the behave() method will catch an exception. </p>
 */
public class OnCollision extends AbstractAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/control/OnCollision.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";
    
    protected Action action;
     
    /**
     * Creates an OnCollision which applies the specified Action to every
     * object that are being collided with.
     */
    public OnCollision( Action action ) {
        this.action = action;
    }
     
    /**
     * For each object this one is colliding with, apply the action to the pair
     * of colliding objects.
     *
     * <p>The results of the called actions are combined using the method
     * outlined in ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {
        
        // get the glyph collision set for that object.
        Set col = to.getBook().getSpatialList().getPotentialCollisions(to);

        if ( col.size() == 0 )
            return new ActionResult(false, false, false);

        // find out what is the depth of descendants of to.
        int height = to.getHeight();

        // It is important to use a set here to prevent duplicates
        HashSet colliders = new HashSet();
                
        // build the set of object whose height matches to's
        for ( Iterator i = col.iterator(); i.hasNext(); ) {
                    
            TextObject collider = (TextObject)i.next();
                    
            // from the glyphs, go up to parents a number of times
            // specified by height, or stop if the parent is the 
            // TextRoot.
            TextObject tmp;
            for ( int j=0; j < height; j++ ) {
                tmp = collider.getParent();
                if ( (tmp instanceof TextObjectRoot)) {
                    break;
                } 
                collider = tmp;
            }                    
            colliders.add( collider );
        }

        ActionResult res = new ActionResult();

        for ( Iterator i = colliders.iterator(); i.hasNext(); ) {
            TextObject collider = (TextObject)i.next();
            ActionResult tres = action.behave(new TextObject[] { collider, to });
            res.combine(tres);
        }

        return res.endCombine();
    }

    public Map getRequiredProperties() {
        return action.getRequiredProperties();
    }
}
