package net.nexttext.behaviour.control;

import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import net.nexttext.input.Mouse;
import processing.core.PApplet;
import java.awt.Rectangle;

/**
 * A Condition which is true when the mouse is on top of the PApplet and 
 * false when it is not. 
 */
public class OnMouseOverApplet extends Condition {
    
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/control/Attic/OnMouseOverApplet.java,v 1.1.2.1 2007/09/22 16:06:20 elie Exp $";
	
    private Mouse mouse;
    private Rectangle bounds;
    
    /**
     * Creates an OnMouseOverApplet which performs the given Action when the mouse
     * is over the PApplet.
     *
     * @param p the parent PApplet
     * @param trueAction the Action to perform when the mouse is over the PApplet
     */
    public OnMouseOverApplet(PApplet p, Action trueAction) {
        this(p, trueAction, new DoNothing());
    }
    
    /**
     * Creates an OnMouseOverApplet which performs one of the given Actions, depending
     * on whether or not the mouse is over the PApplet.
     *
     * @param p the parent PApplet
     * @param trueAction the Action to perform when the mouse is over the PApplet
     * @param falseAction the Action to perform when the mouse is off the PApplet
     */
    public OnMouseOverApplet(PApplet p, Action trueAction, Action falseAction) {
    	super(trueAction, falseAction);
    	this.bounds = p.getBounds();
        this.mouse = Book.mouse;
    }

    /**
     * Checks whether or not the mouse is over the given PApplet.
     * 
     * @param to the TextObject to consider (not used)
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        return bounds.contains(mouse.getX(), mouse.getY());
    }
}
