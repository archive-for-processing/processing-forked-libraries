package net.nexttext.behaviour.standard;

import processing.core.PVector;
import net.nexttext.Locatable;
import net.nexttext.PLocatableVector;
import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.behaviour.Action.ActionResult;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;



/**
 * Causes TextObjects to follow their left siblings.
 *
 * <p>The provided TargettingAction is used to have TextObjects follow either their
 * left or right siblings.  </p>
 */
/* $Id: MoveAway.java 112 2011-08-26 22:10:55Z proumf@gmail.com $ */
public class MoveAway extends AbstractAction implements TargetingAction {

	
	
	protected Locatable target;
	protected long speed;
	
	
	/**
     * Move a TextObject to a specified position at a certain speed.
     * @param x  target position
     * @param y  target position
     */
	 public MoveAway(int x, int y) {
	    	this(x, y, 2);
	    }
	    
	    /**
	     * Move a TextObject to a specified position at a certain speed.
	     * @param x  target position
	     * @param y  target position
	     * @param speed moving speed
	     */
	    public MoveAway(int x, int y, long speed) {
	    	this(new PVector(x, y), speed);
	    }
	    
	    /**
	     * Move a TextObject to a target.
	     * @param target locatable target
	     */
	    public MoveAway( Locatable target ) {
	        this(target, 2);
	    }    
	    
	   

	    /**
	     * Move a TextObject to a target.
	     * @param target PVector target
	     */
	    public MoveAway( PVector target ) {
	        this(target, 2);
	    }
	    /**
	     * Move a TextObject to a target at a certain speed.
	     * @param target a PVector
		 * @param speed The speed of the approach represented as the number of
		 * pixels to move in each frame.  Use a very large number for instant
		 * travel.
	     */
	    publ
		public MoveAway(PVector pVector, long speed) {
			this((Locatable)pVector, speed);
		}
		/**
	     * Move a TextObject to a target at a certain speed.
	     * @param target locatable target
		 * @param speed The speed of the approach represented as the number of
		 * pixels to move in each frame.  Use a very large number for instant
		 * travel.
	     */
	    public MoveAway( Locatable target, long speed ) {
	        this.target = target;
	        this.speed = speed;
	    }

	    
	    
	    
	    public ActionResult behave(TextObject to) {
	        

	        // get the vector from the position to the target
	        PVector pos = to.getPositionAbsolute();
	        
	        // check if we are use a Locatable object or a PVector
	        PVector newDir = target.getLocation();
		 	pos.sub(newDir);

	        ActionResult result = new ActionResult(true, true, false);

		 	// Scale the vector down to the speed if needed.
	        if (pos.mag() > speed) {
	            pos.normalize();
	            pos.mult(speed);
	            result.complete = false;
	        }
	        PVectorProperty posProp = getPosition(to);
	        posProp.add(pos);
	        return result;
	    } 
	    
	    
	    
	    
	    
	    /**
	     * Sets a target to approach.
	     */
	    public void setTarget(Locatable target) {
	       	this.target = target;
	    }

	    /**
	     * Sets a target to approach.
	     */
	    public void setTarget( float x, float y ) {
	    	setTarget(x, y, 0);
	    }
	    
	    /**
	     * Sets a target to approach.
	     */
	    public void setTarget( float x, float y, float z ) {
	    	setTarget(new PLocatableVector(x, y, z));
	    }
	    
	    /**
	     * Sets a target to approach.
	     */
	    public void setTarget( PVector target ) {
	    	setTarget(new PLocatableVector(target));
	    }
}
