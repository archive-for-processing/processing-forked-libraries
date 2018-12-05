package stlr.library;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

import processing.core.*;

/**
 * The STLr class contains functionality to generate STL files from PShape objects.
 *
 * @example Hello 
 */

public class STLr implements PConstants{
	
	
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
	
	
	/**
	 * Writes the supplied shape into an ascii STL file 
	 * @param obj A 3D solid PShape object
	 * @param name The name for the file
	 */
	public void generateAsciiSTL(PShape obj, String name)
	{
		try(OutputStreamWriter out = new OutputStreamWriter(
				PApplet.createOutput(parent.sketchFile(name + ".stl")))) {
			out.write(toSTLasciiformat(obj, name));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a string that is the ascii representation of an STL model
	 * @param obj The object to write
	 * @param name A name for the object
	 * @return String
	 */
	public String toSTLasciiformat(PShape obj, String name) {
		PShape tess = obj.getTessellation();
		String asciiform = "solid " + name + "\n";
		for(int i = 0; i < tess.getVertexCount(); i += 3) {
			asciiform += facet(tess.getVertex(i), tess.getVertex(i + 1), tess.getVertex(i + 2));
		}
		asciiform += "endsolid " + name;
		return asciiform;
	}
	
	
	/**
	 * Calculates surface normal for triangle with vertices assumed to be clockwise.
	 * @param v1 First vertex
	 * @param v2 Second vertex
	 * @param v3 Third vertex
	 * @return PVector
	 */
	private PVector normal(PVector v1, PVector v2, PVector v3) {
		PVector d1 = PVector.sub(v1, v2);
		PVector d2 = PVector.sub(v2, v3);
		PVector norm = new PVector();
		PVector.cross(d1, d2, norm);
		norm.normalize();
		return norm;
	}
	
	
	/**
	 * Get the string that represents a facet in STL format for a triangle.
	 * @param v1 First vertex
	 * @param v2 Second vertex
	 * @param v3 Third vertex
	 * @return String representing the facet
	 */
	private String facet(PVector v1, PVector v2, PVector v3) {
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
	
	
	/**
	 * Generates a binary STL file
	 * @param obj The shape to be converted
	 * @param name The name of the file to be written
	 */
	public void generateBinarySTL(PShape obj, String name) {
		
		//TODO: I think this fails due to the first octant rule (all positive numbers)
		PVector lowest = minVertex(obj);
		PShape tess = obj.getTessellation();
		try(OutputStream out = PApplet.createOutput(parent.sketchFile(name + ".stl"))) {
			out.write(new byte[80]); //Empty 80 byte header
			//Put triangle count
			out.write(ByteBuffer.allocate(4).putInt(tess.getVertexCount()/3).array());
			for(int i = 0; i < tess.getVertexCount(); i += 3) {
				PVector v1 = tess.getVertex(i).add(lowest);
				PVector v2 = tess.getVertex(i + 1).add(lowest);
				PVector v3 = tess.getVertex(i + 2).add(lowest);
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
	
	/**
	 * Creates a PVector containing the minimum x, y, and z values found in the shape
	 * @param obj A PShape object
	 * @return PVector
	 */
	public PVector minVertex(PShape obj) {
		PVector l = new PVector(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		for(int i = 0; i < obj.getVertexCount(); i++) {
			PVector v = obj.getVertex(i);
			if(v.x < l.x)
				l.x = v.x;
			if(v.y < l.y)
				l.y = v.y;
			if(v.z < l.z)
				l.z = v.z;
		}
		return l;
	}
	
	
	/**
	 * Helper function to break a float into four bytes
	 * @param f Floating point number
	 * @return byte[]
	 */
	private byte[] ftbs(float f) {
		return ByteBuffer.allocate(4).putFloat(f).array();
	}
	
	
	/**
	 * 
	 * @param curve A PShape with only cubic-spline vertices
	 * @param radius The radius of the tube
	 * @param lGran The level of granularity on the curve length
	 * @param circleGran Detail of the circle
	 * @return A PShape containing the tube structure
	 * 
	 */
	public PShape noodlize(PShape curve, float radius, int lGran, int circleGran) {
		if(curve.getVertexCount() < 4)
			return null;
		PVector[][] guide = controlSystem(controlPath(curve, lGran));
		PVector[] ring1 = new PVector[circleGran];
		PVector[] ring2 = new PVector[circleGran];
		PVector[] temp;
		PVector x, y, r1a, r1b, r2a, r2b;
		//Initialize rings
		for(int i = 0; i < circleGran; i++) {
			x = PVector.mult(guide[2][0], radius*PApplet.cos(TWO_PI*i/circleGran));
			y = PVector.mult(guide[3][0], radius*PApplet.sin(TWO_PI*i/circleGran));
			ring1[i] = PVector.add(guide[0][0], x).add(y);
			ring2[i] = new PVector();
		}
		
		//Setup shape
		PShape noodle = parent.createShape();
		noodle.beginShape(TRIANGLES);
		noodle.stroke(255, 0, 0, 128);
		noodle.fill(255, 128);
		
		//First cap
		PVector c = guide[0][0];
		for(int i = 0; i < circleGran; i++) {
			PVector a = ring1[i];
			PVector b = ring1[(i + 1) % circleGran];
			noodle.vertex(c.x, c.y, c.z);
			noodle.vertex(a.x, a.y, a.z);
			noodle.vertex(b.x, b.y, b.z);
		}
		
		for(int i = 1; i < guide[0].length; i++) {
			//Swap rings
			temp = ring1;
			ring1 = ring2;
			ring2 = temp;
			//Fill ring1
			for(int j = 0; j < circleGran; j++) {
				x = PVector.mult(guide[2][i], radius*PApplet.cos(TWO_PI*j/circleGran));
				y = PVector.mult(guide[3][i], radius*PApplet.sin(TWO_PI*j/circleGran));
				ring1[j].set(guide[0][i]).add(x).add(y);
			}
			for(int j = 0; j < circleGran; j++) {
				r1a = ring1[j];
				r1b = ring1[(j + 1) % circleGran];
				r2a = ring2[j];
				r2b = ring2[(j + 1) % circleGran];
				//Triangle r1a-r1b-r2a
				noodle.vertex(r1a.x, r1a.y, r1a.z);
				noodle.vertex(r1b.x, r1b.y, r1b.z);
				noodle.vertex(r2a.x, r2a.y, r2a.z);
				//Triangle r2b-r2a-r1b
				noodle.vertex(r2b.x, r2b.y, r2b.z);
				noodle.vertex(r2a.x, r2a.y, r2a.z);
				noodle.vertex(r1b.x, r1b.y, r1b.z);
			}
		}
		
		//Second cap
		c = guide[0][guide[0].length - 1];
		for(int i = 0; i < circleGran; i++) {
			PVector a = ring1[i];
			PVector b = ring1[(i + 1) % circleGran];
			noodle.vertex(c.x, c.y, c.z);
			noodle.vertex(b.x, b.y, b.z);
			noodle.vertex(a.x, a.y, a.z);
		}
		
		noodle.endShape();
		return noodle;
	}
	
	
	/**
	 * Generates a set of points along the cubic spline
	 * @param curve A PShape object assumed to contain vertices of a cubic spline
	 * @param lGran The number of points to place
	 * @return PVector[]
	 */
	public PVector[] controlPath(PShape curve, int lGran) {
		PVector[] points = new PVector[(curve.getVertexCount() - 3)*lGran + 1];
		PVector p0, p1, p2, p3;
		p1 = curve.getVertex(0);
		p2 = curve.getVertex(1);
		p3 = curve.getVertex(2);
		p0 = new PVector();
		int i;
		for(i = 3; i < curve.getVertexCodeCount(); i++) {
			p0.set(p1);
			p1.set(p2);
			p2.set(p3);
			curve.getVertex(i, p3);
			for(int j = 0; j < lGran; j++) {
				points[(i - 3)*lGran + j] = catmullRom(p0, p1, p2, p3, (float)j/lGran);
			}
		} 
		//Last point is the last non-control point
		points[points.length - 1] = curve.getVertex(i - 2);
		return points;
	}
	
	
	/**
	 * Generates a set of coordinate systems for a set of point along a curve
	 * @param cp The control path for the system
	 * @return PVector[][]
	 */
	public PVector[][] controlSystem(PVector[] cp) {
		//0 - position, 1 - tangent, 2 - binormal, 3 - normal
		PVector[][] cs = new PVector[4][cp.length];
		PVector[] tan = new PVector[cp.length - 1];
		//Clone positions
		cs[0] = cp;
		//Create temporary tangents
		for(int i = 0; i < tan.length; i++) {
			tan[i] = PVector.sub(cp[i], cp[i + 1]).normalize();
		}
		//Derive binormal from tangents
		for(int i = 1; i < cp.length - 1; i++) {
			cs[2][i] = tan[i - 1].cross(tan[i]).normalize();
		}
		cs[2][0] = cs[2][1]; //Symmetric edge cases
		cs[2][cs[0].length - 1] = cs[2][cs[0].length - 2];
		//Use averaged tangents to preserve symmetry
		//(reversed cp array should give same results)
		for(int i = 1; i < cp.length - 1; i++) {
			cs[1][i] = PVector.add(tan[i - 1], tan[i]).mult(0.5f);
		}
		cs[1][0] = tan[0]; //Edge cases
		cs[1][cs[0].length - 1] = tan[tan.length - 1];
		//Normals
		for(int i = 0; i < cp.length; i++) {
			cs[3][i] = cs[1][i].cross(cs[2][i]).normalize();
		}
		return cs;
	}
	
	
	/**
	 * Determines the length approximately of a cubic spline curve
	 * @param curve A PShape object assumed to contain a cubic spline
	 * @param grain	The level of detail to sample points at
	 * @return float
	 */
	public float length(PShape curve, int grain) {
		if(curve.getVertexCodeCount() < 4)
			return 0;
		float len = 0;
		PVector p0, p1, p2, p3, previous, next;
		p0 = new PVector();
		p1 = curve.getVertex(0);
		p2 = curve.getVertex(1);
		p3 = curve.getVertex(2);
		previous = new PVector();
		next = new PVector();
		int i;
		for(i = 3; i < curve.getVertexCodeCount(); i++) {
			p0.set(p1);
			p1.set(p2);
			p2.set(p3);
			curve.getVertex(i, p3);
			previous.set(p1);
			for(int j = 1; j < grain; j++) {
				next.set(catmullRom(p0, p1, p2, p3, (float)j/grain));
				len += next.dist(previous);
				previous.set(next);
			}
		}
		next.set(curve.getVertex(i - 1));
		len += next.dist(previous);
		return len;
	}
	
	
	/**
	 * Uses a parametric equation for a catmull-rom spline to place a point
	 * @param p0 First control point
	 * @param p1 Start point
	 * @param p2 End point
	 * @param p3 Second control point
	 * @param t Interpolation between start and end (0-Start 1-End)
	 * @return PVector
	 */
	public PVector catmullRom(PVector p0, PVector p1, PVector p2, PVector p3, float t) {
		PVector tan1 = PVector.sub(p2, p0).mult(0.5f);
		PVector tan2 = PVector.sub(p3, p1).mult(0.5f);
		PVector interp = PVector.mult(tan1, t*(1 - t)*(1 - t));
		interp.add(PVector.mult(tan2, t*t*(t - 1)));
		interp.add(PVector.mult(p1, (1 + 2*t)*(1 - t)*(1 - t)));
		interp.add(PVector.mult(p2, t*t*(3 - 2*t)));
		return interp;
	}
}

