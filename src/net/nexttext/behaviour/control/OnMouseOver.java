package net.nexttext.behaviour.control;

import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;

import net.nexttext.input.Mouse;

/**
 * A Condition which is true when the mouse is on top of the TextObject and 
 * false when it is not. 
 */
public class OnMouseOver extends Condition {
    
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/OnMouseOver.java,v 1.2.8.1 2007/09/22 16:06:20 elie Exp $";
	
    private Mouse mouse;
    
    /**
     * Creates an OnMouseOver which performs the given Action when the mouse
     * is over the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is over the TextObject
     */
    public OnMouseOver(Action trueAction) {
        this(trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnMouseOver which performs one of the given Actions, depending
     * on whether or not the mouse is over the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is over the TextObject
     * @param falseAction the Action to perform when the mouse is off the TextObject
     */
    public OnMouseOver(Action trueAction, Action falseAction) {
    	super(trueAction, falseAction);
        this.mouse = Book.mouse;
    }

    /**
     * Checks whether or not the mouse is over the given TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        return to.getBoundingPolygon().contains(mouse.getX(), mouse.getY());
    }
}
