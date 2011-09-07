package net.nexttext.renderer.util;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class TessData {
	public int[] types;        //types of tesselated shape
	public int[] ends;         //index of end vertices
	public float[][] vertices; //array of vertices
	public int stroke;         //stroke color
	public int fill;           //fill color

	/** Default constructor. */
	public TessData() {}

	/** Constructor. */
	public TessData(ArrayList<Integer> t, ArrayList<Integer> e, ArrayList<double[]> v) {
		types = new int[t.size()];
		for(int i = 0; i < t.size(); i++)
			types[i] = ((Integer)t.get(i)).intValue();

		ends = new int[e.size()];
		for(int i = 0; i < e.size(); i++)
			ends[i] = ((Integer)e.get(i)).intValue();

		vertices = new float[v.size()][3];
		for(int i = 0; i < v.size(); i++) {
			double[] d = (double[])v.get(i);
			vertices[i][0] = (float)d[0];
			vertices[i][1] = (float)d[1];
			vertices[i][2] = (float)d[2];
		}
	}

	/** Clone. */
	public TessData clone() {
		TessData clone = new TessData();
		clone.types = new int[this.types.length];
		for(int i = 0; i < clone.types.length; i++)
			clone.types[i] = this.types[i];

		clone.ends = new int[this.ends.length];
		for(int i = 0; i < clone.ends.length; i++)
			clone.ends[i] = this.ends[i];

		clone.vertices = new float[this.vertices.length][3];
		for(int i = 0; i < clone.vertices.length; i++) {
			clone.vertices[i][0] = this.vertices[i][0];   
			clone.vertices[i][1] = this.vertices[i][1];   
			clone.vertices[i][2] = this.vertices[i][2];   
		}

		clone.stroke = this.stroke;
		clone.fill = this.fill;

		return clone;
	}

	/** Translate. */
	public void translate(PVector offset) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i][0] += offset.x;
			vertices[i][1] += offset.y;
			vertices[i][2] += offset.z;
		}
	}
	
	public void draw(PApplet p) {
		//p.stroke(stroke);
		//p.noStroke();
		//p.noFill();
		//p.fill(fill);
		for (int j = 0; j < types.length; j++) {
			p.beginShape(types[j]);
			// go through vertices
			for (int k = j==0?0:ends[j-1]; k < ends[j]; k++) {
				p.vertex(vertices[k][0], vertices[k][1], vertices[k][2]);
			}
			p.endShape();  
		}
	}
}
