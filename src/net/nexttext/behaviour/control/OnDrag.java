package net.nexttext.behaviour.control;

import net.nexttext.Book;
import net.nexttext.Locatable;
import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import net.nexttext.behaviour.control.Condition;
import net.nexttext.processing.*;

/**
 * A Condition which is true when the TextObject is being dragged by the mouse.
 *
 * <p>It implements the Locatable interface which returns the location of the
 * mouse offset by the vector between the mouse and the TextObject when the
 * drag started.  If the TextObject is moved to this position it is like
 * dragging it with the mouse.  The position is that of the last TextObject
 * which was dragged, so it's only appropriate to use this Locatable in the
 * true condition of OnDrag.  </p>
 */
/* Id */
public class OnDrag extends Condition implements Locatable {
  
    private ProcessingMouse mouse;
    private int buttonToCheck;
    private boolean dragging;
    private Vector3 dragOffset;
    private TextObject lastDragged;
    private TextObject currDragged;

    /**
     * Creates an OnDrag which performs the given Action when the mouse 
     * button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     */
    public OnDrag(Action trueAction) {
        this(ProcessingMouse.BUTTON1, trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnDrag which performs one of the given Actions, depending
     * on whether or not the mouse button 1 is pressed.
     *
     * @param trueAction the Action to perform when the mouse button 1 is pressed
     * @param falseAction the Action to perform when the mouse button 1 is released
     */
    public OnDrag(Action trueAction, Action falseAction) {
        this(ProcessingMouse.BUTTON1, trueAction, falseAction);
    }
    
    /**
     * Creates an OnDrag which performs the given Action when the selected
     * mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     */
    public OnDrag(int buttonToCheck, Action trueAction) {
        this(buttonToCheck, trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnDrag which performs one of the given Actions, depending
     * on whether or not the selected mouse button is pressed.
     *
     * @param buttonToCheck the mouse button to consider
     * @param trueAction the Action to perform when the selected mouse button is pressed
     * @param falseAction the Action to perform when the selected mouse button is released
     */
    public OnDrag(int buttonToCheck, Action trueAction, Action falseAction) {
    	super(trueAction, falseAction);
        this.mouse = Book.mouse;
        this.buttonToCheck = buttonToCheck;
        dragging = false;
        dragOffset = new Vector3();
    }

    /** 
     * Checks whether or not the selected mouse button is pressed over the given TextObject.
     * 
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
    	if (mouse.isPressed(buttonToCheck)) {
    		if (to.getBoundingPolygon().contains(mouse.getX(), mouse.getY())) {
    			if (!dragging) {
    				// lock the mouse to the TextObject
    				dragging = true;
    				dragOffset = new Vector3(mouse.getX(), mouse.getY());
    				dragOffset.sub(to.getPositionAbsolute());
    				currDragged = to;
    			}
    		}
        } else {
            dragging = false;
            lastDragged = currDragged;
            currDragged = null;
        	dragOffset = new Vector3();
        }
    	
        return (dragging && (currDragged == to));
    }

    /**
     * Gets the target position of the dragged TextObject, if it would follow
     * the mouse.
     * 
     * @return the target position of the TextObject
     */
    public Vector3 getLocation() {
        if (dragging) {
            Vector3 ret = new Vector3(mouse.getX(), mouse.getY());
            ret.sub(dragOffset);
            return ret;
        } else {
            return lastDragged.getPositionAbsolute();
        }
    }
}
