//
// Copyright 2004 Jason Lewis
//

package net.nexttext.renderer.pages;

import net.nexttext.*;
import net.nexttext.renderer.*;
import java.awt.*;

/**
 * Traverses the TextObject hierarchy and draws every object's bounding box.
 */

public class RenderBoundingBoxes implements Page {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/renderer/java2dcallback/RenderBoundingBoxes.java,v 1.2 2005/04/04 13:22:55 dissent Exp $";

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
