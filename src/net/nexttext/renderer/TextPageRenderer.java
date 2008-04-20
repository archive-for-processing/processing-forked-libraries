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

import java.awt.Component;
import java.awt.Graphics2D;

import net.nexttext.TextPage;

/**
 * An interface that represents classes capable of rendering a TextPage
 * to a component.
 *
 */
/* $Id$ */
public interface TextPageRenderer {

    /**
     * In implementing this method do not call getGraphics on 
     * the component passed in as it will cause flickering.
     * 
     * <p>The component is only passed so that information such as bounding 
     * boxes can be determined if necessary. It is expected that many 
     * classes implementing this interface will have no use for the component</p>      
     */
    public void render(TextPage textPage, Graphics2D g, Component c);
    
}
