//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

import java.util.Date;

/**
 * A datetime property of a TextObject or Behaviour.
 */

public class DateTimeProperty extends Property {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/DateTimeProperty.java,v 1.3 2005/05/16 16:55:47 dissent Exp $";

    Date original;
    Date value;
  
    public DateTimeProperty() {
        original = new Date();
        value = new Date();
    }

    public DateTimeProperty(Date date) {
        original = new Date(date.getTime());
        value = new Date(date.getTime());
    }

    /**
     * Do not modify the returned value, use set() to make changes instead.
     */
    public Date get() {
        return new Date(value.getTime());
    }

    public Date getOriginal() {
        return new Date(original.getTime());
    }

    public void set(Date date) {         
        value.setTime(date.getTime());
        firePropertyChangeEvent();
    }

    /** Reset this property to its original value. */
    public void reset() {         
        set(getOriginal());
        firePropertyChangeEvent();
    }

    // New Date objects are created in case someone misuses the
    // DateTimeProperty by modifying the internal objects.
    public Object clone() {
        DateTimeProperty that = (DateTimeProperty) super.clone();
        that.original = new Date(original.getTime());
        that.value = new Date(value.getTime());
        return that;
    }
}
