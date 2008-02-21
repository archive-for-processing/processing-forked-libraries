//
// Copyright 2004 Jason Lewis
//

package net.nexttext.input;

import java.util.NoSuchElementException;

/**
 * An iterator over the events of a specifed input source.
 *
 * <p>The iterator keeps the array index of the last event it fetched.  When 
 * new events are added to the list, the iterator can fetch the new events, 
 * one by one, until the last one is reached.</p>
 */

public class InputSourceIterator {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/input/InputSourceIterator.java,v 1.2.2.1 2005/04/15 16:16:23 dissent Exp $";

	// The event list of the iterator.
	InputSource source;
	
	// The last index fetched by the iterator.
	// -1 when the iterator never fetched from the list.
	int lastFetchedEventIndex = -1;

	/**
	 * Class constructor.
	 *
	 * @param	source		the input source to iterator over
	 */
	InputSourceIterator(InputSource source) {
		this.source = source;
	}

	/**
	 * If there is an event waiting.
	 *
	 * <p>Even if it returns false, it may return true later, if a new event
	 * has occurred.  This is different behaviour than java.util.Iterator.  <p>
	 */
	public boolean hasNext() {
        synchronized (source.events) {
            return ((source.latestEventIndex != -1) &&
                    (lastFetchedEventIndex != source.latestEventIndex));
        }
	}
	
	/**
	 * Returns the next object in the iteration
	 *
     * @throws NoSuchElementException if there's no element available.
	 */
	public InputEvent next() {
        synchronized (source.events) {
            if (!hasNext())
                throw new NoSuchElementException("No more elements");

            lastFetchedEventIndex = source.incrementIndex(lastFetchedEventIndex);
            return source.events[lastFetchedEventIndex];
        }
	}
}
