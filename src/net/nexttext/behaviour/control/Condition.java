//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.util.HashMap;
import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * A Condition splits the data flow in two according to the result of the
 * abstract condition() method.
 * 
 * <p>If the condition was evaluated to true, then it will apply the trueAction 
 * otherwise it will apply the falseAction.  </p>
 * 
 * TODO: Handle object pairs, object arrays.
 * 
 * $Id$
 */
public abstract class Condition extends AbstractAction  {
    
    protected Action trueAction;
    protected Action falseAction;
   
    public Condition( Action trueAction, Action falseAction ) {        
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }
    
    /** 
     * @return the outcome of the condition.
     */
    public abstract boolean condition( TextObject to );
        
    /**
     * Applies the trueAction if the result of condition() is true and 
     * applies the falseAction otherwise.
     * 
     * @return the result of the Action that was applied.
     */
    public ActionResult behave(TextObject to) {
        
        if ( condition(to) ) {
            return trueAction.behave(to);
        }
        else {
            return falseAction.behave(to);
        }
    }

    /**
     * The Action has ended, pass it on to both sub-actions.
     */
    public void complete(TextObject to) {
        super.complete(to);
        trueAction.complete(to);
        falseAction.complete(to);
    }

    public Map getRequiredProperties() {
        Map props = new HashMap();
        props.putAll( trueAction.getRequiredProperties() );
        props.putAll( falseAction.getRequiredProperties() );
        return props;
    }
}
