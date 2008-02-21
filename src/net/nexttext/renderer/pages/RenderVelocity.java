//
// Copyright 2004 Jason Lewis
//

package net.nexttext.renderer.pages;

import net.nexttext.*;
import net.nexttext.renderer.*;
import net.nexttext.property.*;
import java.awt.*;

/**
 * Traverses the TextObject hierarchy and draws every object's velocity as a line
 * starting from the object's position.  This callback is useful to debug 
 * behaviours that make use of the Velocity property.
 */

public class RenderVelocity implements Page {

    static final String REVISION = "$CVSHeader: obx/NextText/src/net/nexttext/renderer/java2dcallback/RenderVelocity.java,v 1.1.6.1 2005/04/15 16:32:45 david_bo Exp $";

	TextObjectGroup root;
	Color color; 
	int scale;
	
	/**
	 * Constructs a RenderVelocity callback.  Color is the color that will be 
	 * used to render the vector.   Scale is a scalar that will be used to make
	 * the vector proportionally larger.  This is useful since in many cases
	 * (x,y) values for velocity are often in the range 0~5.
	 */
 	
	public RenderVelocity( TextObjectGroup root, Color color, int scale ) {
	
		this.root = root;
		this.color = color;	
		this.scale = scale;
 	}

	public void render( Graphics2D g2, Component c ) {
		
		TextObjectIterator toi = root.iterator();
		while( toi.hasNext() ) {
			
			TextObject to = toi.next();
			
		 	Vector3Property velProp = (Vector3Property)to.getProperty("Velocity");
		 	
		 	if ( velProp != null ) {
		 	
		 		Vector3 vel = velProp.get();	
		 		Vector3 pos = to.getPositionAbsolute();
		 		
		 		vel.scalar(scale);
		 		
		 		g2.setColor(color);
		 		g2.drawLine( (int)pos.x, (int)pos.y, (int)(pos.x+vel.x), (int)(pos.y+vel.y));
		 	}
		}
		
	}
}
