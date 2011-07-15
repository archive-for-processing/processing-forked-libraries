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

import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.standard.DoNothing;
import java.awt.Polygon;
import java.lang.Math.*;
import processing.core.PVector;
import net.nexttext.input.Mouse;

/**
 * A Condition which is true when the mouse is at proximity of the TextObject and 
 * false when it is not. 
 */
/* $Id: OnMouseOver.java 22 2011-07-15 12:25:25Z proumf $ */
public class OnMouseOnProxi extends Condition {
    
    private Mouse mouse;
    private double distance;
    
    /**
     * Creates an OnMouseOver which performs the given Action when the mouse
     * is close to the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is over the TextObject
     * @param distance default  300 
     */
    public OnMouseOnProxi(Action trueAction) {
        this(trueAction, new DoNothing(),300);
    }
    /**
     * Creates an OnMouseOnProxi which performs one of the given Actions, depending
     * on whether or not the mouse is close the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is close the TextObject
     * @param distance from mouse affected
     */
    
    public OnMouseOnProxi(Action trueAction, double distance) {
    	this(trueAction, new DoNothing(),distance);
        
    }
    /**
     * Creates an OnMouseOnProxi which performs one of the given Actions, depending
     * on whether or not the mouse is close the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is close the TextObject
     * @param falseAction the Action to perform when the mouse is far the TextObject
     */
    
    public OnMouseOnProxi(Action trueAction, Action falseAction) {
        this(trueAction, falseAction ,300);
        
    }
    /**
     * Creates an OnMouseOnProxi which performs one of the given Actions, depending
     * on whether or not the mouse is close the TextObject.
     *
     * @param trueAction the Action to perform when the mouse is close the TextObject
     * @param falseAction the Action to perform when the mouse is far the TextObject
     * @param distance from mouse affected
     */
    
    public OnMouseOnProxi(Action trueAction, Action falseAction, double distance) {
    	super(trueAction, falseAction);
        this.mouse = Book.mouse;
        this.distance = distance;
        
    }

    /**
     * Checks whether or not the mouse is close to the given TextObject.
     * by creating a java polygone(octogone)
     * @param to the TextObject to consider
     * 
     * @return the outcome of the condition
     */
    public boolean condition(TextObject to) {
        
    	double sqrtDoubleTemp = (Math.sqrt((distance*distance)/2));
        
        int distanceSquareRoot = (int)sqrtDoubleTemp;
        PVector center = to.getCenter();
        float[] tempCenterArr = center.array();
        int[] centerArr = {(int)tempCenterArr[0],(int)tempCenterArr[1]};
        //creating a octogone around each text object
        Polygon glyphArea = new Polygon();
        glyphArea.addPoint(centerArr[0],centerArr[1]+(int)distance);
        glyphArea.addPoint(centerArr[0]+distanceSquareRoot,centerArr[1]+distanceSquareRoot);
        glyphArea.addPoint(centerArr[0]+(int)distance,centerArr[1]);
        glyphArea.addPoint(centerArr[0]+distanceSquareRoot,centerArr[1]-distanceSquareRoot);
        glyphArea.addPoint(centerArr[0],centerArr[1]-(int)distance);
        glyphArea.addPoint(centerArr[0]-distanceSquareRoot,centerArr[1]-distanceSquareRoot);
        glyphArea.addPoint(centerArr[0]-(int)distance,centerArr[1]);
        glyphArea.addPoint(centerArr[0]-distanceSquareRoot,centerArr[1]+distanceSquareRoot);
        return glyphArea.contains(mouse.getX(), mouse.getY());
    }
}
