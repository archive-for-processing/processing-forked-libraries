//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * The Multiplexer applies a series of Actions in parallel.
 */
public class Multiplexer extends AbstractAction {

    protected List actions;
    protected HashMap doneWithObject;  // HashMap<Action, HashSet<TextObject>>
    
    /**
     * @param actions a List containing Action objects.
     */
    public Multiplexer( List actions ) {
        this.actions = actions;
        doneWithObject = new HashMap();

        Action action;
        for (ListIterator i = actions.listIterator(); i.hasNext(); ) {
        	action = (Action)i.next();
        	doneWithObject.put(action, new HashSet());
        }
    }
    
    /**
     * Create a new Multiplexer with no actions.
     */
    public Multiplexer() {
        actions = new ArrayList();
        doneWithObject = new HashMap();
    }

    /**
     * Add an action to the Multiplexer.
     */
    public void add(Action action) {
        actions.add(action);
        doneWithObject.put(action, new HashSet());
    }

    /**
     * Apply all the actions to the TextObject.
     *
     * <p>The results of the called actions are combined using the method
     * described in ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {

        ActionResult res = new ActionResult();
                
        for (Iterator i = actions.iterator(); i.hasNext(); ) {
            Action current = (Action)i.next();
            ActionResult tres = null;
            //If textObjects do not have entries in this hashSet
            //then this behaviour has not finished with them            
            if(((HashSet)doneWithObject.get(current)).contains(to))
                tres = new ActionResult(true,true,false);                     
            else 
                tres = current.behave(to);
                            
            if(tres.complete){
            	((HashSet)doneWithObject.get(current)).add(to);                
            }
            res.combine(tres);
            
        }
        res.endCombine();
        // The multiplexer can return complete even if all its actions did not,
        // so those ones need to be informed that it is complete.
        if(res.complete){
            for (Iterator i = actions.iterator(); i.hasNext(); ) {
                Action current = (Action)i.next();
                ((HashSet)doneWithObject.get(current)).remove(to);
            }
            complete(to);
        }
        return res;
    }

    /**
     * End the multiplexer for this object.
     */
    public void complete(TextObject to) {
        super.complete(to);
        for (Iterator i = actions.iterator(); i.hasNext(); ) {
            ((Action)i.next()).complete(to);
        }
    }

    /**
     * The required properties are the union of all properties in the action
     * chain.
     */
    public Map getRequiredProperties() {
        HashMap rP = new HashMap();
        for ( Iterator i = actions.iterator(); i.hasNext(); ) {
            rP.putAll( ((Action)i.next()).getRequiredProperties());
        }
        return rP;
    }
}
