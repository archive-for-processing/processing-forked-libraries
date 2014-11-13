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

package autodesk.processing.libg;

import processing.core.*;

import autodesk.libg.*;
import autodesk.libg.preloader.*;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class LibG {
	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public LibG(PApplet theParent) {
		myParent = theParent;
		
		System.out.println("About to loadDLL");
		
		try {
			System.load("C:\\Users\\patrick\\git\\Autodesk.Processing.LibG\\lib\\LibGPreloader.dll");
		} catch (UnsatisfiedLinkError error) {
			System.out.println(error.toString());
		}
		
		System.out.println("Loaded a DLL");

		
		//System.load("C:\\Users\\patrick\\git\\Autodesk.Processing.LibG\\lib\\msvcr100.dll");

		//System.load("C:\\Users\\patrick\\git\\Autodesk.Processing.LibG\\lib\\LibGPreloader.dll");
		
		System.out.println("Loaded to DLL");

		
		//autodesk.libg.preloader.AsmPreloader.preload_libg_libraries("C:\\Users\\patrick\\git\\Autodesk.Processing.LibG\\liib");
		//autodesk.libg.preloader.AsmPreloader.preload_asm_libraries("C:\\Program Files\\Autodesk\\Revit MEP 2016");

		//autodesk.libg.LibG.start_asm_library();
		
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

}

