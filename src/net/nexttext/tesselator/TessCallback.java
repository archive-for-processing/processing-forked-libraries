package net.nexttext.tesselator;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellatorCallbackAdapter;
import processing.core.PApplet;

/**
 * This tesselator callback uses native Processing drawing functions to 
 * execute the incoming commands.
 */
public class TessCallback extends GLUtessellatorCallbackAdapter {
	private GLU glu;
	
	private ArrayList<Integer> types = new ArrayList<Integer>();
	private ArrayList<Integer> ends = new ArrayList<Integer>();
	private ArrayList<double[]> vertices = new ArrayList<double[]>();

	public TessCallback(GLU glu) {
		this.glu = glu;
	}
	
	public TessData getData() {
		TessData data = new TessData(types, ends, vertices);
		types.clear();
		ends.clear();
		vertices.clear();
		return data;
	}

	public void begin(int type) {
		//keep track of types
		switch (type) {
		case GL.GL_TRIANGLE_FAN: 
			types.add(new Integer(PApplet.TRIANGLE_FAN));
			break;
		case GL.GL_TRIANGLE_STRIP: 
			types.add(new Integer(PApplet.TRIANGLE_STRIP));
			break;
		case GL.GL_TRIANGLES: 
			types.add(new Integer(PApplet.TRIANGLES));
			break;
		}
	}

	public void end() {
		//keep track of where that series of vertices ends
		ends.add(new Integer(vertices.size()));
	}

	public void vertex(Object data) {
		if (data instanceof double[]) {
			double[] d = (double[]) data;
			if (d.length != 3) {
				throw new RuntimeException("TessCallback vertex() data " +
						"isn't length 3");
			}

			vertices.add(d);

		} else {
			throw new RuntimeException("TessCallback vertex() data not understood");
		}
	}

	public void error(int errnum) {
		String estring = glu.gluErrorString(errnum);
		throw new RuntimeException("Tessellation Error: " + estring);
	}

	/**
	 * Implementation of the GLU_TESS_COMBINE callback.
	 * @param coords is the 3-vector of the new vertex
	 * @param data is the vertex data to be combined, up to four elements.
	 * This is useful when mixing colors together or any other
	 * user data that was passed in to gluTessVertex.
	 * @param weight is an array of weights, one for each element of "data"
	 * that should be linearly combined for new values.
	 * @param outData is the set of new values of "data" after being
	 * put back together based on the weights. it's passed back as a
	 * single element Object[] array because that's the closest
	 * that Java gets to a pointer.
	 */
	public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
		double[] vertex = new double[coords.length];
		vertex[0] = coords[0];
		vertex[1] = coords[1];
		vertex[2] = coords[2];

		outData[0] = vertex;
	}
}
