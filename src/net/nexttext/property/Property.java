//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * A property value of a TextObject or Behaviour.
 *
 * <p>TextObjects and Behaviours both keep properties accessible by name.
 * Subclasses of this class are used to hold the values of these properties.
 * Each property keeps an origin value and a current value.</p> 
 *
 * <p>Property implements Cloneable so that a TextObject's properties can be
 * copied easily.  </p>
 */

public abstract class Property implements Cloneable {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/Property.java,v 1.8 2005/05/16 16:55:47 dissent Exp $";
    
    private Collection listeners = new Vector();
    private String name = "";
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Registers a new PropertyChangeListener for this property.
     */
    public void addChangeListener( PropertyChangeListener listener ) {
        listeners.add( listener );
    }
   
    protected void firePropertyChangeEvent() {         
        for ( Iterator i = listeners.iterator(); i.hasNext(); ) {
                ((PropertyChangeListener)i.next()).propertyChanged(this);
        }
    }

    /**
     * Get a new property with the same values as this one.
     *
     * <p>The name is copied because that's what makes it a Property and not
     * just a value.  </p>
     *
     * <p>PropertyChangeListeners are not copied to the new Property.  </p>
     */
    public Object clone() {
        try {             
            Property that = (Property) super.clone();
            that.listeners = new Vector();
            return that;
        } catch (CloneNotSupportedException cnse) {
            throw new RuntimeException(cnse);
        }
    }
    
    /** Reset this property to its original value. */
    public abstract void reset();    
}
