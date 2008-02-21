//
// Copyright 2004 Jason Lewis
//

package net.nexttext;

/**
 * This exception is thrown whenever an object was expect to be part of a list
 * but isnt there.
 */

public class ObjectNotFoundException extends RuntimeException {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/ObjectNotFoundException.java,v 1.1.6.1 2005/04/12 16:11:40 dissent Exp $";

    public ObjectNotFoundException(String message) { super(message); }
}
