//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGlyphIterator;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;


/**
 * Perform the given action on the TextObject's glyphs.
 *
 * <p>The given action is not performed on the TextObject passed to the behave
 * method, but rather on its glyphs.</p>
 *
 */

public class ApplyToGlyph extends AbstractAction {

    static final String REVISION = "$CVSHeader$";

    private Action action;
    
    public ApplyToGlyph(Action descendantAction) {
        this.action = descendantAction;
    }

    /**
     * Apply the given action to the TextObject's descendants.
     *
     * <p>The results of the action calls are combined using the method
     * described in Action.ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {
        if (to instanceof TextObjectGlyph) {
            return action.behave((TextObjectGlyph) to);
        } 
        else {
            ActionResult res = new ActionResult();
            TextObjectGlyphIterator i = ((TextObjectGroup) to).glyphIterator();            
            while (i.hasNext()) {                
                ActionResult tres = action.behave(i.next()); 
                res.combine(tres);
            }
            /*
             * see the ActionResult class for details on how
             * ActionResults are combined.
             */
            res.endCombine();
            if (res.complete){
                action.complete(to);
                complete(to);
            }
            return res;
        }
    }

    /**
     * End this action for this object and end the passed in 
     * action for all its descendants.
     */
    public void complete(TextObject to) {
        super.complete(to);
        if (to instanceof TextObjectGlyph) {
            action.complete(to);
        }
        else{
            TextObjectGlyphIterator i = ((TextObjectGroup) to).glyphIterator();
            while (i.hasNext()) {
                TextObject next = i.next();
                action.complete(next);
            }
        }
    }
}