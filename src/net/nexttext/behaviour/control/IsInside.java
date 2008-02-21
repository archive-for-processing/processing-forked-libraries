//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour.control;

import java.awt.Rectangle;
import java.awt.Shape;

import net.nexttext.behaviour.Action;
import net.nexttext.TextObject;

/**
 * A Condition which is true when the TextObject is wholly or partly inside the
 * provided shape.
 */
public class IsInside extends Condition {
    
    protected Shape area;
    
    public IsInside(Shape area, Action trueAction, Action falseAction) {
        super(trueAction, falseAction);
        this.area = area;
    }
    
    /**
     * @return If the object is inside or overlaps with the Shape.
     */
    public boolean condition( TextObject to ) {
        
        Rectangle objectBB = to.getBoundingPolygon().getBounds();

        return area.intersects(objectBB);
    }     
}
