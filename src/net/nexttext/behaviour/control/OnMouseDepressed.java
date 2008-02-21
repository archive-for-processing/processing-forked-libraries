package net.nexttext.behaviour.control;

import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import net.nexttext.input.Mouse;
import net.nexttext.processing.*;

/**
 * A Condition which is true when a mouse button is down and false when a mouse
 * button is up.
 */
public class OnMouseDepressed extends Condition {
    
    static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMouseDepressed.java,v 1.1.2.1 2007/09/22 16:06:19 elie Exp $";
    
    Mouse mouse;
    int buttonToCheck;

    /**
     * Creates an OnMouseDepressed which performs the given Action when the mouse 
     * button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     */
    public OnMouseDepressed(Action trueAction) {
        this(ProcessingMouse.BUTTON1, trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnMouseDepressed which performs one of the given Actions, depending
     * on whether or not the mouse button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     * @param falseAction the Action to perform when the mouse button 1 is released
     */
    public OnMouseDepressed(Action trueAction, Action falseAction) {
        this(ProcessingMouse.BUTTON1, trueAction, falseAction);
    }
    
    /**
     * Creates an OnMouseDepressed which performs the given Action when the selected
     * mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     */
    public OnMouseDepressed(int buttonToCheck, Action trueAction) {
        this(buttonToCheck, trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnMouseDepressed which performs one of the given Actions, depending
     * on whether or not the selected mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     * @param falseAction the Action to perform when the selected mouse button is released
     */
    public OnMouseDepressed(int buttonToCheck, Action trueAction, Action falseAction) {
    	super(trueAction, falseAction);
        this.mouse = Book.mouse;
        this.buttonToCheck = buttonToCheck;
    }
    
    /**
     * Checks whether or not the mouse is over the given TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        return mouse.isPressed(buttonToCheck);
    }
}
