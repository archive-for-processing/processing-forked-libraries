//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext.behaviour.dform;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGlyphIterator;
import net.nexttext.TextObjectGroup;
import net.nexttext.property.Vector3PropertyList;
import net.nexttext.behaviour.AbstractAction;

/**
 * A super class for DForms.
 *
 * <p>These are actions which modify the appearance of TextObjectGlyphs. </p>
 *
 * <p>This class provides an implementation of behave() which recursively calls
 * it on all Glyphs.  </p>
 */
public abstract class DForm extends AbstractAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/dform/DForm.java,v 1.3 2005/10/18 20:51:07 dissent Exp $";

    /**
     * The control points used to deform a glyph.
     */
    public Vector3PropertyList getControlPoints( TextObjectGlyph tog ) {
        return tog.getControlPoints();
    }

    /**
     * DForms generally just make sense on TextObjectGlyphs.
     */
    public abstract ActionResult behave(TextObjectGlyph to);

    /**
     * Default implementation which recursively calls behave on all children.
     *
     * <p>The results of the called actions are combined using the method
     * outlined in ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {
        if (to instanceof TextObjectGlyph) {
            return behave((TextObjectGlyph) to);
        } else {
            ActionResult result = new ActionResult();
            TextObjectGlyphIterator i = ((TextObjectGroup) to).glyphIterator();
            while (i.hasNext()) {
                ActionResult tres = behave(i.next());
                result.combine(tres);
            }
            return result.endCombine();
        }
    }
}
