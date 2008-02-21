package net.nexttext.renderer.pages;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.nexttext.Page;

/**
 * An Image drawing page
 */
public class ImagePage implements Page {

	 BufferedImage image;
	 Point location;
	    
	/**
	 * 
	 * @param imagePath - path to the image you want this page to draw
	 */
    public ImagePage(String imagePath){ 
    	this(imagePath, new Point(0,0));
    }
    
    /**
     * 
     * @param imagePath - path to the image you want this page to draw
     * @param location - point where the image will be drawn from (upper left corner)
     */
    public ImagePage(String imagePath, Point location){ 
    	this.location = location;
        File file = new File(imagePath);
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {            
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g, Component c) {
        g.drawImage(image,location.x,location.y,null);        
    }
    
}
