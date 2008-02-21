package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import processing.core.PApplet;

/**
 * A Condition which is true when the mouse moves over the PApplet i.e. a 
 * single true result is returned if the mouse was off of the PApplet and
 * moved over it.
 */
public class OnMouseInApplet extends OnMouseOverApplet {
    
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMouseInApplet.java,v 1.1.2.1 2007/09/22 16:06:20 elie Exp $";
	
    private boolean isOver;
    private boolean wasOver;
    
    /**
     * Creates an OnMouseInApplet which performs the given Action when the mouse
     * moves over the PApplet.
     *
     * @param p the parent PApplet
     * @param trueAction the Action to perform when the mouse is over the PApplet
     */
    public OnMouseInApplet(PApplet p, Action trueAction) {
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
        if (!wasOver && isOver) {
            return true;
        }
        return false;
    }
}
