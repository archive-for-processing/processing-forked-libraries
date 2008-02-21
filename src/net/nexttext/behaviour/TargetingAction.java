//
// Copyright (C) 2004,2005 Jason Lewis
//

package net.nexttext.behaviour;

import net.nexttext.Locatable;

// $CVSHeader: obx/NextText/src/net/nexttext/behaviour/TargetingAction.java,v 1.1.2.2 2005/04/13 15:00:18 dissent Exp $

/**
 * A TargetingAction is an Action that uses a target.
 *
 * <p>A common type of TargetingAction is one which moves its object to the
 * target.  The purpose of this class is to make it easy to switch around
 * targeted movement in behaviours, by using different TargetingActions.  </p>
 */
public interface TargetingAction extends Action {

    /**
     * Set a new target for this action.
     */
    public void setTarget( Locatable target );    
}
