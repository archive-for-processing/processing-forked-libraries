package net.nexttext.renderer;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.nexttext.TextPage;

/**
 * An interface that represents classes capable of rendering a TextPage
 * to a component.
 *
 */
public interface TextPageRenderer {

    /**
     * In implementing this method do not call getGraphics on 
     * the component passed in as it will cause flickering.
     * 
     * <p>The component is only passed so that information such as bounding 
     * boxes can be determined if necessary. It is expected that many 
     * classes implementing this interface will have no use for the component</p>      
     */
    public void render(TextPage textPage, Graphics2D g, Component c);
    
}
