//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.HashSet;
import java.util.Map;
import java.util.LinkedList;
import java.util.WeakHashMap;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * Perform the given action on the TextObject's descendants.
 *
 * <p>The given action is not performed on the TextObject passed to the behave
 * method, but rather on its descendants.  The number of levels to descend can
 * be specified upon construction.  If the TextObject has no descendants at the
 * appropriate level, then nothing is done.  </p>
 */

public class Descend extends AbstractAction {

    static final String REVISION = "$CVSHeader$";

    protected Action descendantAction;
    protected int depth;

    // Behaviours perform a property initialization on each TextObject added to
    // them.  However, since descendants are not initialized by the Behaviour,
    // this Action has to initialize them itself.  Required properties are only
    // determined once, and stored in descendantReqProps.  Each TextObject only
    // needs to be initialized once, so those that have been initialized are
    // remembered in initedDescendants.
    protected Map descendantReqProps;    
    /* 
     * Using a WeakHashMap here makes this action less prone to memory
     * leaks should the structure of a TextObject being processed by this
     * action changes (e.g. if a descendant is removed after the textObjects has
     * already passed through this action). However it does not offer any protection
     * to the descendantAction. Which may still cause a memory leak if it stores metadata
     * on a textObject that suddenly disappears from the descendants list of the TextObject
     * being processed by descend.
     * 
     */
    protected WeakHashMap initedDescendants = new WeakHashMap();

    public Descend(Action descendantAction) {
        this(descendantAction, 1);
    }

    /**
     * Construct a new Descend action with the given action and depth.
     *
     * @param depth is a non-negative integer indicating the number of levels
     * to descend in the TextObject hierarchy to get the TextObjects to be
     * acted on.
     */
    public Descend(Action descendantAction, int depth) {
        this.descendantAction = descendantAction;
        this.depth = depth;
        this.descendantReqProps = descendantAction.getRequiredProperties();
    }

    /**
     * Apply the given action to the TextObject's descendants.
     *
     * <p>The results of the action calls are combined using the method
     * described in Action.ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {

        ActionResult res = new ActionResult();
        LinkedList descendants = getDescendants(to);
        while (!descendants.isEmpty()) {
            TextObject desc = (TextObject)descendants.removeFirst();
            initRequiredProperties(desc);
            res.combine(descendantAction.behave(desc));
        }
        res.endCombine();
        // Descend can return complete even if descendantAction didn't return
        // complete on all the children, so if necessary inform all children
        // that it's complete.
        if (res.complete) complete(to);
        return res;
    }

    /**
     * End this action for this object.
     */
    public void complete(TextObject to) {
        super.complete(to);
        LinkedList descendants = getDescendants(to);
        while (!descendants.isEmpty()) {
            TextObject descendant = (TextObject)descendants.removeFirst();
            descendantAction.complete(descendant);
            initedDescendants.remove(descendant);
        }        
    }

    /**
     * Get the descendants to be acted upon.
     */
    private LinkedList getDescendants(TextObject to) {
        // Loop over depth, creating a new List of objects at each step,
        // populating it with the children of the objects from the previous
        // iteration.
        LinkedList descendants = new LinkedList();
        descendants.add(to);
        for (int i = 0; i < depth; i++) {
            // objects from the last iteration become the parents of the next
            // iteration.
            LinkedList parents = descendants;
            descendants = new LinkedList();
            while (!parents.isEmpty()) {
                TextObjectGroup tog = (TextObjectGroup) parents.removeFirst();
                TextObject child = tog.getLeftMostChild();
                while (child != null) {
                    descendants.add(child);
                    child = child.getRightSibling();
                }
            }
        }
        return descendants;
    }

    /**
     * Initialize the required properties of descendantAction on a child.
     *
     * <p>Since the behaviour does not and cannot initialize the children of
     * the TextObject automatically, it needs to be done each time behave() is
     * called.  </p>
     */
    private void initRequiredProperties(TextObject to) {
        if (initedDescendants.get(to) == null) {
            to.initProperties(descendantReqProps);
            initedDescendants.put(to, new Boolean(true));
        }
    }
}
