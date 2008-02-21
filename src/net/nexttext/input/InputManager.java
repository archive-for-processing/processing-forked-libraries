//
// Copyright 2004 Jason Lewis
//

package net.nexttext.input;

import java.awt.Component;

import java.util.HashMap;

/**
 * A manager for the input sources.
 *
 * <p>The input manager keeps input sources accessible by name.  The application
 *    adds input source objects to the manager and behaviors can fetch these
 *    objects to access their data.  All sources are stored in a
 *    {@link HashMap}.</p>
 *
 */

public class InputManager {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/input/InputManager.java,v 1.5 2004/08/23 15:15:50 dissent Exp $";

    // The hash sources are kept in.
    HashMap sources = new HashMap();
    
    /** Class constructor. 
     * Creates the default input sources (mouse and keyboard).
	 *
	 * @param component	the component the listeners are attached to
	 */
    public InputManager(Component component){ 
	    add("Mouse", new MouseDefault(component));          	
        add("Keyboard", new KeyboardDefault(component));          		 	
	 }
    
    /**
     * Adds an input source to the list.
     *
     * @param	name	key of the new input source
     * @param	source	the new input source object
     */
    public void add(String name, InputSource source) {
        if (!sources.containsKey(name)) {
            sources.put(name, source);
        }
    }
    
    /**
     * Removes an input source from the list.
     *
     * @param	name	key of the input source to remove
     */
    public void remove(String name) {
	   	sources.remove(name);
	}
    
    /**
     * Gets an input source from the list.
     *
     * @param	name	key of the input source to return
     * @return			input source specified by the key parameter.
     *					null if no source is attached to the specified key.
     * @see				InputSource
     */
    public InputSource get(String name) {
    	return (InputSource)sources.get(name);
    }

}
