//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext.behaviour;

import java.util.LinkedHashSet;
import java.util.Set;

import net.nexttext.TextObject;

/**
* AbstractBehaviours act on a list of TextObjects, and are included in the
* simulation.
*
* <p>The TextObjects to act on are stored in a list internal to the Behaviour.
* To include an AbstractBehaviour in the simulation, it is added to the book,
* which calls the behaveAll() method, triggering the behaviour to call actions
* on each of its TextObjects.  Subclasses of AbstractBehaviour differ in how
* they implement the behaveAll() method. </p>
*/

public abstract class AbstractBehaviour  {
 
	 static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/AbstractBehaviour.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";
	 
	 /*
	  * This displayName code is duplicated in AbstractAction, however it is quite
	  * simple so it doesn't justify creating a new baseclass just for this..
	  */
	 private String displayName;
	 
	 /**
	  * Sets the display name of this Action instance to the specified string.   
	  */
	 public void setDisplayName( String name ) {
	     displayName = name;
	 }
	 
	 /**
	  * Returns the display name of this instance.  If no particular display name 
	  * was specified, the class name is used by default.
	  */
	 public String getDisplayName() {
	      
	     if ( displayName == "" ) {
	     	// use the class name if no name was specified
	         displayName = this.getClass().getName();
	     }
	     return displayName;
	 }
	 
	 //////////////////////////////////////////////////////////////////////
	 // TextObjects to act on.
	
	 // Objects are stored in a Set to guarantee uniqueness, and LinkedHash is
	 // nice because it provides a consistent order.  In general performance is
	 // not a major issue, because most accesses are iteration over the list.
	 
	 protected Set objects = new LinkedHashSet();
	 
	 /**
	  * Behave on every TextObject in the list.
	  *
	  * <p>This method is called as part of the simulation, and when a behaviour
	  * has been added to the Book.  Different subclasses will implement this
	  * method in different ways.  </p>
	  */
	 public abstract void behaveAll();
	 
	 /**
	  * Add a TextObject to this Behaviour's list of objects to act on.
	  *
	  * <p>Each Action requires a specific set of properties in TextObjects that
	  * it acts on.  These properties are added to the TextObjects when they are
	  * added to a behaviour for each Action which is a part of that behaviour.
	  * Subclasses of AbstractBehaviour have to be sure to perform this
	  * necessary step, by overriding this method, (but don't forget to call
	  * super.addObject()).  Behaviour provides a good example of how to do
	  * this.  </p>
	  *
	  * <p>If the TextObject is already in the set, nothing will be done, a
	  * single remove will still remove the object from the list.  </p>
	  */
	
	 public synchronized void addObject(TextObject to) {
	     objects.add(to);
	 }
	
	 /**
	  * Stop this behaviour from acting on a TextObject.
	  */    
	 public synchronized void removeObject(TextObject to) {
	     objects.remove(to);
	 }        
	 
	 public String toString() {
	     return getDisplayName();
	 }
}
