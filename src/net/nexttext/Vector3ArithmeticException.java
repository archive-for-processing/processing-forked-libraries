//
// Copyright 2004 Jason Lewis
//

package net.nexttext;

/**
 * An illegal Vector3 operation has been performed.
 *
 * <p>Notably, any attempt to normalize the zero vector, or determine angles
 * with the it will result in this exception.  </p>
 */

public class Vector3ArithmeticException extends Exception {

    static final String REVISION = "$CVSHeader$";

    public Vector3ArithmeticException(String message) { super(message); }
}
