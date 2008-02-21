//
// Copyright (C) 2004,2005,2006 Jason Lewis
// 

package net.nexttext.behaviour.physics;

import java.awt.Rectangle;
import java.awt.Shape;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

/**
 * StayInside tries to keep an object inside a Shape.  Most likely, you 
 * will want to combine this action with another one which moves the object 
 * inside the Shape first.
 */
public class StayInside extends PhysicsAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/physics/StayInside.java,v 1.3 2005/10/18 20:51:07 dissent Exp $";
    
    protected Shape shape;
    
    /**
     * @param shape an area to remain inside of.
     * @param jiggle is number of pixels to jiggle object to keep it inside. 
     */
    public StayInside(Shape shape, double jiggle) {
        this.shape = shape;
        properties().init("Jiggle", new NumberProperty(jiggle));
    }
    
    /**
     * This constructor sets jiggle to 3 by default.
     * 
     * @param shape an area to remain inside of. 
     */
    public StayInside(Shape shape) {
        this(shape, 3);
    }
     
    /**
     * Jiggles the object until it's fully contained inside the shape. Also
     * slows down the object's velocity if it's trying to move outside.
     * 
     * <p>The returned ActionResult will include an event each time the object
     * is jiggled to keep it inside.  </p>
     */
    public ActionResult behave(TextObject to) {

        Rectangle toBB = to.getBoundingPolygon().getBounds();
        
        if ( shape.intersects( toBB )) {

            Vector3Property posProp = getPosition(to);
            Vector3Property velProp = getVelocity(to);

            // Record which sides intersect
            boolean xLeft   = shape.intersects(toBB.getMinX(), toBB.getMinY(),
                                               1, toBB.getHeight());
            boolean xRight  = shape.intersects(toBB.getMaxX(), toBB.getMinY(),
                                               1, toBB.getHeight());
            boolean xTop    = shape.intersects(toBB.getMinX(), toBB.getMinY(),
                                               toBB.getWidth(), 1);
            boolean xBottom = shape.intersects(toBB.getMinX(), toBB.getMaxY(),
                                               toBB.getWidth(), 1);
            
            // If only one side intersects, then jiggle a bit.

            if (xLeft && !xRight) {
                jiggle(true, -1, posProp, velProp);
            } else if (xRight && !xLeft) {
                jiggle(true, 1, posProp, velProp); 
            } else if (xTop && !xBottom) {
                jiggle(false, -1, posProp, velProp);
            } else if (xBottom && !xTop) {
                jiggle(false, 1, posProp, velProp);
            }
            return new ActionResult(false, false, true);
        } else {
            return new ActionResult(false, false, false);
        }
    }

    //  Jiggle a word within the target.
    private void jiggle(boolean xAxis, int dir, Vector3Property pos, Vector3Property vel) {
        double jiggle = ((NumberProperty)properties().get("Jiggle")).get();
        // Move it back within the object
        pos.add( xAxis ? new Vector3(dir * jiggle, 0) : new Vector3(0, dir * jiggle) );
        // Scale back the velocity by max(half,jiggle) if it's moving out
        if (dir * (xAxis? vel.get().x : vel.get().y) < 0) {
            double velMove = -1 * (xAxis? vel.get().x : vel.get().y) / 2;
            if (Math.abs(velMove) > jiggle) { velMove = dir * jiggle; }
            vel.add(new Vector3( xAxis? velMove : 0, xAxis? 0: velMove , 0));
        }
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
