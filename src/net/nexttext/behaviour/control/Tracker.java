//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * Tracks the TextObjects processed by an action.
 *
 * <p>Tracker can be used to determine how many and which TextObjects are
 * currently being processed.  It is constructed with an action to track, and
 * is used in place of the action being tracked.  <p>
 */
public class Tracker extends AbstractAction {

    static final String REVISION = "$CVSHeader$";

    Action action;

    /**
     * Construct a Tracker for the given Action.
     */
    public Tracker(Action action) {
        this.action = action;
    }

    /**
     * Pass the TextObject on to the contained Action, tracking the object.
     */
    public ActionResult behave(TextObject to) {

        textObjectData.put(to, null);

        ActionResult res = action.behave(to);
        if (res.complete) {
            complete(to);
        }
        return res;
    }

    /**
     * Get the count of objects currently being processed by the action.
     */
    public int getCount() {
        return textObjectData.size();
    }

    /**
     * Determine if a specific object is being processed by the action.
     */
    public boolean isProcessing(TextObject to) {
        return textObjectData.containsKey(to);
    }
}
