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

public class Chartly {

	// Processing app using this library.
	public static PApplet app;
	public static Cleaner cleaner;
	/**
	 * --- TODO ---
	 * rename variables
	 * Getters and setters
	 * Looks comments (rename?)
	 * Change relative dimensions?
	 * Refactor Constants?
	 */
	
	/**
	 * Constructor for the main Library object
	 * @param app Your project. (In most cases, use the keyword "this")
	 */
	public Chartly(PApplet app) {
		Chartly.app = app;
		Chartly.cleaner = new Cleaner();
		Defaults.create();	
	}

}


