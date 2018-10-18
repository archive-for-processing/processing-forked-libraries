package stlr.library;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import processing.core.*;

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

public class STLr implements PConstants{
	
	// myParent is a reference to the parent sketch
	PApplet parent;
	
	public final static String VERSION = "1.0";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public STLr(PApplet theParent) {
		parent = theParent;
	}
	
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
	
	public String toSTLasciiformat(PShape obj, String name)
	{
		PShape tess = obj.getTessellation();
		PVector vertex = new PVector();
		String asciiform = "solid " + name + "\n";
		for(int i = 0; i < tess.getVertexCount(); i += 3) {
			asciiform += "facet normal 0 0 0\n";
			asciiform += "\touter loop\n";
			for(int j = 0; j < 3; j++) {
				tess.getVertex(i + j, vertex);
				asciiform += "\t\tvertex " + vertex.x +
						" " + vertex.y + " " + vertex.z + "\n";
			}
			asciiform += "\tendloop\n";
			asciiform += "endfacet\n";
		}
		asciiform += "endsolid " + name;
		return asciiform;
	}
	
	public void generateAsciiSTL(PShape obj, String name)
	{
		try(OutputStreamWriter out = new OutputStreamWriter(
				PApplet.createOutput(parent.sketchFile(name + ".stl")))) {
			out.write(toSTLasciiformat(obj, name));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

