package bontempos.ProjectionMatrix;

import Jama.*;
import processing.core.*;
//import processing.opengl.*;


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

public class ProjectionMatrix {

	// processing PApplet class
	PApplet parent;

	public final static String VERSION = "##library.prettyVersion##";
	public double zDepth = 1d;
	public int mouseOverTol = 5;

	//projection correspondent points
	PVector [] p2dcopy = new PVector[6];
	PVector [] p3dcopy = new PVector[6];

	//homography plane corners
	PVector [] sourceCopy = new PVector[4];
	PVector [] targetCopy = new PVector[4];

	static PMatrix3D projMatrix;
	static PMatrix3D homographyMatrix;

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent is processing sketch 
	 */
	public ProjectionMatrix(PApplet theParent) {
		parent = theParent;
		welcome();
	}


	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}


	public String sayHello() {
		return "hello library.";
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
	 * @return 3D position of the projection center
	 */
	public static PVector getTranslation() {
		if(projMatrix != null){
			return new PVector(projMatrix.m03,projMatrix.m13,projMatrix.m23);
		}
		else{
			return null;
		}
	}


	/**
	 * builds the left side of the matrix to be computed for 2D and 3D correspondence
	 * @param p2d the points in a plane correspondent to 3D points projection
	 * @param p3d the spatial 3D points
	 */
	double [][] makeMatrixA3D ( PVector[] p2d, PVector[]p3d ) {
		for(int i = 0; i<p2d.length; i++){
			p2dcopy[i] = p2d[i].copy();
			p3dcopy[i] = p3d[i].copy();
		}
		int ptsNum = p2d.length;
		double [] tmpAc = new double[ ptsNum * 11 * 2 ];
		int j = 0;
		for (int i = 0; i < ptsNum; i++) {
			float X, Y, Z, u, v;
			X = p3d[i].x;
			Y = p3d[i].y;
			Z = p3d[i].z;
			u = p2d[i].x;
			v = p2d[i].y;
			//inserting vars for Projector u
			double[] Pu = {X, Y, Z, 1, 0, 0, 0, 0, -X*u, -Y*u, -Z*u};
			for (int k = 0; k < Pu.length; k++) {
				tmpAc[j] = Pu[k];
				j++;
			}
			//inserting vars for Projector v
			double[] Pv = { 0, 0, 0, 0, X, Y, Z, 1, -X*v, -Y*v, -Z*v};
			for (int k = 0; k < Pv.length; k++) { 
				tmpAc[j] = Pv[k];
				j++;
			}
		}
		//formating result maxCpoints*2 rows 11 cols
		int row = 0, col = 0;
		double[][] m = new double[ptsNum*2][11];
		for (int i = 0; i < tmpAc.length; i++) {

			m[row][col] = tmpAc[i];

			col++;
			if (col >= 11) {
				row++;
				col = 0;
			}
		} 
		return m;
	}



	/**
	 * builds the left side of the matrix to be computed for plane homography
	 * @param source the 4 corners of a plane
	 * @param target the 4 corners of a plane 
	 */
	double [][] makeMatrixAHomography( PVector[] source, PVector[] target ) {
		for(int i = 0; i<source.length; i++){
			sourceCopy[i] = source[i].copy();
			targetCopy[i] = target[i].copy();
		}
		int ptsNum = source.length;

		if(ptsNum != target.length) return null;

		double [] tmpAc = new double[ ptsNum * 8 * 2 ];
		int j = 0;
		for (int i = 0; i < ptsNum; i++) {
			float X, Y, u, v;
			X = source[i].x;
			Y = source[i].y;
			u = target[i].x;
			v = target[i].y;
			//inserting vars for Projector u
			double[] Hu = {X, Y, 1, 0, 0, 0, -X*u, -Y*u };
			for (int k = 0; k < Hu.length; k++) {
				tmpAc[j] = Hu[k];
				j++;
			}
			//inserting vars for Projector v
			double[] Hv = { 0, 0, 0, X, Y, 1, -X*v, -Y*v};
			for (int k = 0; k < Hv.length; k++) { 
				tmpAc[j] = Hv[k];
				j++;
			}
		}

		//formating result maxCpoints*2 rows 8 cols
		int row = 0, col = 0;
		double[][] m = new double[ptsNum*2][8];
		for (int i = 0; i < tmpAc.length; i++) {

			m[row][col] = tmpAc[i];

			col++;
			if (col >= 8) {
				row++;
				col = 0;
			}
		} 

		return m;
	}



	/**
	 * builds the right side of the matrix to be computed
	 */
	double[][] makeMatrixR(PVector [] p2d) { //just one row, but using [][] is for convinience.
		int j = 0;
		int ptsNum = p2d.length;
		double [][] m = new double[ptsNum*2][1]; //this is  u and v from projector (only col 0 is used)

		for (int i = 0; i < ptsNum; i++) {
			m[j][0] =  p2d[i].x;
			j++;
			m[j][0] =  p2d[i].y;
			j++;
		}
		return m;
	}



	/**
	 * below is this equation ProjMatrix = ( A.transpose * A ).invert() * ( A.transpose * R )
	 * @param ac Matrix left side
	 * @param rc Matrix right side
	 * @return PMatrix3D projection matrix
	 */
	PMatrix3D makeProjectionMatrix(double[][] ac, double[][]rc) {

		Matrix A = new Matrix(ac);
		Matrix R = new Matrix(rc);
		//A.print(7, 1);
		Matrix tmpA = A.transpose().times(A);
		Matrix tmpR = (A.transpose()).times(R);
		Matrix result = (tmpA.inverse()).solve(tmpR);

		PMatrix3D projMat = new PMatrix3D(
				(float)result.get(0, 0), (float)result.get(1, 0), (float)result.get(2, 0), (float)result.get(3, 0), 
				(float)result.get(4, 0), (float)result.get(5, 0), (float)result.get(6, 0), (float)result.get(7, 0), 
				(float)result.get(8, 0), (float)result.get(9, 0), (float)result.get(10, 0), (float)zDepth, 
				0, 0, 0, 1);

		return projMat;
	}
	
	/**
	 * uses the given array of 2D projected points and makes correspondences to
	 * to given 3D points in the given order.
	 * @param p2d is an array with 2D points 
	 * @param p3d is an array with 3D points
	 * @return PMatrix3D projection matrix
	 */
	public PMatrix3D getProjectionMatrix( PVector[] p2d, PVector[]p3d ) {

		if( p2d.length == p3d.length){
			projMatrix = makeProjectionMatrix( makeMatrixA3D(p2d, p3d), makeMatrixR(p2d) );
			return projMatrix;
		}

		else{
			return null;
		}
	}
	
	
	
	/**
	 * below is this equation ProjMatrix = ( A.transpose * A ).invert() * ( A.transpose * R )
	 * @param ac Matrix left side
	 * @param rc Matrix right side
	 * @return PMatrix3D projection matrix
	 */
	PMatrix3D makeHomographyMatrix(double[][] ac, double[][]rc) {

		Matrix A = new Matrix(ac);
		Matrix R = new Matrix(rc);
		//A.print(7, 1);
		Matrix tmpA = A.transpose().times(A);
		Matrix tmpR = (A.transpose()).times(R);
		Matrix result = (tmpA.inverse()).solve(tmpR);

		PMatrix3D projMat = new PMatrix3D(
				(float)result.get(0, 0), (float)result.get(1, 0), (float)result.get(2, 0), 0, 
				(float)result.get(4, 0), (float)result.get(5, 0), (float)result.get(6, 0), 0, 
				(float)result.get(8, 0), (float)result.get(9, 0), (float)result.get(10, 0), 0, 
				0, 0, 0, 0);

		return projMat;
	}



	
	
	/**
	 * uses the given array of 2D projected points and makes correspondences to
	 * to given 3D points in the given order.
	 * @param source is an array with 2D points 
	 * @param target is an array with 3D points
	 * @return Homography matrix
	 */
	public PMatrix3D getHomographyMatrix( PVector[] source, PVector[]target ) {

		if( source.length == target.length){
			homographyMatrix = makeHomographyMatrix( makeMatrixAHomography(source, target), makeMatrixR(target) );
			return homographyMatrix;
		}

		else{
			return null;
		}
	}
	



	/**
	 * converts 3D coordinate to its correspondent projection in 2D coord
	 * @param input 3D point in space
	 * @param projMatrix projection matrix to be used
	 * @return output 2D point in projection coord.
	 */
	public PVector getProjectedCoord(PVector input, PMatrix3D projMatrix) {
		float [] in = { input.x, input.y, input.z, 1f };
		float [] out = new float[4];
		projMatrix.mult(in, out);
		return new PVector( out[0]/out[3], out[1]/out[3] ); //maybe needs one variation to use out[2]
	}
	
	
	
	
	/**
	 * converts 3D coordinate to its correspondent projection in 2D coord
	 * @param input 3D point in space
	 * @param projMatrix projection matrix to be used
	 * @return output 2D point in projection coord.
	 */
	public PVector getHomographyCoord(PVector input, PMatrix3D homographyMatrix) {
		float [] in = { input.x, input.y, 1f };
		float [] out = new float[3];
		homographyMatrix.mult(in, out);
		return new PVector( out[0]/out[2], out[1]/out[2] );
	}




	/***
	 *  calculates the coordinates on the plane surface corresponding to the screen coordinates
	 *  @param screen_x x position on screen
	 *  @param screen_y y position on screen
	 *  @param planePosition center or ref. point of the plane
	 *  @planeDirection plane normal 
	 *  @return output 2D point coord of given screen coord on a plane
	 */
	public PVector getUnProjectedPointOnPlane(float screen_x, float screen_y, PVector planePosition, PVector planeDirection) {

		PVector f = planePosition.copy(); // Position of the plane
		PVector n = planeDirection.copy(); // The direction of the plane ( normal vector )
		PVector w = unProject(screen_x, screen_y, -1f); // 3 -dimensional coordinate corresponding to a point on the screen
		PVector e = getEyePosition(); // Viewpoint position

		// Computing the intersection of  
		f.sub(e);
		w.sub(e);
		w.mult( n.dot(f)/n.dot(w) );
		w.add(e);

		return w;
	}



	// Function to get the position of the viewpoint in the current coordinate system
	PVector getEyePosition() {
		PMatrix3D mat = (PMatrix3D)parent.getMatrix(); //Get the model view matrix
		mat.invert();
		return new PVector( mat.m03, mat.m13, mat.m23 );
	}



	//Function to perform the conversion to the local coordinate system ( reverse projection ) from the window coordinate system
	PVector unProject(float winX, float winY, float winZ) {
		PMatrix3D mat = getMatrixLocalToWindow();  
		mat.invert();

		float[] in = {winX, winY, winZ, 1.0f};
		float[] out = new float[4];
		mat.mult(in, out);

		if (out[3] == 0 ) {
			return null;
		}

		PVector result = new PVector(out[0]/out[3], out[1]/out[3], out[2]/out[3]);  
		return result;
	}


	//Function to compute the transformation matrix to the window coordinate system from the local coordinate system
	public PMatrix3D getMatrixLocalToWindow() {
		PMatrix3D projection = ((processing.opengl.PGraphics3D)parent.g).projection; 
		PMatrix3D modelview = ((processing.opengl.PGraphics3D)parent.g).modelview; 

		// viewport transf matrix
		PMatrix3D viewport = new PMatrix3D();
		viewport.m00 = viewport.m03 = parent.width/2;
		viewport.m11 = - parent.height/2;
		viewport.m13 =  parent.height/2;

		// Calculate the transformation matrix to the window coordinate system from the local coordinate system
		viewport.apply(projection);
		viewport.apply(modelview);
		return viewport;
	}


	//test of mouse is over a 3D point within tolerance (r)
	public boolean mouseOver3DPoint(PVector pointPosition) {
		//if (!depthEnabled) hint(ENABLE_DEPTH_TEST);

		boolean result = false;
		PVector vp = getProjectedCoord (pointPosition, getMatrixLocalToWindow());
		if (vp.x > parent.mouseX-mouseOverTol && vp.x < parent.mouseX+mouseOverTol &&
				vp.y > parent.mouseY-mouseOverTol && vp.y < parent.mouseY+mouseOverTol) {
			result  = true;
		}
		//if (!depthEnabled) hint(DISABLE_DEPTH_TEST);
		return result;
	}


	/**
	 * returns the transformed position of a point multiplied by its rotation angle
	 * rotates around world center
	 * @param v1 3D point in space
	 * @param up Up mask PVector, ex; 0,1,0;
	 * @param angle in degrees
	 * @return output 3D transformed (oriented) point.
	 */
	public PVector rot(PVector v1, PVector up, float degrees) {
		//angles in radians
		float a = PApplet.radians(degrees);
		PVector result = new PVector(0, 0, 0);

		if (up.x == 1 && up.y == 0 && up.z == 0) {
			PVector [] rx = {
					new PVector(1, 0, 0), 
					new PVector(0, PApplet.cos(a), -PApplet.sin(a)), 
					new PVector(0, PApplet.sin(a), PApplet.cos(a))
			};
			result.x = v1.dot(rx[0]);
			result.y = v1.dot(rx[1]);
			result.z = v1.dot(rx[2]);
		} else if (up.x == 0 && up.y == 1 && up.z == 0) {
			PVector [] ry = {
					new PVector(PApplet.cos(a), 0, PApplet.sin(a)), 
					new PVector(0, 1, 0), 
					new PVector(-PApplet.sin(a), 0, PApplet.cos(a))
			};
			result.x = v1.dot(ry[0]);
			result.y = v1.dot(ry[1]);
			result.z = v1.dot(ry[2]);
		} else if (up.x == 0 && up.y == 0 && up.z == 1) {
			PVector [] rz = {
					new PVector(PApplet.cos(a), -PApplet.sin(a), 0), 
					new PVector(PApplet.sin(a), PApplet.cos(a), 0), 
					new PVector(0, 0, 1)
			};
			result.x = v1.dot(rz[0]);
			result.y = v1.dot(rz[1]);
			result.z = v1.dot(rz[2]);
		}
		return result;
	}



	/**
	 * updates a 2d point value for projection matrix calibration
	 * @param i the index order of the 2d point being updated
	 * @param p2d the current updating point value
	 * @return outputs the updated projection matrix
	 */
	public PMatrix3D updateMatrix2d ( int i, PVector p2d) {
		p2dcopy[i] = p2d;
		projMatrix = makeProjectionMatrix( makeMatrixA3D(p2dcopy, p3dcopy), makeMatrixR(p2dcopy) );
		return projMatrix;
	}

}

