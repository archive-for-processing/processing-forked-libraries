/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.nexttext.renderer;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.MemoryImageSource;
import java.awt.image.BufferStrategy;

import java.util.Collection;
import java.util.Iterator;

import net.nexttext.*;


/**
 * This is a renderer that uses the Java Graphics2D object to perform all the
 * drawing operations. It uses the BufferStrategy class to optimize the
 * rendering and take advantage of its buffering capabilities.
 * Look at http://java.sun.com/docs/books/tutorial/extra/fullscreen/index.html
 * for more details.
 */
/* $Id$ */
public class Java2DRenderer extends JFrame implements PageRenderer {

    /** antialias on/off flag.  on by default */
	boolean antialias = true;
	boolean fullscreen = false;
        
	int width = 0;
	int height = 0;
	
	DisplayMode originalDisplayMode;
    BufferStrategy buffer;

	//whether we should attempt to change display modes
    private boolean changeDisplayMode = true;
    
    //The panel that we will render to
    protected JPanel renderPanel;

	/**
	 * Default constructor.  Creates and initialize a JPanel for rendering
	 */
   
	public Java2DRenderer(int width, int height, String windowTitle) {
	    super(windowTitle);
        
        this.width = width;
        this.height = height;
        
        //Create a frame (window) to display the application
        //pack the frame before anything is added, get the
        //insets(size of borders + titlebar) and add them to the desired size
        this.pack();
        this.setSize(width + this.getInsets().left + this.getInsets().right,
                     height + this.getInsets().bottom + this.getInsets().top);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );        
        this.validate();
        this.setVisible(true);
        this.setIgnoreRepaint(true);   
		
