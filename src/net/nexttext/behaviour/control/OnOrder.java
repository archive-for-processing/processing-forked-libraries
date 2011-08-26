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

package net.nexttext.behaviour.control;


import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;


/**
 * A Condition which is true when a given string is presant in the book ordered and 
 * false when it is not. 
 */
/* $Id: OnOrder.java 22 2011-06-28 12:25:25Z proumf $ */
public class OnOrder extends Condition {
   
	protected String stringOrder;
    protected int side;
    
    /**
     * Creates an OnOrder which performs the given Action when the book glyph are
     * in the requiered order
     *
     * @param the string of the ordered string
     * @param trueAction the Action to perform when the text is ordered
     */
    public OnOrder(String order, Action trueAction) {
        this(order, 1, trueAction, new DoNothing());
    }
    /**
     * Creates an OnOrder which performs the given Action when the book glyph are
     * in the requiered order
     *
     * @param the string of the ordered string
     * @param trueAction the Action to perform when the text is ordered
     * @param falseAction, the Action to perform when the text is not ordered
     */
    public OnOrder(String order, Action trueAction, Action falseAction) {
        this(order, 1, trueAction, falseAction);
    }
    /**
     * Creates an OnOrder which performs the given Action when the book glyph are
     * in the requiered order
     *
     * @param the string of the ordered string
     * @param the side from which the order is taken 0-Left, 1-Right, 2-Top, 3-Bottom
     * @param trueAction the Action to perform when the text is ordered
     */
    public OnOrder(String order,int side, Action trueAction) {
        this(order, side, trueAction, new DoNothing());
    }
    /**
     * Creates an OnOrder which performs the given Action when the book glyph are
     * in the requiered order
     *
     * @param the string of the ordered string
     * @param the side from which the order is taken 0-Left, 1-Right, 2-Top, 3-Bottom
     * @param trueAction the Action to perform when the text is ordered
     * @param falseAction, the Action to perform when the text is not ordered
     */
    public OnOrder(String order,int side, Action trueAction, Action falseAction) {
    	super(trueAction, falseAction);
    	this.stringOrder = order;
        this.side = side;
    }

    /**
     * Checks whether or not the mouse is over the given PApplet.
     * 
     * @param to the TextObject to consider (not used)
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
    	if (to.toString().equals("")|| stringOrder.equals("")) 
        	return false;
    	
    	TextObjectGlyph [] bookOrderArray = to.getBook().getSpatialList().getOrder(side);
    	 String bookOrder = "";
       
    	 for (int i=0; i<bookOrderArray.length; i++){
    		 bookOrder = bookOrder.concat(bookOrderArray[i].toString());
 	     }
	
        if ( bookOrder.contentEquals(stringOrder) ){
        	return true;
        }else{
        	return false;}
//    	return true;
        	}
    	
    	
    }
