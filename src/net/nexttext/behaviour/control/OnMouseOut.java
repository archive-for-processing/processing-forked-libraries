package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;

/**
 * A Condition which is true when the mouse moves off of the TextObject i.e. a 
 * single true result is returned if the mouse was over the TextObject and
 * moved off of it.
 */
public class OnMouseOut extends OnMouseOver {
    
    static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMouseOut.java,v 1.1.2.2 2007/10/25 21:13:24 elie Exp $";
    
    private boolean isOver;
    private boolean wasOver;
    
    /**
     * Creates an OnMouseOut which performs the given Action when the mouse
     * moves off the TextObject.
     *
     * @param trueAction the Action to perform when the mouse moves off the TextObject
     */
    public OnMouseOut(Action trueAction) {
        super(trueAction, new DoNothing());
        
        isOver = false;
        wasOver = false;
    }

    /**
     * Checks whether or not the mouse moved off the given TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        wasOver = isOver;
        isOver = super.condition(to);
        if (wasOver && !isOver) {
            return true;
        }
        return false;
    }
}
