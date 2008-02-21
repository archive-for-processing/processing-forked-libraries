//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * A series of Actions, each of which is executed when the previous one is
 * complete.
 *
 * <p>The current action in the chain is remembered for each TextObject.  When
 * behave is called, that action is called.  If that action returns complete or
 * won't complete and returns an event, then the current action is moved to the
 * next action, to be called next time.  </p>
 * 
 * <p>If the current action is incremented, then ActionResult.event is
 * returned, unless there is no next action, in which case
 * ActionResult.complete is returned.  </p>
 * 
 * $Id$
 */
public class Chain extends AbstractAction {
    
    protected List actions;

    /**
     * Creates a new Chain with the given actions.
     * 
     * @param actions a List containing Action objects.
     */
    public Chain( List actions ) {
        this.actions = actions;
    }

    /**
     * Create a new Chain with no actions.
     */
    public Chain() {
        actions = new ArrayList();
    }

    /**
     * Add an action to the end of the Chain.
     */
    public void add(Action action) {
        actions.add(action);
    }

    /**
     * Process the current action for the given TextObject.
     *
     * <p>If the action is completed, update current action.  </p>
     *
     * @return ActionResult with event set if the current action was
     * incremented, and complete set if it was incremented past the end of the
     * chain.
     */
    public ActionResult behave(TextObject to) {

        // Get the index of the current action.
        Integer currentActionIndexObj = (Integer) textObjectData.get(to);

        // TextObjects that have not been processed start at the beginning of
        // the chain.
        if (currentActionIndexObj == null) {
            currentActionIndexObj = new Integer(0);
            textObjectData.put(to, currentActionIndexObj);
        }

        int currentActionIndex = currentActionIndexObj.intValue();

        // If the index is too high, maybe actions is empty, or the list of
        // actions changed.
        if (currentActionIndex >= actions.size()) {
            textObjectData.remove(to);
            return new ActionResult(true, true, true);
        }

        // Perform the desired action
        Action currentAction = (Action) actions.get(currentActionIndex);
        ActionResult res = currentAction.behave(to);

        // Increment the current action if necessary
        if (res.complete || (res.event && !res.canComplete)) {
            currentAction.complete(to);
            if (currentActionIndex == (actions.size() - 1)) {
                // The chain is complete
                complete(to);
                return new ActionResult(true, true, true);
            } else {
                textObjectData.put(to, new Integer(currentActionIndex + 1));
                return new ActionResult(false, true, true);
            }
        }

        return new ActionResult(false, true, false);
    }

    /**
     * The chain has ended for this TextObject, forget internal state.
     */
    public void complete(TextObject to) {
    	super.complete(to);
        Integer indexObj = (Integer) textObjectData.remove(to);
        if (indexObj != null) {
            Action currentAction = (Action) actions.get(indexObj.intValue());
            currentAction.complete(to);
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
