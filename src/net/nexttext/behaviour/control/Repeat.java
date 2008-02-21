//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.HashMap;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;
import net.nexttext.property.NumberProperty;

/**
 * Repeats an action for a fixed number of times, then stops that action.  
 * 
 * <p>The repeat count is incremented whenever the called Action can't complete
 * but returns an event, or when it does complete.  Repeat will return event in
 * the ActionResult when the count is incremented, and complete when it reaches
 * its repeat count.  </p>
 *
 * <p>Repeat will have no effect when used with an action that does not return
 * events or complete.  </p>
 * 
 * XXXBUG: how could we identify a "Countable" object explicitly?
 */
public class Repeat extends AbstractAction {

    Action action;

    /**
     * @param repetitions is the number of times to repeat, use 0 to repeat
     * forever.
     */

    public Repeat( Action action, int repetitions ) {
        this.action = action;
        properties().init("Repetitions", new NumberProperty(repetitions));
    }

    /**
     * See class description. 
     */
    public ActionResult behave(TextObject to) {
        
        // get the repetition property
        long rep = ((NumberProperty)properties().get("Repetitions")).getLong();
        
        ActionResult tres = action.behave(to);
        if (rep > 0) {
            // get the counter for that object
            Integer counter = (Integer) textObjectData.get(to);

            // if there was no counter for that object create a new one.
            if ( counter == null ) {
                counter = new Integer(1);
            } else {
                // increment the counter
                counter = new Integer(counter.intValue()+1);    
            }
            
            // check if we reached the max number of repetitions
            if (counter.intValue() >= rep) {
                // remove the counter
                textObjectData.remove(to);
                return new ActionResult(true, true, tres.event);
            } else {
                // put the updated counter back
                textObjectData.put(to, counter);
                return new ActionResult(false, true, tres.event);
            }
        }
        // if the Repeat is set to infinite repetitions 
        return new ActionResult(false, false, tres.event);
    }

    public Map getRequiredProperties() {
        return action.getRequiredProperties();
    }
}
