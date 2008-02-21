package net.nexttext;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * A page represents a layer of content in a nexttext application, typically each
 * layer contains text or graphics. 
 *
 */
public interface Page {
    
    static final String REVISION = "$CVSHeader:$";
    
    /**
     * In implementing this method do not call getGraphics on 
     * the component passed in as it will cause flickering.
     * 
     * <p>The component is only passed so that information such as bounding 
     * boxes can be determined if necessary. It is expected that many 
     * classes implementing this interface will have no use for the component</p>      
     */
    public void render(Graphics2D g, Component c);
    
}
