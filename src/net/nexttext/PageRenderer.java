package net.nexttext;

import java.awt.Component;
import java.util.Collection;


/**
 * The PageRenderer interface defines a basic set of requirements that must be met 
 * by any renderer in order to be usable by the library's Simulator.
 * 
 * <p>PageRenderers do not render text and graphics directly but rather provide a surface 
 * (usually a component) onto which Pages can render themselves.</p>
 * 
 */
public interface PageRenderer {
    
    static final String REVISION = "$CVSHeader:$";
    
    /**
     * This method is responsible for rendering the set of pages passed
     * to it.
     * 
     * <p>Typically it will consist of some set up, e.g. clearing the screen to the background
     * colour. Followed by calling the render method of each page with a graphics object the page
     * can draw to. Followed by finally displaying the new frame. For an example look
     * at Java2DRenderer</p> 
     * 
     */
    public void renderPages( Collection pages);
    
    /** 
     * A renderer must be able to return it's drawing surface, or canvas, to
     * the parent application.  This may be null, if the renderer is not
     * drawing to a Component.
     */
    public Component getCanvas();

}
