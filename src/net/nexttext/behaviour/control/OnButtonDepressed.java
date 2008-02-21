//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;

import net.nexttext.input.Mouse;

/**
 * A Condition which is true when a mouse button is depressed.
 */
public class OnButtonDepressed extends Condition {
    
    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/behaviour/control/OnButtonDepressed.java,v 1.1.2.1 2005/04/14 16:41:58 dissent Exp $";

    Mouse mouse;
    int button;

    /**
     * @param button is a value from net.nexttext.input.Mouse
     */
    public OnButtonDepressed(Mouse mouse,
                             int button,
                             Action trueAction,
                             Action falseAction) {
        super(trueAction, falseAction);
        this.mouse = mouse;
        this.button = button;
    }

    /** 
     * @return the outcome of the condition.
     */
    public boolean condition( TextObject to ) {
        return mouse.isPressed(button);
    };
}
