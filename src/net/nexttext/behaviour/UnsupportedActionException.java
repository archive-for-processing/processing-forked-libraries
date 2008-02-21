//
//Copyright 2004 Jason Lewis
//

package net.nexttext.behaviour;

/**
 * This exception is thrown when an unsupported behave() method is called on on
 * an Action.
 */
public class UnsupportedActionException extends RuntimeException {
    
    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/behaviour/UnsupportedActionException.java,v 1.1.2.1 2005/04/11 18:09:26 david_bo Exp $";

    public UnsupportedActionException(String message) { super(message); }
}
