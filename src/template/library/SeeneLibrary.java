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

package template.library;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import processing.core.*;
import template.library.SeeneObject;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 * @example Hello 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */

public class SeeneLibrary 
{
	
	// myParent is a reference to the parent sketch
	PApplet myParent;

	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * private no-param Constructor mad private to keep anyone from initializing the 
	 * Seene Lib without passing in a pointer to the PApplet object
	 */
	private SeeneLibrary() 
	{
	}	
	
	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public SeeneLibrary(PApplet theParent) 
	{
		myParent = theParent;
		welcome();
	}
	
	
	private void welcome() 
	{
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	public SeeneObject createSeeneObject()
	{
		return new SeeneObject(myParent);
	}
	
	public SeeneObject createSeeneObjectFromURL(String URL)
	{
		return new SeeneObject(myParent,URL);
	}
	public SeeneObject createSeeneObjectFromFile(String oeModelFilePath, String textureFilePath)
	{
		return new SeeneObject(myParent,oeModelFilePath,textureFilePath);
	}
	
	public String sayHello() 
	{
		return "hello library.";
	}
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() 
	{
		return VERSION;
	}

	/**
	 * 
	 * @param theA
	 *          the width of test
	 * @param theB
	 *          the height of test
	 */
	public void setVariable(int theA, int theB) 
	{
		myVariable = theA + theB;
	}

	/**
	 * 
	 * @return int
	 */
	public int getVariable() 
	{
		return myVariable;
	}
	
	
}

