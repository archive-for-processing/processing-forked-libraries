//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.property;

import java.awt.Color;

/**
 * A colour property of a TextObject or a Behaviour.
 */
public class ColorProperty extends Property {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/ColorProperty.java,v 1.6 2006/06/02 21:30:27 dissent Exp $";
    
    Color value;
    Color original;
    
    boolean isInherited = true;
    
    /**
     * Creates a new ColorProperty using java.awt.Color.black by default.
     */
    public ColorProperty() {
        original = value = Color.black; 
    }
    
    /**
     * Creates a new ColorProperty from the java.awt.Color object.
     * 
     * <p>This color property is no longer inherited by default. </p>
     */
    public ColorProperty( Color color ) {
        original = value = color;
        setInherited( false );
    }
    
    /**
     * Do not modify the returned value, use set() to make changes instead.
     */
    public Color get() {         
        return value;
    }
    
    /**
     * Sets the Color property to the specified value.  Also sets the Inherited
     * property to false. 
     */
    public void set( Color newColor ) {       
        value = newColor;
        // property change event fired in setInherited
        setInherited( false );
    }
    
    public Color getOriginal() {         
        return original;
    }
    
    public void setOriginal( Color newColor ) {       
        original = newColor;
        firePropertyChangeEvent();
    }
    
    /**
     * Setting inherited to true on a ColorProperty will cause it to bypass
     * it's current color value in favor of the color value of it's parent.
     * By default a ColorProperty is inherited.
     */
    public void setInherited( boolean inherited ) {
        this.isInherited = inherited;
        firePropertyChangeEvent();
    }
     
    /**
     * Returns the inherited status of this ColorProperty.
     */
    public boolean isInherited() { return isInherited; }
    
    /**
     * Reset interface from superclass.  Resets the color to its original value.
     */
    public void reset() {
        value = original;
        firePropertyChangeEvent();
    }

    // New Color objects are created in case someone misuses the ColorProperty
    // by modifying the internal objects.
    public Object clone() {
        ColorProperty that = (ColorProperty) super.clone();
        that.value = new Color(value.getRed(),
                               value.getGreen(),
                               value.getBlue(),
                               value.getAlpha());
        that.original = new Color(original.getRed(),
                                  original.getGreen(),
                                  original.getBlue(),
                                  original.getAlpha());
        return that;
    }
}
