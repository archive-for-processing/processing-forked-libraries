//
// Copyright 2004 Jason Lewis
//

package net.nexttext;

/**
 * A utility class used to traverse the Glyph children of a TextObjectGroup.
 */
public class TextObjectGlyphIterator {

    static final String REVISION = "$CVSHeader$";

    // It buffers the next Glyph to be returned.  This is necessary to do
    // hasNext() properly.
    TextObjectGlyph next;

    // It can't extend TextObjectIterator, since it needs to change the return
    // type of next().  Instead it wraps it.
    TextObjectIterator iterator;

    TextObjectGlyphIterator(TextObjectGroup group) {
        iterator = new TextObjectIterator(group);
        bufferNextGlyph();
    }

    private void bufferNextGlyph() {
        while (iterator.hasNext()) {
            TextObject to = iterator.next();
            if (to instanceof TextObjectGlyph) {
                next = (TextObjectGlyph) to;
                return;
            }
        }
        next = null;
    }

	/** If the traversal is complete. */
	public boolean hasNext() { return next != null; }

	/** Get the next node in the traversal. */
	public TextObjectGlyph next() {
        TextObjectGlyph current = next;
        bufferNextGlyph();
        return current;
	}
}
