//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.dform;

import net.nexttext.Book;
import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.control.OnMouseDepressed;
import net.nexttext.behaviour.control.Repeat;
import net.nexttext.processing.ProcessingMouse;

/**
 * The factory of DForm behaviours.
 */
public class DFormFactory {
          
	static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/dform/DFormFactory.java,v 1.2.8.1 2007/09/22 16:06:22 elie Exp $";
    
    public static AbstractBehaviour pull() {
    	Action pull = new Pull(Book.mouse, 10, 2);
        Action reform = new Reform();
        Behaviour b = new Behaviour(new OnMouseDepressed(ProcessingMouse.BUTTON1, pull, reform));
        b.setDisplayName("Pull");
        
        return b;
    }
     
    public static AbstractBehaviour throb() {         
        Behaviour throb = new Behaviour(new Repeat(new Throb(2, 100), 0));
        throb.setDisplayName("Throb");
        return throb;
    }
    
    public String toString() {
        return "DForm";
    }
}
