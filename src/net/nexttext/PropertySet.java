//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.nexttext.property.Property;

/**
 * The PropertySet encapsulates the named properties of an object.  
 * 
 * <p>The PropertySet is a map from property names to Property objects.  It is
 * similar to java.util.Map, except that once a Property object has been added
 * to the PropertySet that object cannot be removed or replaced, the name will
 * always map to the very same Property object.  Making the Properties static
 * in this way makes it easier to write code which uses Properties.  It allows
 * original Property values to be stored correctly, it reduces the number of
 * checks that have to be done in behaviours, and means that concurrent access
 * is feasible.  </p>
 */
public class PropertySet {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/PropertySet.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";
    
    HashMap properties = new HashMap();
    
    /**
     * Initialize the property with this value, if it's not already defined.
     *
     * <p>The provided property is cloned before it is added to the property
     * list.</p>
     */
    public void init(String name, Property value) {
        if (!properties.containsKey(name)) {
            value.setName(name);
            properties.put(name, value.clone());
        }
    }

    /**
     * Initialize all the properties in the map, if not already defined.
     *
     * <p>See initProperty(String, Property).  Map keys must be Strings, values
     * must be Properties.</p>
     */
    public void init(Map properties) {
        // Initialize the required properties on the TextObject.
        Iterator i = properties.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = (Map.Entry) i.next();
            init((String) e.getKey(), (Property) e.getValue());
        }
    }
    
    /** Get the named property, null if it's not there. */
    public Property get(String name) {
        return (Property) properties.get(name);
    }

    /** Names of all the properties, in an unmodifiable set. */
    public Set getNames() {
        return java.util.Collections.unmodifiableSet(properties.keySet());
    }
    
    /**
     * Resets all the properties to their original value.
     */
    public void reset() {        
        java.util.Iterator i = getNames().iterator();
        while (i.hasNext()) {
            get((String) i.next()).reset();
        }
    }
}
