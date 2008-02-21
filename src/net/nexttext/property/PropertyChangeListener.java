//
//Copyright (C) 2005 Jason Lewis
//

package net.nexttext.property;

/** 
 * Listeners implemeting this interface can be notified when a property changes
 * and receive a reference to that property.
 */
public interface PropertyChangeListener {    
    public void propertyChanged( Property propertyThatChanged );
}
