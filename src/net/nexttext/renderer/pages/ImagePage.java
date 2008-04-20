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
/* $Id$ */
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
