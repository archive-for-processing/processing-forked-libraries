/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package me.max.chartly;

import processing.core.*;

/**
 * Main class of the Chartly library. Only used for storing the
 * processing applet using the library.
 * 
 * Must be initialized before using the library.
 * 
 * @example Chart_Example
 * 
 * @author Max Johnson
 */
public class Chartly {

	/*
	 * To-Do
	 * Fix buffer logic on axes
	 */
	
	// Processing app using this library.
	public static PApplet app;
	public static Cleaner cleaner;
	
	/**
	 * Constructor for the main Library object
	 * @param app Your project. (In most cases, use the keyword "this")
	 */
	public Chartly(PApplet app) {
		Chartly.app = app;
		Chartly.cleaner = new Cleaner();
		Defaults.create();	
	}
	
	/**
	 * Tests to ensure that Chartly has been given an applet.
	 * @return whether the library is loaded with an applet
	 */
	public static boolean hasApplet() {
		return app != null;
	}

}



