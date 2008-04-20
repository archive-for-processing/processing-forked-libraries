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

package net.nexttext.behaviour.dform;

import net.nexttext.Book;
import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.control.OnMouseDepressed;
import net.nexttext.behaviour.control.Repeat;
import net.nexttext.processing.ProcessingMouse;

/**
 * The factory of DForm behaviours.
 */
/* $Id$ */
public class DFormFactory {
    
    public static AbstractBehaviour pull() {
    	Action pull = new Pull(Book.mouse, 10, 2);
        Action reform = new Reform();
        Behaviour b = new Behaviour(new OnMouseDepressed(ProcessingMouse.BUTTON1, pull, reform));
        b.setDisplayName("Pull");
        
        return b;
    }
     
    public static AbstractBehaviour throb() {         
        Behaviour throb = new Behaviour(new Repeat(new Throb(2, 100), 0));
        throb.setDisplayName("Throb");
        return throb;
    }
    
    public String toString() {
        return "DForm";
    }
}
