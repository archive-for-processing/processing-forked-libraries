package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.processing.*;

/**
 * A Condition which is true when a mouse button is pressed i.e. a single true result 
 * is returned if the button was up and got pressed down.
 */
public class OnMousePressed extends OnMouseDepressed {
    
    static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMousePressed.java,v 1.1.2.2 2007/10/25 21:13:24 elie Exp $";
    
    private boolean isPressed;
    private boolean wasPressed;
    
    /**
     * Creates an OnMousePressed which performs the given Action when the mouse 
     * button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     */
    public OnMousePressed(Action trueAction) {
        this(ProcessingMouse.BUTTON1, trueAction);
    }
    
    /**
     * Creates an OnMousePressed which performs the given Action when the selected
     * mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     */
    public OnMousePressed(int buttonToCheck, Action trueAction) {
        super(buttonToCheck, trueAction);
        
        isPressed = false;
        wasPressed = false;
    }
    
    /**
     * Checks whether or not the selected mouse button is pressed.
     * 
     * @param to the TextObject to consider (unused)
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        wasPressed = isPressed;
        isPressed = super.condition(to);
        if (!wasPressed && isPressed) {
            return true;
        }
        return false;
    }
}
