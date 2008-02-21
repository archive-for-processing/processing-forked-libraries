//
// Copyright 2004 Jason Lewis
//

package net.nexttext.property;

/**
 * A property's datetime value of a TextObject or Behaviour.
 */

public class EP extends Property {

    static final String REVISION = "$CVSHeader$";

     originalValue;
     currentValue;
     futureValue;

    /** Reset this property to its original value. */
    public void reset() {
        set(getOriginal());
    }

    /**
     * Called when the frame changes, to trigger copying of the future value
     * into the current value.
     */
    void frameChange() {
        currentValue = futureValue;
    }

    public Object clone() {
        NumberProperty that = (NumberProperty) super.clone();
        that.originalValue = originalValue;
        that.currentValue = currentValue;
        that.futureValue = futureValue;
        return that;
    }

}
