package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.processing.*;

/**
 * A Condition which is true when a mouse button is pressed over a TextObject 
 * i.e. a single true result is returned if the button was up and got pressed 
 * down on top of the given TextObject.
 *
 * $Id$
 */
public class OnMousePressedOver extends OnMouseDepressed {
    
    private boolean isPressed;
    private boolean wasPressed;
    
    /**
     * Creates an OnMousePressedOver which performs the given Action when the mouse 
     * button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     */
    public OnMousePressedOver(Action trueAction) {
        this(ProcessingMouse.BUTTON1, trueAction);
    }
    
    /**
     * Creates an OnMousePressedOver which performs the given Action when the selected
     * mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     */
    public OnMousePressedOver(int buttonToCheck, Action trueAction) {
        super(buttonToCheck, trueAction);
        
        isPressed = false;
        wasPressed = false;
    }
    
    /**
     * Checks whether or not the selected mouse button is pressed over the TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        wasPressed = isPressed;
        isPressed = super.condition(to);
        if (!wasPressed && isPressed && to.getBoundingPolygon().contains(mouse.getX(), mouse.getY())) {
        	return true;
        }
        return false;
    }
}