		//Add a panel to this frame that we can draw to
        renderPanel = new JPanel(true);
        renderPanel.setSize( width, height );
        renderPanel.setPreferredSize( new Dimension(width, height) );        
        renderPanel.setIgnoreRepaint(true);        
        this.add(renderPanel);        
	}
    
    public Java2DRenderer(int width, int height){
        this(width,height,"NextText Application");
    }
    
    /** 
     * The starting point for rendering loop. Initializes the bufferStrategy,
	 * obtains a graphics objects and calls the render method of each page
     * passed into it. After which the buffer is drawn to the screen.
     */
    public void renderPages( Collection pages ) {
        try {
            initBuffer();
        } catch (NullPointerException npe) {
            System.out.println("No buffer: cannot render . Have you added your Renderer to a Frame?");
            npe.printStackTrace();
            System.exit(1);
        }
        
        // The graphics2D are drawn directly on the parent frame. 
        Graphics2D g2 = (Graphics2D)buffer.getDrawGraphics();        
        // When resizing, it's possible to loose the reference to the graphics
        // context, so we skip rendering the frame.
        if (g2==null){
            System.out.println("Skip rendering frame because the graphics " +
                    "context was lost temporarily.");
            return;
        }
        
        //Translate to avoid overlap with the Frame's titlebar a
        //and borders
        g2.translate(this.getInsets().left, this.getInsets().top);
        //Clear the background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());        

        // set the rendering hints
        if ( antialias ) {
           g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        }
        g2.setRenderingHint( RenderingHints.KEY_RENDERING , RenderingHints.VALUE_RENDER_SPEED );
        g2.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
                
        // render the pages
    	Iterator i = pages.iterator();
        while (i.hasNext()) {
            Page page = (Page)i.next();
            AffineTransform original = g2.getTransform();
            page.render(g2, this.getCanvas());
            g2.setTransform(original);
        }
        
        try {
            buffer.show();
        } catch (NullPointerException npe) {
            System.out.println("Temporarily lost the drawing buffer when resizing");
        }
    } // end rendering
    
    /**
     * Sets the background color.
     */    
    public void setBackgroundColor( Color color ) {
        setBackground( color );
	}
	
	/**
	 * Returns the current background color.
	 */
    public Color getBackgroundColor() {
        return getBackground();
    }
	
	/**
	 * Turns on Java2D antialising
	 */
	public void enableAntialiasing() {
		antialias = true;
	}
    
    /**
     * Turns off Java2D antialising
     */
    public void disableAntialiasing() {
        antialias = false;
    }
	
	/**
	 * Returns the JPanel used for drawing
	 *
	 * @return Component	The drawing surface
	 */
	public Component getCanvas() { 
		return renderPanel;
	}
    
    /**
     * Attempts to initialize the buffer strategy if it's not done yet. The buffer
     * strategy optimizes the use of double-buffering or page-flipping.
     * 
     * <p>This method should be called only once the Renderer has been added to a Frame.</p>
     */
    public void initBuffer() throws NullPointerException 
    {
        if (buffer == null) {
            createBufferStrategy(2);
            buffer = getBufferStrategy();
        }      
    } 
    
    
    private GraphicsDevice getScreen(int screenID){
        try {
            initBuffer();
        } catch (NullPointerException npe) {
            System.out.println("No buffer: cannot switch display modes. Have you added your Renderer to a Frame?");
            npe.printStackTrace();
            return null;
        }
        
        GraphicsDevice[] screens = 
            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();        
               
        GraphicsDevice screen = null;        
        if(screenID < screens.length)
            screen = screens[screenID];
        else{
            System.out.println("Cannot find screen# " + screenID);
        }
        return screen;
    }
    
    /*
     * Method only called if the screen is in full screen mode, if parent
     * parent frame exists, and parent frame can change modes.
     */
    public void setScreenToWindow(int screenID)
    {
        if (fullscreen)
        {   // currently in fullscreen mode --> converting to window mode.       
            // somehow the display mode must be set BEFORE setFullScreenWindow
            // is set to null, otherwise there is a DisplayModeChangeUnsupported
            // exception.
            GraphicsDevice screen =  getScreen(screenID);
            if(screen == null){
                //The screen is unavailable or does not exist
                return;
            }
            if ( screen.isDisplayChangeSupported() && changeDisplayMode) 
            {
                screen.setDisplayMode( originalDisplayMode );
            }
            //null argument exits fullscreen mode
            screen.setFullScreenWindow(null);
            fullscreen = false;
            makeVisible(true);
            showMouseCursor();  
        }
    }
    
    /*
     * Set a window to fullscreen if it is not already in fullscreen
	 * and there us no other window fullscreened on that screen
     */
    public void setScreenToFull(int screenID)
    {        
        if (!fullscreen)
        { // currently in window mode --> converting to fullscreen mode
            GraphicsDevice screen =  getScreen(screenID);     
            if(screen == null){
                //The screen is unavailable or does not exist
                return;
            }
            //Check if the screen already has another window in fullscreen mode on it
            if(screen.getFullScreenWindow() != null)
                return;
            
            fullscreen = true; 
            makeVisible(false);
            hideMouseCursor();
            screen.setFullScreenWindow( (Window)this );

            // try to set the display mode to match this Renderer's dimensions
            // checking to see if we can change the resolution of the screen
            if ( screen.isDisplayChangeSupported() && changeDisplayMode ) {
                if ( originalDisplayMode == null ) {
                    originalDisplayMode = screen.getDisplayMode();
				}
                DisplayMode dMode = new DisplayMode( 
                                width, 
                                height, 
                                originalDisplayMode.getBitDepth(),
                                originalDisplayMode.getRefreshRate() );
                try {
                    screen.setDisplayMode( dMode );
                } catch (Exception e) {
                    changeDisplayMode = false;
                    screen.setDisplayMode( originalDisplayMode );
                }               
            }
        }
    }
    
    /**
     * Make the renderer visible.
     * 
     * @param decorated indicates if the window should have a title bar etc.    
     */
    public void makeVisible(boolean decorated)
    {   
        dispose();
        setUndecorated(!decorated);
        pack();
        setVisible(true);
    }
    
    /**
     * @return true if the Renderer is in Fullscreen mode, false otherwise.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }
    
    /**
     * Hides the mouse cursor on this Renderer
     */
    public void hideMouseCursor() {
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(16, 16, pixels, 0, 16));
        Cursor transparentCursor =
                Toolkit.getDefaultToolkit().createCustomCursor
                    (image, new Point(0, 0), "invisiblecursor");        
        setCursor( transparentCursor );     
    }
    
    /**
     * Restores the mouse cursor to the default value on this Renderer.
     */
    public void showMouseCursor() {
        setCursor(null);
    }
}