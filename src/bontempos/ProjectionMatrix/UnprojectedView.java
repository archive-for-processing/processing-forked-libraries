package bontempos.ProjectionMatrix;

import processing.core.*;


/**
 * This is a template class and can be used to start a new processing Library.

 *
 * @example Hello 
 */

public class UnprojectedView {

	// processing PApplet class
	static PApplet parent;
	static int mouseOverTol = 5; 

	public static UnprojectedView instance;		

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent is processing sketch 
	 */
	public UnprojectedView(){}


	public static PVector solve(PVector input) {
		if (instance == null) {
			instance = new UnprojectedView();
			return instance.getProjectedCoord(input,getMatrixLocalToWindow());
		}
		return instance.getProjectedCoord(input, getMatrixLocalToWindow());
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
	static PMatrix3D getMatrixLocalToWindow() {
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


	
	//test of mouse is over a 3D point within tolerance (mouseOverTol)
	public boolean mouseOver3DPoint(PVector pointPosition) {

		boolean result = false;
		PVector vp = getProjectedCoord (pointPosition, getMatrixLocalToWindow());
		if (vp.x > parent.mouseX-mouseOverTol && vp.x < parent.mouseX+mouseOverTol &&
				vp.y > parent.mouseY-mouseOverTol && vp.y < parent.mouseY+mouseOverTol) {
			result  = true;
		}
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







}

