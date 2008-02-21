//
// Copyright 2004 Jason Lewis
//

package net.nexttext;

/**
 * The text data tree structure cannot be modified in that way.
 */

public class DataTreeException extends RuntimeException {

    static final String REVISION = "$CVSHeader$";

    DataTreeException(String message) { super(message); }
}
