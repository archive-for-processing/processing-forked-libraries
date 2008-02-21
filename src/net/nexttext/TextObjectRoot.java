//
// Copyright (C) 2005 Jason Lewis
//

package net.nexttext;

/**
 * The TextObjectRoot is just like a regular {@link TextObjectGroup} except that 
 * it contains a reference to the {@link Book} it belongs too. 
 * 
 * <p>As such, one can get to the Book from any {@link TextObject} by moving up 
 * the hierarchy until the instance of TextObjectRoot is found. </p>
 */
public class TextObjectRoot extends TextObjectGroup {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/TextObjectRoot.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";
    
    /**
     * The constructor has private access so the only way to obtain a
     * TextObjectRoot is through the package-only method create()
     */
    TextObjectRoot( Book book ) {
        super();
        this.book = book;
    }
}
