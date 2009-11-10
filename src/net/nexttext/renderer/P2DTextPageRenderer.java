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

import java.awt.geom.AffineTransform;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import net.nexttext.TextPage;

public class P2DTextPageRenderer extends G2DTextPageRenderer {
	protected PGraphics pg;

    public P2DTextPageRenderer(PApplet p) {
        super(p);
        this.pg = p.createGraphics(p.width, p.height, PConstants.JAVA2D);
        this.g2 = ((PGraphicsJava2D)pg).g2;
    }
    
	@Override
	public void renderPage(TextPage textPage) {
        // When resizing, it's possible to lose the reference to the graphics
        // context, so we skip rendering the frame.
        if (g2 == null) {
            System.out.println(("Skip rendering frame because the graphics context was lost temporarily."));
        }

        else if (textPage.getTextRoot() == null) {
            System.out.println("TextPage: No root specified yet");
        } 
        
        // traverse the TextObject hierarchy
        else {
        	pg.beginDraw();
        	pg.background(0x00ffffff);
        	if (p.g.smooth)
        		pg.smooth();
        	else
        		pg.noSmooth();
        	
            AffineTransform original = g2.getTransform();
            enterCoords(textPage);
            traverse(textPage.getTextRoot());
            exitCoords(textPage);
            g2.setTransform(original);
            
            pg.endDraw();
            p.image(pg, 0, 0);
        }
	}
}
