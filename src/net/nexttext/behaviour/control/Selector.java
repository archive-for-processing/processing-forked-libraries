//
// Copyright (C) 2006 Jason Lewis
//


package net.nexttext.behaviour.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;

/**
 * This action maintains a collection of action (in form of a HashMap)
 * in which any one of the contained actions can be set to act as the
 * current action.
 * 
 * <p>TextObjects are passed on to (and only to) the current action. 
 * This allows for the creation of behaviours with state, i.e. behaviours 
 * that display different dynamics at different times.
 * 
 * For each action contained in the selector a collection of textObjects that 
 * the action has finished processing is maintained. This maintains the 'completed'
 * semantics of contained actions but allows textObjects to continually be acted upon
 * when the current action changes.
 * </p>
 * 
 * $Id$
 */
public class Selector extends AbstractAction {
    
    HashMap actions;  // HashMap<String, Action>
    Action current;          
    HashMap actionDoneProcessing;  // HashMap<Action, HashMap<TextObject, Boolean>>               
    
    public Selector (){
        actions = new HashMap();
        actionDoneProcessing = new HashMap(); 
    }
    
    /**
     * If [name] matches the name of a previously added
     * action then that action will be made the current action
     * 
     * @param name - the name of the action to be selected.
     */
    public synchronized void select(String name){
        current = (Action)actions.get(name);
        if(current == null)
            throw new NullPointerException("This selector does not contain the action: " + name);
              
    }
    
    /**
     * Add an action to the selector
     * @param name
     * @param action
     */
    public void add(String name, Action action){
        actions.put(name, action);
        actionDoneProcessing.put(action, new HashMap());
    }
    
    /**
     * Applies the current action
     * 
     * @return ActionResult(false, false, true) if the current action
     * completes or signals an event. Else returns ActionResult(false, false, false)
     * 
     */
    public ActionResult behave(TextObject to) {
       //Check if the current action previously indicated that it was finished 
       //with this textObject
       HashMap doneWith = (HashMap)actionDoneProcessing.get(current);
       Boolean completed = (Boolean)doneWith.get(to);
       if(completed != null && completed.booleanValue() == true){
           return new ActionResult(false, false, false);
       }
       else{
           ActionResult currResult = current.behave(to);
           /*
            * If the current action returns complete, we do not
            * want the selector to return complete, because the
            * current action may change.
            * 
            * Instead we signal that an event has occured.
            */ 
           if(currResult.complete){
               current.complete(to);
               doneWith.put(to, new Boolean(true));
               return new ActionResult(false, false, true);
           }           
           else
              return new ActionResult(false, false, false);
       }
    }
    
    public Object[] getActionNames(){
        return actions.keySet().toArray();
    }

    public Map getRequiredProperties() {
        Map props = new HashMap();
        Collection actionList = actions.values();
        
        for (Iterator i = actionList.iterator(); i.hasNext(); ) {
            Action a = (Action)i.next();
            props.putAll(a.getRequiredProperties());
        }        
        return props;
    }
}
