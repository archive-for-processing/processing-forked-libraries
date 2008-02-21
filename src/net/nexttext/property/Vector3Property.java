//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

import net.nexttext.property.Property;
import net.nexttext.Vector3;
import net.nexttext.Vector3ArithmeticException;

/**
 * A vector3 property of a TextObject or a Behaviour.
 * 
 * <p>Note that defensive copies of the current value and and original value of the
 * properties are always created when getting or setting the property.</p>  
 * 
 * <p>Also note that every wrapper method around Vector3 arithmetic operations 
 * will fire a property change event.   Maybe we want to consider removing these
 * wrappers to lower the event overhead. </p>
 * 
 * @see Vector3
 */

public class Vector3Property extends Property {
	
	static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/Vector3Property.java,v 1.20 2006/05/01 14:59:52 dissent Exp $";

	private Vector3	original;
	private Vector3 value; 
	
	/**
	 * Constructor.  Creates a new Vector3Property based on the object passed
	 * as a parameter.  The current and original values for that property will
	 * both be a copy of the parameter value.
	 */
	public Vector3Property ( Vector3 value ) {
		original = new Vector3(value);
		this.value = new Vector3(value);	 
	}
	
	/**
	 * Creates a Vector3Property from 3 doubles.
	 */
	public Vector3Property(double x, double y, double z) {
		original = new Vector3 (x, y, z);
		value = new Vector3(x, y, z);		
	}

	/**
	 * Creates a Vector3 property from 2 doubles, using 0 as the z coordainte.
	 */
	public Vector3Property(double x, double y) {
        this(x, y, 0);
	}
	
    /**
     * Construct a property with specified original and future values.
     *
     * <p>The provided vectors are copied.  This function is used for the
     * special purpose of calculating an absolute position from the natively
     * stored relative positions.  </p>
     */
	public Vector3Property(Vector3 original, Vector3 value ) {
		this.original = new Vector3(original);
		this.value = new Vector3(value);		
	}
	
	/**
	 * Returns a copy of the original value of this property 
	 */
	public Vector3 getOriginal() { 
        return new Vector3( original );
	}
	
	/**
	 * Returns the value of this property.
     *
     * @return A copy of the value of this property.
	 */
	public Vector3 get() {
	    return new Vector3( value );
	}
	
	/**
	 * Sets the value of this property.   The object passd as a newValue will 
	 * be copied before it is assigned to the property's value. 
	 */
	public void set( Vector3 newValue ) {
	    value.x = newValue.x;
	    value.y = newValue.y;
	    value.z = newValue.z;
	    firePropertyChangeEvent();
	}
	
    /**
     * Replaces the value of this property by its original value.
     */
    public void reset() {
        value = new Vector3( original );
        firePropertyChangeEvent();
    }

    public double getX() {
        return value.x;
    }

    public double getY() {
        return value.y;
    }

    public double getZ() {
        return value.z;
    }

   	/** 
   	 * Wrapper around vector addition of Vector3 class
   	 */
   	public void add( Vector3 v1 ) {   		
   		value.add(v1);
   		firePropertyChangeEvent();
   	}
   	
   	/**
   	 * Wrapper around vector substraction of Vector3 class
   	 */
   	public void sub( Vector3 v1 ) {   		
   		value.sub(v1);
   		firePropertyChangeEvent();
   	}
   	
   	/**
   	 * Wrapper around vector cross product of Vector3 class
   	 */
   	public void cross( Vector3 v1 ) {   	 
   		value.cross(v1);	
   		firePropertyChangeEvent();
   	}

   	/**
   	 * Wrapper around vector matrix product of Vector3 class
   	 */
   	public void matrix( Vector3 v1 ) {   	 
   		value.matrix(v1);	
   		firePropertyChangeEvent();
   	}

   	/**
   	 * Wrapper around vector scalar product of Vector3 class
   	 */
   	public void scalar( double s ) {   	
   		value.scalar(s);	
   		firePropertyChangeEvent();
   	}
   	
   	/**
   	 * Wrapper around vector dot product of Vector3 class
   	 */
   	public double dot( Vector3 v1 ) {
   		return value.dot(v1);	   		 
   	}
   	
   	/**
   	 * Wrapper around vector normalization of Vector3 class
   	 */
   	public void normalize() throws Vector3ArithmeticException {   		
   		value.normalize();	
   		firePropertyChangeEvent();
   	}
	 
   	/**
   	 * Wrapper around vector rotation of Vector3 class
   	 */
   	public void rotate(double angle) {   		 
   	    value.rotate(angle);
   	    firePropertyChangeEvent();
   	}

	/**
	 * Wrapper around toString() function of Vector3 class.
     */
    public String toString() {
        return "(" + original.toString() +
            ", " + value.toString() + ")";
	}

    // New Vector3 objects are created in case someone misuses the
    // Vector3Property by modifying the internal objects.
    public Object clone() {
        Vector3Property that = (Vector3Property) super.clone();
        that.original = new Vector3(original);
        that.value = new Vector3(value);
        return that;
    }

}
