//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

import net.nexttext.property.Property;
import net.nexttext.Vector3;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A property for lists of Vector3s.
 *
 * <p>This class is half way between a list of Vector3Properties and a Property
 * containing a list of Vector3s, so is is not fully consistent with the other
 * property classes.  To make it consistent it would need these changes: </p>
 *
 * <ul>
 * <li>Change name to Vector3ListProperty. </li>
 *
 * <li>Change <code>add(Vector3Property)</code> to
 * <code>add(Vector3)</code>. </li>
 *
 * <li>Replace <code>Vector3Property get()</code> with <code>Vector3
 * get()</code> and <code>Vector3 getOriginal()</code>.
 *
 * <li>Add all of the <code>Vector3</code> mathematical methods.  </li>
 * </ul>
 */

public class Vector3PropertyList extends Property implements PropertyChangeListener {
	
	static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/property/Vector3PropertyList.java,v 1.7 2006/02/08 18:02:32 dissent Exp $";
	
	private ArrayList list;
	
	/**
	 * Default constructor.  Creates an empty Vector3PropertyList.
	 */
	public Vector3PropertyList() {
        list = new ArrayList();
	}
	
	/**
	 * Adds a Vector3Property object to the list.
	 */
	public void add( Vector3Property v1 ) {
        add(list.size(), v1);
	}
	
	/**
	 * Adds a Vector3Property object at the specified position in the list.
	 */
	public void add( int position, Vector3Property v1 ) {
        v1.addChangeListener(this);
		list.add( position, v1 );
		firePropertyChangeEvent();
	}
	
	/**
	 * Returns the Vector3Property object at the specified position in the list.
	 */
	public Vector3Property get( int position ) {
        return (Vector3Property)list.get(position);
	}
	
    /**
     * Resets each Vector3Property in the list to its original value.
     */
    public void reset() {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ((Vector3Property)i.next()).reset();
        }
        firePropertyChangeEvent();
    }
    
    /**
     * Clears out the content of the list
     */
    public void clear() {
    	list.clear();
    	firePropertyChangeEvent();
    } 
   
    public String toString() {
        StringBuffer ret = new StringBuffer();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ret.append(((Vector3Property)i.next()).toString());
        }
        return ret.toString();
	}

	/**
	 * Returns an iterator for the list of Vector3Property objects.
     *
     * <p>Don't use this iterator to remove items from the list, since this
     * won't trigger the necssary PropertyChangeEvents.  If you need to remove
     * points, write a remove method for this class, and call that.  </p>
	 */
	public Iterator iterator() {
		return list.iterator();
	}
	   
	/**
	 * Returns the number of Vector3Property objects containted in this list.
	 */
	public int size() {
		return list.size();
	}	

    /**
     * For interface PropertyChangeListener, called when one of the properties
     * in the list changes.
     */
    public void propertyChanged(Property pc) {
        firePropertyChangeEvent();
    }

    public Object clone() {
        Vector3PropertyList that = (Vector3PropertyList) super.clone();
        that.list = new ArrayList(list.size());
        Iterator i = list.iterator();
        while (i.hasNext()) {
            that.add((Vector3Property)((Vector3Property)i.next()).clone());
        }
        return that;
    }
}
