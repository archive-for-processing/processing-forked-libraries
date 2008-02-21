//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.dform;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.Vector3;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Vector3PropertyList;
import net.nexttext.property.Vector3Property;

import java.awt.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A DForm which throbs the TextObject.
 *
 * <p>Think of throb as a multiplication of the size of the object, which
 * changes over time.  </p>

 * <p>In order to improve interoperability with other DForms, the period of the
 * throb is tracked as a frame count specific to each TextObjectGlyph.  This
 * way, each time the behaviour is called it modifies the control points of the
 * Glyph by multiplying them by the appropriate factor, thus preserving any
 * other modifications.  </p>
 *
 * <p>The following calculation defines throb with a period <code>p</code> and
 * scale of <code>s</code>.  Given a vector <code>c</code> from the center of
 * the glyph to one of its control points, it's throbbed value for frame
 * <code>f</code> is 
 * <pre>p * ( ( s - 1 ) * ( ( cos( f / p * 2PI - PI ) ) + 1 ) + 1 ) </pre>
 * </p>
 *
 * <p>XXXBUG: If the period or scale is changed after the behaviour has been
 * started, then it will mess up any objects that are already throbbing.  The
 * way to correct this problem is to cache the period and scale along with the
 * frame count, and only update them when a throb is completed.  </p>
 */
public class Throb extends DForm {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/dform/Throb.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";

    /**
     * @param scale is amount the object's size will increase, as a multplier.
     * @param period is the period of the throb, in frames.
     */
    public Throb(double scale, int period) {
        properties().init("Scale", new NumberProperty(scale));
        properties().init("Period", new NumberProperty(period));
    }

    public ActionResult behave(TextObjectGlyph to) {
        // Get the cached previous frameCount
        Integer fCObj = (Integer) (textObjectData.get(to));
        if (fCObj == null) { fCObj = new Integer(0); }
        int fC = fCObj.intValue() + 1;
        textObjectData.put(to, new Integer(fC));

        // The amount to multiply each control point by.  The factor to
        // generate the current frame from the origin, divided by the factor to
        // generate the previous frame from the origin.
        double scale = ((NumberProperty) properties().get("Scale")).get();
        long period = ((NumberProperty) properties().get("Period")).getLong();

        double factor = tF(fC, scale, period) / tF(fC - 1, scale, period);

        // Determine the center of to, in the same coordinates as the control
        // points will be.
        Vector3 toAbsPos = to.getPositionAbsolute();
        Rectangle bb = to.getBoundingPolygon().getBounds();
        Vector3 center = new Vector3(bb.getCenterX(), bb.getCenterY());
        center.sub(toAbsPos);

        // Traverse the control points of the glyph, applying the
        // multiplication factor to each one, but offset from the center, not
        // the position.
        Vector3PropertyList cPs = getControlPoints(to);
        Iterator i = cPs.iterator();
        while (i.hasNext()) {
            Vector3Property cP = (Vector3Property) i.next();
            // Get the vector from the center of the glyph to the control point.
            Vector3 p = cP.get();
            p.sub(center);

            // Scale the control point by the appropriate factor
            p.scalar(factor);

            // Return p to the original coordiates
            p.add(center);

            // Install p as the property
            cP.set(p);
        }
        if (fC % period == 0) {
            return new ActionResult(false, false, true);
        } else {
            return new ActionResult(false, false, false);
        }
    }

    /**
     * The amount to multiply a vector by on the specified frame.
     */
    private double tF(int frame, double scale, long period) {
        double phase = Math.PI * 2 * frame / period;
        return ((Math.cos(phase - Math.PI) + 1) * (scale - 1 )) + 1;
    }
}
