//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

/**
 * A number property of a TextObject or Behaviour.
 *
 * <p>This class handles both integer and floating point type numbers.  It
 * seemed easier to do this in one class, rather than having a class for each
 * Java primitive numerical type.  Internally, the values are stored as
 * doubles, and cast as appropriate.</p>
 */

public class NumberProperty extends Property {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/NumberProperty.java,v 1.9 2005/05/16 16:55:47 dissent Exp $";

    double original;
    double value;
  
    public NumberProperty(double value) {
        original = value;
        this.value = value;      
    }

    public double getOriginal() { return original; }

    public double get() { return value; }

    public void set(double value) { 
       this.value = value;
       firePropertyChangeEvent();
    }

    public void add(double value) {
        this.value += value;
        firePropertyChangeEvent();
    }

    public void reset() {
        set(getOriginal());
    }

    public String toString() {
        return "(" + Double.toString(original) +
            ", " + Double.toString(value);
    }

    // Accessors in case it's easier to treat a number property as a long.

    public NumberProperty(long value) {
        original = (double) value;
        this.value = (double) value;     
    }

    public long getOriginalLong() { return (long) original; }
    public long getLong() {
        return (long) value;
    }

    public void set(long value) { 
        this.value = (double) value;
        firePropertyChangeEvent();
    }

    public void add(long value) {
        value += (double) value;
        firePropertyChangeEvent();
    }
}
