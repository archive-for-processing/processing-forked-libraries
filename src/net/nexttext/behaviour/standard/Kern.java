//
// Copyright (C) 2007 Jason Lewis
//

package net.nexttext.behaviour.standard;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.Vector3;
import net.nexttext.behaviour.AbstractAction;

/**
 * 
 * Resets the spacing between children of a textObjectGroup. It 
 * uses the width of the bounding box of the children of that group
 * plus a constant passed in at construction to space the children.
 * 
 * <p>When applied to a word this operation is known as Kerning, it was
 * originally designed to do just this, hence the name. </p>
 *
 */
public class Kern extends AbstractAction {
    
    public double kern;
    
    public Kern(double kern){
        this.kern = kern;
    }
    
    public ActionResult behave(TextObject to) {
        if (to instanceof TextObjectGlyph) {
            return new ActionResult(true, true, false);
        }        
        TextObjectGroup tog = (TextObjectGroup)to;
        TextObject left = tog.getLeftMostChild();
        TextObject right = left.getRightSibling();
        while(right != null){
            Vector3 leftBottomLeftCorner = new Vector3(left.getBounds().getMinX(),left.getBounds().getMaxY());
            Vector3 rightBottomLeftCorner = new Vector3(right.getBounds().getMinX(),right.getBounds().getMaxY());
            //Find the distance between the two glyphs 
            rightBottomLeftCorner.sub(leftBottomLeftCorner);
            //Move the right sibling onto the same position as its left neighbour
            right.getPosition().sub(rightBottomLeftCorner);
            //Add the width of the left sibling plus the kern constant to the position
            //of the right sibling
            /*
             * We should check whether we can do this in one set() rather than a sub() 
             * followed by an add(). I (Yannick) think i tried it and it didnt work but i 
             * am not sure.
             */
            double width = left.getBounds().getWidth() + kern;
            right.getPosition().add(new Vector3(width,0));
            //Move onto the next pair
            left = right;            
            right = right.getRightSibling();            
        }
        return new ActionResult(true, true, false);
    }
}
