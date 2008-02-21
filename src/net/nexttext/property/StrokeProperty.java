//
// Copyright (C) 2006 Jason Lewis
//

package net.nexttext.property;

import java.awt.BasicStroke;

/**
 * A stroke property for a TextObject (using the BasicStroke implementation).
 * Allows to draw outlines of glyphs in NextText.
 *
 * <p> The stroke is centered on the control points of a glyph, which means
 * that for larger strokes, the control points will be inside of the stroke. 
 * Thus, large strokes will exceed the bounding polygon. </p>
 */
public class StrokeProperty extends Property {
    
    static final String REVISION = "$CVSHeader$";
    
    BasicStroke value;
    BasicStroke original;
    
    boolean isInherited = true;
    
    /**
     * Creates a new StrokeProperty using a stroke of width 1. 
     * The default attributes are a solid line of width 1.0, CAP_BUTT,
     * JOIN_ROUND, which give the best rendering with most glyphs.
     *
     * <p>The stroke property is inherited by default. </p>
     */
    public StrokeProperty() {
        original = value = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND); 
    }
    
    /**
     * Creates a new StrokeProperty from the java.awt.BasicStroke object.
     *
     * <p>The stroke property is not inherited by default. </p>
     */
    public StrokeProperty( BasicStroke stroke ) {
        original = value = stroke;
        setInherited( false );
    }
    
    /**
     * Do not modify the returned value, use set() to make changes instead.
     */
    public BasicStroke get() {         
        return value;
    }
    
    /**
     * Sets the Stroke property to the specified value.  Also sets the Inherited
     * property to false. 
     */
    public void set( BasicStroke newStroke ) {    
        value = newStroke;
        // property change event fired in setInherited
        setInherited( false );
    }
    
    /**
     * Returns the original stroke.
     */
    public BasicStroke getOriginal() {         
        return original;
    }
    
    /**
     * Modifies the original stroke value.
     */
    public void setOriginal( BasicStroke newStroke ) {       
        original = newStroke;
        firePropertyChangeEvent();
    }
    
    /**
     * Setting inherited to true on a StrokeProperty will cause it to bypass
     * it's current stroke value in favor of the stroke value of its parent.
     * By default a StrokeProperty is not inherited.
     */
    public void setInherited( boolean inherited ) {
        this.isInherited = inherited;
        firePropertyChangeEvent();
    }
     
    /**
     * Returns the inherited status of this StrokeProperty.
     */
    public boolean isInherited() {
        return isInherited;
    }
    
    /**
     * Reset interface from superclass.  Resets the stroke to its original value.
     */
    public void reset() {
        value = original;
        firePropertyChangeEvent();
    }
}
