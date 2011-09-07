package net.nexttext.property;

import net.nexttext.renderer.util.TessData;

public class TessDataProperty extends Property {
	
	private TessData original;
	private TessData value;
	
	public TessDataProperty(TessData data) {
		original = data;
		value = data.clone();	
	}
	
	public TessDataProperty(TessData original, TessData value) {
		this.original = original;
		this.value = value;	
	}
	
	/**
	 * @return a copy of the original value of this property .
	 */
	public TessData getOriginal() { 
        return original.clone();
	}
	
	/**
	 * Set the original value of the property.
	 */
	public void setOriginal(TessData original) {
		this.original = original;	
	    firePropertyChangeEvent();
	}
	
	/**
	 * @return a copy of the value of this property.
	 */
	public TessData get() {
	    return value.clone();
	}
	
	/**
	 * Sets the value of this property. The object passed as a newValue will 
	 * be copied before it is assigned to the property's value. 
	 */
	public void set(TessData data) {
		value = data;
	    firePropertyChangeEvent();
	}
	
	@Override
	public void reset() {
		value = original.clone();
		firePropertyChangeEvent();
	}

}
