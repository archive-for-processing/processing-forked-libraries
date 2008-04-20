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

import net.nexttext.*;
import java.awt.*;

/**
 * Traverses the TextObject hierarchy and draws every object's bounding box.
 */
/* $Id$ */
public class RenderBoundingBoxes implements Page {

	TextObjectGroup root;
	Color boxColor; 
	boolean doGlyphs;
	boolean doGroups;
	
	public RenderBoundingBoxes( TextObjectGroup root, 
								Color boxColor, 
								boolean doGlyphs, 
								boolean doGroups ) {
		
		this.root = root;
		this.boxColor = boxColor;	
		this.doGlyphs = doGlyphs;
		this.doGroups = doGroups;
	}

	public void render( Graphics2D g2, Component c ) {
	  
		TextObjectIterator toi = root.iterator();
		while( toi.hasNext() ) {
			
			TextObject to = toi.next();
			
			if ( to != root ) {
			
				if ( (to instanceof TextObjectGlyph && doGlyphs) ||
				     (to instanceof TextObjectGroup && doGroups) ) {
				    
					g2.setColor( boxColor );				 
				 	g2.draw( to.getBoundingPolygon() );
				}
			}
		}
		
	}
}
