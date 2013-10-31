/**
 * SeeneLib
 * A utility to allow editing and presentation of Seene files
 * http://yourlibraryname.com
 *
 * Copyright (c) 2013 Ben Van Citters http://www.benvancitters.com
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
 * @author      Ben Van Citters http://www.benvancitters.com
 * @modified    10/31/2013
 * @version     1.0.0 (1)
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
	
	public final static String VERSION = "1.0.0";
	

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
		System.out.println("SeeneLib 1.0.0 by Ben Van Citters http://www.benvancitters.com");
	}
	
	public SeeneObject createSeeneObject()
	{
		return new SeeneObject(myParent);
	}
	
	/**
	 * Constructs a 3D mesh from a url string
	 * 
	 * @param URL
	 *          a url to the seene.com website for a particular 3D model
	 */
	public SeeneObject createSeeneObjectFromURL(String URL)
	{
		return new SeeneObject(myParent,URL);
	}
	
	/**
	 * Constructs a 3D mesh from a url string
	 * 
	 * @param oeModelFilePath
	 *          the path to the .oemodel file containing your seene
	 *          
	 * @param textureFilePath
	 *          the path to the texture file for the seene
	 */
	public SeeneObject createSeeneObjectFromFile(String oeModelFilePath, String textureFilePath)
	{
		return new SeeneObject(myParent,oeModelFilePath,textureFilePath);
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



	
	
}

