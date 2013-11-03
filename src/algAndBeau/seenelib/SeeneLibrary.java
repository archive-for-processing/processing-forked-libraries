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

package algAndBeau.seenelib;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import algAndBeau.seenelib.SeeneObject;
import processing.core.*;

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
	
	/**
	 * Constructs a 3D mesh from a url string
	 * 
	 * @example seeneLibrary.createSeeneObjectFromURL("http://seene.co/s/rUaySD");
	 * @param URL
	 *          a url to the seene.com website for a particular 3D model
	 */
	public SeeneObject createSeeneObjectFromURL(String URL)
	{
		return new SeeneObject(myParent,URL);
	}
	
	/**
	 * Constructs a 3D mesh from a texture and a model file
	 * 
	 * @example SeeneObject so = seeneLibrary.createSeeneObjectFromFile("scene.oemodel","poster.jpg");
	 * 
	 * @param oeModelFilePath
	 *          the path to the .oemodel file containing your seene on disk
	 *          
	 * @param textureFilePath
	 *          the path to the texture file for the seene on disk
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

