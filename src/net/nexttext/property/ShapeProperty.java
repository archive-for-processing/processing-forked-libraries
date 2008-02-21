//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

import java.awt.Shape;

/**
 * A shape property of a TextObject or a Behaviour.
 *
 * <p>This is a property wrapper for java.awt.Shape.  </p>
 */
// The clones will share the same internal Shape objects.  This is necessary
// because Shape is an interface, so there is no way to know if the objects
// themselves implement Cloneable.  It is not a problem because Shapes cannot
// be modified, so changing the shape will replace the reference.  If the Shape
// objects change on their own there is no problem because sharing those
// changes seems like the correct behaviour.

public class ShapeProperty extends Property {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/ShapeProperty.java,v 1.3 2005/05/16 16:55:47 dissent Exp $";

    Shape original;
    Shape value;

	/**
	 * Creates a new ShapeProperty with a copy of the provided Shape.
	 */
    public ShapeProperty(Shape shape) {
        original = shape;
        value = shape;
    }

    /**
     * Do not modify the returned value, use set() to make changes instead.
     */
    public Shape get() {
        return value;
    }

    public void set(Shape shape) {        
        value = shape;
        firePropertyChangeEvent();
    }

    public void reset() {
        set(original);
    }
}
