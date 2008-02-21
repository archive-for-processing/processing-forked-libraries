//
//Copyright (C) 2004, 2005 Jason Lewis
//
 
package net.nexttext.behaviour.physics;

import processing.core.PApplet;
import net.nexttext.Book;
import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.control.OnCollision;
import net.nexttext.behaviour.control.OnDrag;
import net.nexttext.behaviour.standard.DoNothing;
import net.nexttext.behaviour.standard.Follow;
import net.nexttext.input.Mouse;

/**
 * The factory of Physics behaviours.
 */
public class PhysicsFactory {

    static final String REVISION = "$Header: /Volumes/Storage/Data/Groups/obx/CVS/NextText/src/net/nexttext/behaviour/physics/PhysicsFactory.java,v 1.4.8.1 2007/09/22 16:06:21 elie Exp $";
    
    /**
     * Collide is a behaviour which performs collision response when TextObjects
     * collide with each other
     */
    public static final AbstractBehaviour collide() {        
	    Behaviour collide;
	    collide = new Behaviour( new OnCollision( new Collide( 1, 0 )));               
        collide.setDisplayName("Collide");        
        return collide;
    }
    
    /**
     * Keeps objects inside the visible window, bouncing them off when they reach an edge.
     */
    public static AbstractBehaviour stayInWindow(PApplet p) {        
	    Behaviour b;
	    b = new Behaviour(new StayInWindow(p, true));               
        b.setDisplayName("Stay In Window");        
        return b;
    }
    
    /**
     * Explode gives a one time velocity push to objects in a random direction.
     */
    public static final AbstractBehaviour explode() {
        Behaviour b;
        b = new Behaviour( new Explode() );
        b.setDisplayName("Explode");
        return b;
    }
    
    /**
     * Basic move.  Applies Velocity and Acceleration, changing the Position.
     */
    public static final AbstractBehaviour move() {
        Behaviour b;
        b = new Behaviour( new Move() );
        b.setDisplayName("Move");
        return b;
    }
    
    public static final AbstractBehaviour throwable() {
        Approach approach = new Approach(Book.mouse, 1, 1);
        OnDrag   onDrag   = new OnDrag(approach, new DoNothing());
        approach.setTarget(onDrag);
        Behaviour b = new Behaviour(onDrag);
        b.setDisplayName("Throwable");
        return b;
    }
    
    public static AbstractBehaviour follow() {        
        Behaviour b = new Behaviour( new Follow( new Approach( null, 1, 1 ) ) );
        b.setDisplayName("Follow");
        return b;
    }
    
    /**
     * Returns a descriptive name for this factory.
     */
    public String toString() { return "Physics"; }
}
