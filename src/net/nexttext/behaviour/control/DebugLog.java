//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.HashMap;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * An action which logs the TextObjects it processes.
 *
 * <p>This action is useful for debugging, since it can be used to track the
 * order that various actions process objects.  It can be created with an
 * action, which it will call, allowing it to be inserted anywhere in the
 * action tree.  Messages are sent to the Book's log.</p>
 */
public class DebugLog extends AbstractAction {

    static final String REVISION = "$CVSHeader$";

    Action action = null;
    String prefix;

    public DebugLog(String prefix) {
        this.prefix = prefix;
    }

    public DebugLog(String prefix, Action action) {
        this.prefix = prefix;
        this.action = action;
    }

    // Creates a string represenation of the TextObject.
    StringBuffer asString(TextObject to) {
        StringBuffer msg = new StringBuffer();
        if (to instanceof TextObjectGroup) {
            msg.append("[ ");
            TextObject to2 = ((TextObjectGroup)to).getLeftMostChild();
            while (to2 != null) {
                msg.append(to2);
                to2 = to2.getRightSibling();
            }
            msg.append(" ]");
        } if (to instanceof TextObjectGlyph) {
            msg.append(((TextObjectGlyph)to).getGlyph());
        }
        return msg;
    }

    public ActionResult behave(TextObject to) {
        StringBuffer msg = asString(to);
        msg.insert(0,prefix);
        ActionResult res = new ActionResult(true, true, false);
        if (action != null) {
            res = action.behave(to);
            msg.append(" returning (");
            msg.append(res.complete ? "t" : "f");
            msg.append(res.canComplete ? "t" : "f");
            msg.append(res.event ? "t" : "f");
            msg.append(")");
        }
        to.getBook().log(msg.toString());
        return res;
    }

    public Map getRequiredProperties() {
        if (action != null)
            return action.getRequiredProperties();
        else
            return new HashMap(0);
    }

    public void complete(TextObject to) {
        super.complete(to);
        if (action != null) {
            action.complete(to);
        }
    }
}
