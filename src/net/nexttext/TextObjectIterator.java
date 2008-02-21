//
// Copyright 2004 Jason Lewis
//

package net.nexttext;

import java.util.Stack;

/**
 * A utility class used to traverse the children of a TextObjectGroup.
 *
 * <p>The traversal is done depth-first, meaning that all of a node's children
 * will be traversed before that node is.  This behaviour is necessary for the
 * Simulator to update bounding boxes correctly.  If you find a need for a
 * bread-first traversal, then you should make it an option in this class. </p>
 *
 * <p>The node itself will be returned as part of the traversal. </p>
 */
public class TextObjectIterator {

    static final String REVISION = "$CVSHeader$";

    // The state of the iteration is maintained in a stack, which has the next
    // node on top, with all of its ancestors to the top of them traversal
    // above it.  Getting the next node means returning the top of the stack,
    // then finding the next node to traverse and pushing it, and any
    // appropriate ancestors on top of the stack.

	Stack ancestors = new Stack();

	/** Construct an iterator over the group and its descendants. */
	TextObjectIterator( TextObjectGroup group ) {
        descend(group);
	}

    // Push the provide TextObject, and all of its left descendants onto the
    // stack.  This causes the traversal to start at the bottom.
    private void descend(TextObject to) {
        while (to != null) {
            ancestors.push(to);
            if (to instanceof TextObjectGroup) {
                to = ((TextObjectGroup) to).getLeftMostChild();
            } else {
                to = null;
            }
        }
    }

	/** If the traversal is complete. */
	public boolean hasNext() {
		return !ancestors.empty();
	}

	/** Get the next node in the traversal. */
	public TextObject next() {

		TextObject current = (TextObject) ancestors.pop();

        // Put the next object on the stack.  If we're returning the object
        // orignally provided (the stack is empty), then there's nothing left
        // to traverse, so don't push anything onto the stack.  If there's no
        // right sibling, then the next object is the parent, which is already
        // on the stack.
		if ( (!ancestors.empty()) && (current.getRightSibling() != null) ) {
            descend(current.getRightSibling());
		}

	 	return current;
	}
}
