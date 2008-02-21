//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour;

import java.util.Iterator;
import java.util.Map;

import net.nexttext.TextObject;

/**
 * Basic Behaviour class.  
 * 
 * <p>Behaviours are used by the Book, to apply Actions to a set of
 * TextObjects.  </p>
 * 
 * <p>When the Action indicates that the processing of that object is complete
 * the object is removed from the Behaviour's set of TextObjects.  </p>
 */
public class Behaviour extends AbstractBehaviour {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/Behaviour.java,v 1.16 2005/11/04 17:57:11 dissent Exp $";
    
    protected Action action;
    
    /**
     * Creates a Behaviour which will perform the specified action.
     */
    public Behaviour( Action action ) {
        this.action = action;
    }

    /**
     * Calls behave() on every object in its list.
     * 
     * <p>Objects will be removed from the list if the Action completes.  </p>
     */
    public synchronized void behaveAll() {
        for (Iterator i = objects.iterator(); i.hasNext(); ) {
            TextObject to = (TextObject) i.next();
            Action.ActionResult res = action.behave(to);
            if (res.complete) {
                i.remove();
                action.complete(to); //In case the action forgot to call complete itself
            }
        }
    }
    
    public synchronized void addObject( TextObject to ) {
        super.addObject(to);
        Map properties = action.getRequiredProperties();
        to.initProperties( properties );        
    }
        
     /**
      * Stop this behaviour from acting on a TextObject.
      */    
     public synchronized void removeObject(TextObject to) {
         action.complete(to);
         super.removeObject(to);
     }  
}
