package stlr.library;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

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
	
	public String toSTLasciiformat(PShape obj, String name) {
		PShape tess = obj.getTessellation();
		String asciiform = "solid " + name + "\n";
		for(int i = 0; i < tess.getVertexCount(); i += 3) {
			asciiform += triangle(tess.getVertex(i), tess.getVertex(i + 1), tess.getVertex(i + 2));
		}
		asciiform += "endsolid " + name;
		return asciiform;
	}
	
	private PVector normal(PVector v1, PVector v2, PVector v3) {
		PVector d1 = PVector.sub(v1, v2);
		PVector d2 = PVector.sub(v2, v3);
		PVector norm = new PVector();
		PVector.cross(d1, d2, norm);
		norm.normalize();
		return norm;
	}
	
	private String triangle(PVector v1, PVector v2, PVector v3) {
		PVector norm = normal(v1, v2, v3);
		String triangle = "facet normal ";
		triangle += norm.x + " " + norm.y + " " + norm.z + "\n";
		triangle += "\touter loop\n";
		triangle += "\t\tvertex " + v1.x + " " + v1.y + " " + v1.z + "\n";
		triangle += "\t\tvertex " + v2.x + " " + v2.y + " " + v2.z + "\n";
		triangle += "\t\tvertex " + v3.x + " " + v3.y + " " + v3.z + "\n";
		triangle +=  "\tendloop\nendfacet\n";
		return triangle;
	}
	
	public void generateBinarySTL(PShape obj, String name) {
		
		//TODO: I think this fails due to the first octant rule (all positive numbers)
		
		PShape tess = obj.getTessellation();
		try(OutputStream out = PApplet.createOutput(parent.sketchFile(name + ".stl"))) {
			out.write(new byte[80]); //Empty 80 byte header
			//Put triangle count
			out.write(ByteBuffer.allocate(4).putInt(tess.getVertexCount()/3).array());
			for(int i = 0; i < tess.getVertexCount(); i += 3) {
				PVector v1 = tess.getVertex(i);
				PVector v2 = tess.getVertex(i + 1);
				PVector v3 = tess.getVertex(i + 2);
				PVector norm = normal(v1, v2, v3);
				out.write(ftbs(norm.x)); out.write(ftbs(norm.y)); out.write(ftbs(norm.z));
				out.write(ftbs(v1.x)); out.write(ftbs(v1.y)); out.write(ftbs(v1.z));
				out.write(ftbs(v2.x)); out.write(ftbs(v2.y)); out.write(ftbs(v2.z));
				out.write(ftbs(v3.x)); out.write(ftbs(v3.y)); out.write(ftbs(v3.z));
				out.write(new byte[2]); //Empty two bytes to finish it off
			}
		} catch(IOException e) {
			e.printStackTrace();
		};
	}
	
	public byte[] ftbs(float f) {
		return ByteBuffer.allocate(4).putFloat(f).array();
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

