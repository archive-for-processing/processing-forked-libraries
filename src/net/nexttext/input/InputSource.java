//
// Copyright 2004 Jason Lewis
//

package net.nexttext.input;

/**
 * An interface to external events.
 *
 * <p>InputSources are generally used by Behaviours which want to change what
 * they do based on external information.  An InputSource can be accessed in
 * two ways, as a series of events, or as an object with state.  To access
 * events, a Behaviour gets an InputSourceIterator from the input source, and
 * reads events from it.  To access state, a Behaviour uses InputSource
 * specific state accessors.  </p>
 *
 * <p>Events are buffered internally in the InputSource, so that all Behaviours
 * will see all events.  If a Behaviour is slow in accessing events, they may
 * be flushed from the buffer, and it will miss events.  </p>
 */

public abstract class InputSource {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/input/InputSource.java,v 1.8 2005/05/16 16:55:47 dissent Exp $";

    // Events are stored internally in an array, with a pointer to the most
    // recent event.  Events are added to the array, and then the pointer is
    // incremented, or wrapped as appropriate.

	InputEvent[] events = new InputEvent[1024];

	// The array index of the latest event generated (added) to the list.
	// -1 for an empty list
	int latestEventIndex = -1;

    /** Used in the iterator also. */
    int incrementIndex(int oldIndex) { return (oldIndex + 1) % events.length; }

	/**
	 * Adds an event object to the list.
	 *
	 * @param	event	the event object to insert
	 */
	protected void addEvent(InputEvent event) {
        synchronized (events) {
            latestEventIndex = incrementIndex(latestEventIndex);
            events[latestEventIndex] = event;
        }
	}

	/**
	 * Gets an iterator over the list of events of the input source.
	 *
	 * @return		an iterator over the list of events
	 */
	public InputSourceIterator getIterator() {
		return new InputSourceIterator(this);
	}

}
