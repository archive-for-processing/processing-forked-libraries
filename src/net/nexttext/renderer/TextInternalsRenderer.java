//
// Copyright 2004 Jason Lewis
//

package net.nexttext.renderer;

import net.nexttext.*;

import java.awt.Component;
import java.awt.Graphics2D;

import java.util.Iterator;

/**
 * This renderer prints out the internals of the TextPage tree to the console.
 *
 * <p>It's good for debugging when you want to know what's going on.</p>
 */

public class TextInternalsRenderer implements TextPageRenderer{

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/renderer/TextInternalsRenderer.java,v 1.2 2004/07/22 14:30:50 dissent Exp $";

    public void render(TextPage textPage, Graphics2D g, Component c) {
        processNode(textPage.getTextRoot());        
    }

    // Rendering is done recursively.  A prefix is maintained so that objects
    // are indented further deeper in the tree.

    String prefix = "";

    void processNode(TextObject node) {
        if (node == null) return;
        System.out.print(prefix);
        if (node instanceof TextObjectGlyph) {
            System.out.println("<glyph> " + ((TextObjectGlyph) node).getGlyph());
            processProperties(node);
        } else {
            System.out.println("<node>");
            processProperties(node);
            String oldPrefix = prefix;
            prefix = prefix + "  ";
            processNode(((TextObjectGroup) node).getLeftMostChild());
            prefix = oldPrefix;
        }
        processNode(node.getRightSibling());
    }

    void processProperties(TextObject node) {
        String pp = prefix + "    ";
        Iterator i = node.getPropertyNames().iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            System.out.println(pp + name + " = " + node.getProperty(name));
        }
    }

}
