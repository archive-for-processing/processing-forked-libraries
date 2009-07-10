/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.nexttext;

/**
 * A basic class representing a vector.
 *
 * This class will need the usual vector operations.  (Another option is to use
 * javax.vecmath.Vector3D, however this adds an extra dependency on the Java3D
 * API only for this one simple class..)
 */
/* $Id$ */
public class Vector3 implements Locatable {

	public final static Vector3 ZERO = new Vector3(0, 0, 0);

	public final static Vector3 UNIT_X = new Vector3(1, 0, 0);
	public final static Vector3 UNIT_Y = new Vector3(0, 1, 0);
    public final static Vector3 UNIT_Z = new Vector3(0, 0, 1);
    public final static Vector3 UNIT_XYZ = new Vector3(1, 1, 1);

	/**
	 * The vector's coordinates are defined as public data members
	 */
	public double x;
	public double y;
	public double z;

	/**
	 * Default constructor -- x, y and z gets assigned the value 0 by default.
	 */
	public Vector3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Constructs and intialize using xy properties ( z = 0 by default ).
	 */
	public Vector3( double x, double y )
	{
		this.x = x;
		this.y = y;
		this.z = 0;
		checkValue();
	}

	/**
	 * 3D Constructor. This is put in here in case we support 3D operations.
	 */
	public Vector3( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		checkValue();
	}

	/**
	 * Construct a Vector3 object from another Vector3
	 */
	public Vector3( Vector3 v1 ) {
		this.x = v1.x;
		this.y = v1.y;
		this.z = v1.z;
	}
	
	private boolean stopChecking = true;
	
	private void checkValue() {
		if (stopChecking) return;
		try {
			if (x > 10000000)
				throw new RuntimeException("x: " + x);
			if (y > 10000000)
				throw new RuntimeException("y: " + y);
			if (z > 10000000)
				throw new RuntimeException("z: " + z);
		} 
		catch (RuntimeException e) {
				e.printStackTrace();
				stopChecking = true;
		}
	}
	
	/**
	 * Returns a string representation of the vector
	 */
	public String toString() {

		return "[" + this.x + "," + this.y + "," + this.z + "]"; 
	}

	/**
     * Modify this Vector by adding the given one to it.
 	 */
	public Vector3 add( Vector3 v1 ) {
		this.x += v1.x;
		this.y += v1.y;
		this.z += v1.z;
		checkValue();
		return this;
	}
	
	/**
     * Modify by subtracting the given one from it.
 	 */
	public Vector3 sub( Vector3 v1 ) {
		this.x -= v1.x;
		this.y -= v1.y;
		this.z -= v1.z;
		checkValue();
		return this;
	}

	/**
     * Modify this Vector to be the cross product of it and the given one.
 	 */
	 public Vector3 cross( Vector3 v1 ) {
	 	this.x = (this.y * v1.z) - (v1.y * this.z);
	 	this.y = (this.z * v1.x) - (v1.z * this.x);
	 	this.z = (this.x * v1.y) - (v1.x * this.y);
	 	checkValue();
	 	return this;
	 }

	/**
     * Modify this Vector to be the matrix product of it and the given one.
     *
     * <p>Multiply this Vector with a diagonal matrix represented by the other
     * Vector.  this.x becomes (this.x * that.x), etc. </p>
 	 */
	 public Vector3 matrix( Vector3 v1 ) {
         this.x = this.x * v1.x;
         this.y = this.y * v1.y;
         this.z = this.z * v1.z;
         checkValue();
         return this;
	 }

	/**
     * Modify this Vector by multiplying it with the given scalar.
 	 */
	 public Vector3 scalar( double s ) {
         this.x = this.x * s;
         this.y = this.y * s;
         this.z = this.z * s;
         checkValue();
         return this;
	 }

	 /**
      * Return the dot product of this vector with the given one.
	  */
	 public double dot( Vector3 v1 ) {
	 	return (this.x * v1.x ) + (this.y * v1.y) + (this.z * v1.z);
	 }

    /**
     * Return the length of this vector.
     */
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

	 /**
	  * Modify this Vector by changing its length to 1, but preserving its
	  * direction.
	  */
    public Vector3 normalize() {
        double t = Math.sqrt(x*x + y*y + z*z);
        if (t != 0) {
            x = x/t;
            y = y/t;
            z = z/t;
        }
        checkValue();
        return this;
    }
    
    /**
     * Modify this Vector by rotating it by the specified angle in radians around
     * the Z axis.
     * @param angle
     * @return rotated vector
     */
    public Vector3 rotate( double angle ) { return rotateZ(angle); }
    
    /**
     * Modify this Vector by rotating it by the specified angle in radians around
     * the Z axis.
     * 
     * <p>A positive angle will rotate the vector in a clockwise direction from 
     * the viewer's perspective. </p>
     * 
     * <p>Note that internally, the (x,y) coordinates of the Vector are modified
     * in a counter clockwise fashion.  This is due to the fact that unlike the 
     * traditional cartesian system, the screen has a mirrored Y axis. </p> 
     * 
     * <p>The reason why we have this disrepancy is because we need to mirror's 
     * Java 2D rotation convention. </p>
     */
    public Vector3 rotateZ( double angle ) {
        // because the screen coordinate are mirrored along the X axis (ie: a 
        // negative Y means going out of the screen), we must first negate angle
        // to get clockwise rotation.  
        // the result will be a rotation that is looks clockwise on screen.
        angle = -angle;
        
        double newX = x * Math.cos(angle) + y * Math.sin(angle);
        double newY = - x * Math.sin(angle) + y * Math.cos(angle);
        x = newX;
        y = newY;
        checkValue();
        return this;
    }

    /**
     * Return the angle between this vector and the x axis.
     */
    public double theta() throws Vector3ArithmeticException { 
        if (isZero()) {
            throw new Vector3ArithmeticException("Can't theta the zero vector");
        }
        double theta = Math.acos(x / length());
        return ( y > 0 ) ? theta : -theta;
    }
    
    /**
     * Return the absolute angle between this Vector and the provided one.
     */
    public double theta(Vector3 other) throws Vector3ArithmeticException {
        double diffTheta = theta() - other.theta();
        while (diffTheta >= Math.PI * 2) { diffTheta -= Math.PI * 2; }
        while (diffTheta < 0) { diffTheta += Math.PI * 2; }
        return (diffTheta <= Math.PI) ? diffTheta : Math.PI * 2 - diffTheta;
    }
    
    /**
     * Indicate if this is a zero-length vector.
     */
    public boolean isZero() {
        return length() == 0;
    }

    /**
     * A Vector3 implements the Locatable interface so that it can be used as a 
     * "location constant".
     */
    public Vector3 getLocation() {
        return new Vector3(x,y,z);
    }

    /**
     * are these two vectors the same? they are is they both have the same x,y,
     * and z values.
     *
     * @param o
     *            the object to compare for equality
     * @return true if they are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof Vector3)) { return false; }

        if (this == o) { return true; }

        Vector3 comp = (Vector3) o;
        if (Double.compare(x,comp.x) != 0) return false;
        if (Double.compare(y,comp.y) != 0) return false;
        if (Double.compare(z,comp.z) != 0) return false;
        return true;
    }
}
