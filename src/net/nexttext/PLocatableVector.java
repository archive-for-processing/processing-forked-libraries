package net.nexttext;

import processing.core.PVector;

public class PLocatableVector extends PVector implements Locatable {

	/**
	 * Default constructor -- x, y and z gets assigned the value 0 by default.
	 */
	public PLocatableVector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Constructs and initialize using xy properties ( z = 0 by default ).
	 */
	public PLocatableVector( float x, float y )
	{
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	/**
	 * Constructs and initialize using xyz properties.
	 */
	public PLocatableVector( float x, float y, float z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/** Construcs and initialize using a PVector. */
	public PLocatableVector( PVector vector ) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
    /**
     * A PLocatableVector implements the Locatable interface so that it can be used as a 
     * "location constant".
     */
	public PVector getLocation() {
		return new PVector(x, y, z);
	}

}
