//
//Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.dform;

import java.util.Iterator;

import net.nexttext.Locatable;
import net.nexttext.TextObjectGlyph;
import net.nexttext.Vector3;
import net.nexttext.behaviour.TargetingAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.Vector3PropertyList;

/** 
 * ChaosPull is similar to {@link Pull} except that the control points get into a chaotic state when
 * they reach the target.
 *
 * TODO: add parameters.
 */
public class ChaosPull extends DForm implements TargetingAction{

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/dform/ChaosPull.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";

    Locatable target;
    int chaosStrength;
    
    public void setTarget( Locatable target ) {
        this.target = target;
    }
    
    public ChaosPull( Locatable target ) {
        this(target, 1200);
    }
    
    public ChaosPull( Locatable target, int chaosStrength ) {
        this.target = target;        
        this.chaosStrength = chaosStrength;
    }
    
    /* (non-Javadoc)
     * @see net.nexttext.behaviour.dform.DForm#behave(net.nexttext.TextObjectGlyph)
     */
    public ActionResult behave(TextObjectGlyph to) {    
        
        // Get the position of the target relative to the TextObject.
        Vector3 toAbsPos = to.getPositionAbsolute();
        Vector3 targetV = target.getLocation();
        targetV.sub(toAbsPos);

        // Traverse the control points of the glyph, determine the distance
        // from it to the target and move it part way there.
        Vector3PropertyList cPs = getControlPoints(to);
        Iterator i = cPs.iterator();
        while (i.hasNext()) {
            
            Vector3Property cP = (Vector3Property) i.next();
            Vector3 p = cP.get();
            
            Vector3 offset = new Vector3(targetV);
            offset.sub(p);
            
            double pullForce = chaosStrength/(offset.length()+25);
            offset.scalar( pullForce / offset.length() );
            
            p.add(offset);
            cP.set(p);  
        }
        
        return new ActionResult(false, false, false);
    }

    public int getChaosStrength() {
        return chaosStrength;
    }

    /**
     * Sets the 'strength' of the chaosPull, stronger chaosPull results in 
     * larger deformations and faster pulling.
     * 
     * <p>The default value is 1200.</p>
     */
    public void setChaosStrength(int chaosStrength) {
        this.chaosStrength = chaosStrength;
    }
}

