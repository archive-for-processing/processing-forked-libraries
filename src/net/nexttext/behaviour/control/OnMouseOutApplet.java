package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import processing.core.PApplet;

/**
 * A Condition which is true when the mouse moves off the PApplet i.e. a 
 * single true result is returned if the mouse was over the PApplet and
 * moved off of it.
 */
public class OnMouseOutApplet extends OnMouseOverApplet {
    
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMouseOutApplet.java,v 1.1.2.1 2007/09/22 16:06:20 elie Exp $";
	
    private boolean isOver;
    private boolean wasOver;
    
    /**
     * Creates an OnMouseOutApplet which performs the given Action when the mouse
     * moves off of the PApplet.
     *
     * @param p the parent PApplet
     * @param trueAction the Action to perform when the mouse is over the PApplet
     */
    public OnMouseOutApplet(PApplet p, Action trueAction) {
        super(p, trueAction, new DoNothing());
        
        isOver = false;
        wasOver = false;
    }

    /**
     * Checks whether or not the mouse is over the given PApplet.
     * 
     * @param to the TextObject to consider (not used)
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
