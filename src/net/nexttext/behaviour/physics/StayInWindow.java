//
// Copyright (C) 2004,2005 Jason Lewis
//

package net.nexttext.behaviour.physics;
 
import java.awt.Rectangle;

import processing.core.PApplet;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.behaviour.Action;
import net.nexttext.property.Vector3Property;

/**
 * Keep objects inside a window.
 *
 * <p>By default, StayInWindow bounces objects off the edge of the visible
 * window.  It can be configured to change the window size and object
 * behaviour.  </p>
 * 
 * $Id$
 */
public class StayInWindow extends PhysicsAction implements Action {
  
	boolean bounce;
    
    /** Window to keep things in. */
    long minX, maxX, minY, maxY;
    
    /**
     * Creates a StayInWindow which constraints TextObjects to always remain 
     * within the bounds of the PApplet and to bounce off the edges.
     *
     * @param p the parent PApplet
     */
    public StayInWindow(PApplet p) {            
        this(new Rectangle(0, 0, p.width, p.height), true);
    }
    
    /**
     * Creates a StayInWindow which constraints TextObjects to always remain 
     * within the bounds of the PApplet.
     *
     * @param p the parent PApplet
     * @param bounce Causes the objects to bounce off the edges if true or to
     * stick to the edges if false.
     */
    public StayInWindow(PApplet p, boolean bounce) {            
        this(new Rectangle(0, 0, p.width, p.height), bounce);
    }
    
    /**
     * Creates a StayInWindow which constraints TextObjects to remain within the
     * bounds of a specific rectangle.
     * 
     * @param bounds An arbitrary rectangle
     * @param bounce Causes the objects to bounce off the edges if true or to
     * stick to the edges if false.
     */
    public StayInWindow(Rectangle bounds, boolean bounce) {
        this.bounce = bounce;
        setWindow(bounds);
    }
    
    /**
     * Redefines the window region to constraint objects to.
     */
    public void setWindow( Rectangle window ) {
        minX = (int)window.getMinX();
        minY = (int)window.getMinY();
        maxX = (int)window.getMaxX();
        maxY = (int)window.getMaxY();         
    }
    
    /**
     * Constraints the object so that it stays within the visible window.  
     * 
     * <p>The returned ActionResult will include an event when the object
     * encountered one of the window's edges.  </p>
     */
    public ActionResult behave(TextObject to) {
    	// see if the object is inside the window using the object's
        // bounding box.
        Rectangle bb = to.getBoundingPolygon().getBounds();
        Vector3 offset = bringBackObject( bb );

        if ( offset.isZero() ) {
            // if there is no offset then the object is inside, so do nothing
            return new ActionResult(false, false, false);
        }
        else {
            // translate the object so its inside
            Vector3Property posProp = getPosition(to);
            Vector3 pos = posProp.get();
            pos.add( offset );
            posProp.set( pos );
            
            Vector3Property velProp = getVelocity(to);
            Vector3 vel = velProp.get();
            
            // either bounce or stop 
            if ( bounce ) {
                vel = bounce(vel, offset);
            }
            else {
                vel = stick(vel, offset);
            }
            velProp.set( vel );
            
            return new ActionResult(false, false, true);
        }
    }
 
    /**
     * Returns the Vector3 by which the object must be translated in order to
     * remain inside the window.
     * 
     * @return the amount to translate to remain inside the window
     */
    private Vector3 bringBackObject( Rectangle bb ) {
        
        Vector3 offset = new Vector3();
        
        // XXXBUG: to be safe, we could modify Vector3 to add a add() method
        // which takes 3 doubles as opposed to a Vector3.
        if ( bb.getMinX() < minX ) offset.x += minX - bb.getMinX();
        if ( bb.getMaxX() > maxX ) offset.x += maxX - bb.getMaxX();
        if ( bb.getMinY() < minY ) offset.y += minY - bb.getMinY();
        if ( bb.getMaxY() > maxY ) offset.y += maxY - bb.getMaxY();
        
        return offset;
    }
    
    /**
     * Given the Vector3 that was used to bring the object back, reflect 
     * the object's velocity so that it "bounces" off each edge it crossed
     * over.
     * 
     * @return the modified velocity
     */
    private Vector3 bounce( Vector3 objectVel, Vector3 bringBack ) {
        
        if ( bringBack.x != 0 ) objectVel.x *= -1; 
        if ( bringBack.y != 0 ) objectVel.y *= -1;
        
        return objectVel;
    }
    
    /**
     * Given the Vector3 that was used to bring the object back, set the
     * Velocity to zero in any direction the object crossed over.
     * 
     * @return the modified velocity
     */
    private Vector3 stick( Vector3 objectVel, Vector3 bringBack ) {
        
        if ( bringBack.x != 0 ) objectVel.x = 0; 
        if ( bringBack.y != 0 ) objectVel.y = 0;
        
        return objectVel;
    }
}
