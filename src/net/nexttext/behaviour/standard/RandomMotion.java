//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.PropertySet;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.NumberProperty;
import net.nexttext.behaviour.AbstractAction;

/**
 * Moves a TextObject randomly.
 */
public class RandomMotion extends AbstractAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/standard/RandomMotion.java,v 1.2 2005/05/16 16:55:47 dissent Exp $";

    /** 
     * Default constructor. Speed is 4 by default.
     */
    public RandomMotion() {
        init(4);
    }
    
    public RandomMotion(int speed) {
        init(speed);
    }

    private void init( int speed ) {
        properties().init("Speed", new NumberProperty(speed));
    }
    
    /**
     * Moves a TextObject randomly.
     */
    public ActionResult behave(TextObject to) {
        Vector3Property pos = getPosition(to);
        double rate = ((NumberProperty) properties().get("Speed")).get();
        pos.add(new Vector3(rate * (Math.random()-0.5), rate * (Math.random()-0.5)));
        return new ActionResult(false, false, false);
    }
}
