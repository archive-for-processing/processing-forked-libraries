//
// Copyright 2004 Jason Lewis
//

package net.nexttext.input;

import net.nexttext.*;

import java.awt.event.MouseEvent;

/**
 * An input source for mouse information.
 */

public abstract class Mouse extends InputSource implements Locatable {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/input/Mouse.java,v 1.8.6.1 2005/04/15 16:15:34 dissent Exp $";

    static public int BUTTON1 = MouseEvent.BUTTON1;
    static public int BUTTON2 = MouseEvent.BUTTON2;
    static public int BUTTON3 = MouseEvent.BUTTON3;

    /**
     * Get if the specified button is pressed or not
     *
     * @param button is a static button definition.
     * @return  if the specified button is pressed.
     */
    public abstract boolean isPressed(int button);
    
    /**
     * Get the current x position of the mouse
     */
    public abstract int getX();
    
    /**
     * Get the current y position of the mouse
     */
    public abstract int getY();

    public Vector3 getPosition() { return new Vector3(getX(),getY()); }

    /**
     * Locatable interface.
     */
    public Vector3 getLocation() { return getPosition(); }
}
