import net.nexttext.TextObject;
import net.nexttext.TextObjectGroup;
import net.nexttext.Vector3;
import net.nexttext.Locatable;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;

import java.awt.Rectangle;


/**
 * Behaviour that moves a TextObject on random trajectories around its parent's 
 * center, creating a swimming effect.
 */
public class Swim extends IWillFollowAction {

    static final String REVISION = "$CVSHeader$";
    
    static final double THRESHOLD = 0.1;
    
    Rectangle bounds;
    
    // when setting a new target, this is the max distance away from the 
    // current position that it can be
    double segmentX;  
    double segmentY;

    
    /** 
     * Creates a new instance of Swim
     * 
     * @param bounds the bounding rectangle
     */
    public Swim(Rectangle bounds) {
        this.bounds = bounds;
        
        properties().init("speedX", new NumberProperty(0.5f));
        properties().init("speedY", new NumberProperty(0.5f));
      
        segmentX = 25.0f;
        segmentY = 25.0f;
    }
    
    
    /** 
     * Creates a new instance of Swim with custom speed and segment length
     * 
     * @param bounds the bounding rectangle
     * @param speedX horizontal float speed
     * @param speedY vertical float speed
     * @param segmentX max horizontal segment length
     * @param segmentY max vertical segment length
     */
    public Swim(Rectangle bounds, float speedX, float speedY, float segmentX, float segmentY) {
        this.bounds = bounds;
        
        properties().init("speedX", new NumberProperty(speedX));
        properties().init("speedY", new NumberProperty(speedY));
        
        this.segmentX = segmentX;
        this.segmentY = segmentY;
    }
	
    
    /** 
     * Moves a TextObject in small increments on a trajectory.
     *
     * @param to the TextObject to act upon
     */
    public ActionResult behave(TextObject to) {
        double x, y;            
            
        // get the position
        Vector3Property positionProperty = (Vector3Property)to.getProperty("Position");
        Vector3 position = positionProperty.get();
        
        // if not yet set, init the target at the TextObject's current position
        if (to.getProperty("Target") == null)
            to.init("Target", new Vector3Property(position));
        
        
        // get the target position
        Vector3 target = ((Vector3Property)to.getProperty("Target")).get();

        // calculate the difference from the current position to the target position
        double targetDiffX = target.x-position.x;
        double targetDiffY = target.y-position.y;
        
        // if the TextObject has reached its target, set a new one
        if ((Math.abs(targetDiffX) < THRESHOLD) && (Math.abs(targetDiffY) < THRESHOLD)) {
            // get the distance from the parent's (word) center
            double parentDiffX = to.getParent().getCenter().x - to.getCenter().x;
            double parentDiffY = to.getParent().getCenter().y - to.getCenter().y;
    
            // new targets are always in the direction of the parent's center
            if ((-segmentX <= parentDiffX) && (parentDiffX <= segmentX))
                x = Math.random()*2*segmentX-segmentX;
            else if (-segmentX > parentDiffX)
                x = Math.random()*segmentX;
            else
                x = Math.random()*-segmentX;
                        
            if ((-segmentY <= parentDiffY) && (parentDiffY <= segmentY))
                y = Math.random()*2*segmentY-segmentY;
            else if (-segmentY > parentDiffY)
                y = Math.random()*segmentY;
            else
                y = Math.random()*-segmentY;
            
            // make sure the next target is inside the window
            Vector3 nextTarget = new Vector3(x, y);
            Vector3 nextAbsTarget = to.getLocation();
            nextAbsTarget.add(nextTarget);
            if (nextAbsTarget.x <= 0)
                nextTarget.add(new Vector3(segmentX, 0));
            else if (nextAbsTarget.x >= bounds.width)
                nextTarget.add(new Vector3(-segmentX, 0));
            
            if (nextAbsTarget.y <= 0)
                nextTarget.add(new Vector3(0, segmentY));
            else if (nextAbsTarget.y >= bounds.height)
                nextTarget.add(new Vector3(0, -segmentY));
                        
            setTarget(to, nextTarget);
            
        // if the TextObject has not reached its target, move towards it    
        } else {
            // get the speed
            float speedX = (float)((NumberProperty)properties().get("speedX")).get();
            float speedY = (float)((NumberProperty)properties().get("speedY")).get();
        
            // move the TextObject towards the target by a random amount
            if (targetDiffX < 0)
                x = Math.random()*Math.max(targetDiffX, -speedX);
            else
                x = Math.random()*Math.min(targetDiffX, speedX);
            
            if (targetDiffY < 0)
                y = Math.random()*Math.max(targetDiffY, -speedY);
            else
                y = Math.random()*Math.min(targetDiffY, speedY);
            
            // translate to the new position
            positionProperty.add(new Vector3(x, y));
        }
        
        return new ActionResult(false, false, false);
    }
    
   
    /**
     * Sets a new target destination for the TextObject, depending on its 
     * current position and its parent's current position
     *
     * @param to the TextObject to consider
     * @param target the new target
     */
    public void setTarget(TextObject to, Locatable target) {
        Vector3Property targetProperty = (Vector3Property)to.getProperty("Target");
        targetProperty.set((Vector3)target);
    }
}
