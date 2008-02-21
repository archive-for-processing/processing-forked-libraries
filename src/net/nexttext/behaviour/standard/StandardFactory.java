//
//Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.Book;
import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.control.OnDrag;
import net.nexttext.behaviour.control.Repeat;

/**
 * The factory of Standard behaviours.
 */
public class StandardFactory {
	
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/standard/StandardFactory.java,v 1.3.4.1 2007/09/22 16:06:19 elie Exp $";
    
    public static final AbstractBehaviour randomMotion() {        
        Behaviour rm = new Behaviour( new RandomMotion() );
        rm.setDisplayName("Random Motion");
        return rm;
    }

    
	public static AbstractBehaviour followMouse() {
        MoveTo moveTo = new MoveTo(Book.mouse, 1);
        Behaviour b = new Behaviour(new Repeat(moveTo, 0));
        b.setDisplayName("Follow Mouse");
        return b;
    }
    
    public static AbstractBehaviour draggable() {
        MoveTo moveTo = new MoveTo(Book.mouse, Long.MAX_VALUE);
        OnDrag onDrag = new OnDrag(new Repeat(moveTo, 0), new DoNothing());
        moveTo.setTarget(onDrag);
        Behaviour b = new Behaviour(onDrag);
        b.setDisplayName("Draggable");
        return b;
    }

    public String toString() { return "Standard"; }
}
