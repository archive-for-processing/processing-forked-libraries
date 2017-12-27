package bontempos.ProjectionMatrix;

import processing.core.*;
import processing.event.MouseEvent;


/**
 * This is a template class and can be used to start a new processing Library.

 *
 * @example Hello 
 */

public class UnprojectedView {

	// processing PApplet class
	static PApplet parent;
	static PGraphics g;
	static int mouseOverTol = 5; 

	//default for mouse 3D picking. Plane can be changed if desired;
	static float ZCenter = 0f; //assuming world, but maybe its better if its the picking 3d obj center
	static PVector planePos;
	static PVector planeRot;
	public static PVector mousePos;

	public static UnprojectedView instance;		

	public UnprojectedView(){}


	public static PVector solve(PApplet p , PVector input) {
		if (instance == null) {
			instance = new UnprojectedView();
		}
		parent = p;
		return UnprojectedView.getProjectedCoord(input);
	}
	
	public static PVector solve(PVector input) {
		if (instance == null) {
			instance = new UnprojectedView();
		}
		if(parent != null){
			return UnprojectedView.getProjectedCoord(input);
		}else{
			System.out.println("UNPROJECTED VIEW ERROR: Parent PApplet is null");
			return null;
		}
	}


	public static void setMouseOverTol(int mouseOverTol) {
		if (instance == null) {
			instance = new UnprojectedView();
		}
		UnprojectedView.mouseOverTol = mouseOverTol;
	}


	public static boolean mouseOver3DPoint(PVector input){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		return isMouseOver3DPoint(input);
	}
	
	public static boolean p2dOver3DPoint(PVector input2d, PVector input3d){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		return is2DPointOver3DPoint(input2d, input3d);
	}

	public static void setWindow3DPicking(PApplet p){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		parent = p;
		//p.registerMethod("mouseEvent", instance);
		ZCenter = 0f; //assuming world, but maybe its better if its the picking 3d obj center
		planePos = new PVector(parent.width/2, parent.height/2, ZCenter ); 
		planeRot = new PVector( 0, 0, 1 );   //z pointing the camera  

	}




	/**
	 * converts 3D coordinate to its correspondent projection in window 2D coord
	 * @param input 3D point in space
	 * @return output 2D point in projection coord.
	 */
	public static PVector getProjectedCoord(PVector input) {
		if (instance == null) {
			instance = new UnprojectedView();
		}
		PMatrix3D mat = getMatrixLocalToWindow(); 
		float [] in = { input.x, input.y, input.z, 1f };
		float [] out = new float[4];
		mat.mult(in, out);
		return new PVector( out[0]/out[3], out[1]/out[3] ); 
	}

	public static PVector getPointOnPlane(PApplet papplet, float screen_x, float screen_y, PVector planePosition, PVector planeDirection){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		parent = papplet;
		return UnprojectedView.getUnProjectedPointOnPlane(screen_x, screen_y, planePosition, planeDirection);
	}


	public static PVector getPointOnDepth(){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		return UnprojectedView.getUnProjectedPointOnPlane(parent.mouseX, parent.mouseY, planePos, planeRot);
	}

	public static PVector getPointOnDepth(PVector objectCenter){
		if (instance == null) {
			instance = new UnprojectedView();
		}
		////TODO dont use planeRot, but a plane aligned to camera plane
		PVector cameraPlaneRot = getCameraPosition().normalize();
		//System.out.println("cameraPlaneRot: " + cameraPlaneRot);
		return UnprojectedView.getUnProjectedPointOnPlane(parent.mouseX, parent.mouseY, objectCenter, cameraPlaneRot);
	}



	/***
	 *  calculates the coordinates on the plane surface corresponding to the screen coordinates
	 *  @param screen_x x position on screen
	 *  @param screen_y y position on screen
	 *  @param planePosition center or ref. point of the plane
	 *  @planeDirection plane normal 
	 *  @return output 2D point coord of given screen coord on a plane
	 */
	static PVector getUnProjectedPointOnPlane(float screen_x, float screen_y, PVector planePosition, PVector planeDirection) {

		PVector f = planePosition.copy(); // Position of the plane
		PVector n = planeDirection.copy(); // The direction of the plane ( normal vector )
		PVector w = unProject(screen_x, screen_y, -1f); // 3 -dimensional coordinate corresponding to a point on the screen
		PVector e = getCameraPosition(); // Viewpoint position
		// Computing the intersection of  
		f.sub(e);
		w.sub(e);
		w.mult( n.dot(f)/n.dot(w) );
		w.add(e);

		return w;
	}



	// Function to get the position of the viewpoint in the current coordinate system
	public static PVector getCameraPosition() {
		PMatrix3D mat = (PMatrix3D)parent.getMatrix(); //Get the model view matrix
		mat.invert();
		return new PVector( mat.m03, mat.m13, mat.m23 );
	}



	//Function to perform the conversion to the local coordinate system ( reverse projection ) from the window coordinate system
	static PVector unProject(float winX, float winY, float winZ) {
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


	//test of 2d screen point is over a 3D point within tolerance (mouseOverTol)
	static boolean is2DPointOver3DPoint(PVector point2d, PVector pointPosition) {
		PVector vp = getProjectedCoord (pointPosition);
		if (vp.x > point2d.x-mouseOverTol && vp.x < point2d.x+mouseOverTol &&
				vp.y > point2d.y-mouseOverTol && vp.y < point2d.y+mouseOverTol) {
			return true;
		}
		return false;
	}


	//test of mouse is over a 3D point within tolerance (mouseOverTol)
	static boolean isMouseOver3DPoint(PVector pointPosition) {
		return is2DPointOver3DPoint( new PVector(parent.mouseX,parent.mouseY), pointPosition);
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


	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();

		switch (event.getAction()) {
		case MouseEvent.PRESS:
			// do something for the mouse being pressed
			break;
		case MouseEvent.RELEASE:
			// do something for mouse released
			break;
		case MouseEvent.CLICK:
			// do something for mouse clicked
			break;
		case MouseEvent.DRAG:
			// do something for mouse dragged
			break;
		case MouseEvent.MOVE:
			//mousePos = getUnProjectedPointOnPlane( x, y, planePos, planeRot );
			break;
		}
	}




}

