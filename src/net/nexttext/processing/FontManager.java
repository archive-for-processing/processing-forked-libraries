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

package net.nexttext.processing;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import processing.core.PApplet;

/**
 * This class controls access to custom fonts in the 'data' folder of the sketch. The 
 * fonts must be in the TrueType format with extension '.ttf'.
 */
/* $Id$ */
public class FontManager {
    
    static final float DEFAULT_SIZE = 48.0f;
    
    /**
     * Loads the font with the given filename from the 'data' folder of the sketch
     * 
     * @param pApplet the parent PApplet
     * @param filename the font file name
     * 
     * @return the loaded font
     */
    public static Font loadFont(PApplet pApplet, String filename) {
    	try {
    		return Font.createFont(Font.TRUETYPE_FONT, pApplet.openStream(filename)).deriveFont(DEFAULT_SIZE);
    	} catch (IOException ioe) {
    		PApplet.println("ERROR loading font '"+filename+"'! Make sure it is in the 'data' folder of your sketch and that you typed the filename correctly.");
    		ioe.printStackTrace();
    	} catch (FontFormatException ffe) {
    		PApplet.println("ERROR loading font '"+filename+"'! There appears to be something wrong with the font file...");
    		ffe.printStackTrace();
    	}
    	
    	return null;
    }
    
    /**
     * Derives a font with the given size from the given font.
     * 
     * @param font font to derive
     * @param size font size
     *
     * @return the derived font
     */
    public static Font deriveFont(Font font, float size) {
    	return font.deriveFont(size);
    }
    
    /**
     * Derives a font with the supplied attributes from the given font.
     * 
     * @param font font to derive
     * @param size font size
     * @param italic whether the font is italic
     * @param bold whether the font is bold
     *
     * @return the derived font
     */
    public static Font deriveFont(Font font, float size, boolean italic, boolean bold) {
        int style = Font.PLAIN;
        if (bold) 
            style += Font.BOLD;
        if (italic) 
            style += Font.ITALIC;
        
        return font.deriveFont(style, size);
    }
}
