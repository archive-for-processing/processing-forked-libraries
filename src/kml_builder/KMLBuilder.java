package kml_builder;

import processing.core.*;
import processing.data.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class KMLBuilder {
	
	// myParent is a reference to the parent sketch
	static PApplet Parent;

	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @param theParent
	 */
	public KMLBuilder(PApplet Parent) {
		KMLBuilder.Parent = Parent;
	}
	
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
	
	/**
	 * Overrides saveXML function to facilitate saving KML objects
	 *
	 * @param KMLRoot SaveKML KML object to be saved
	 * @return true if the file was saved successfully
	 */
	public static boolean saveXML(KMLRoot saveKML) {
	  XML out = saveKML.getRootNode();
	  String fileName = saveKML.getFileName();
	  return(Parent.saveXML(out, fileName));
	}

}

