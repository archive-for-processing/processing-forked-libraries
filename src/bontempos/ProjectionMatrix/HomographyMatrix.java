package bontempos.ProjectionMatrix;

import Jama.Matrix;
import processing.core.PMatrix3D;
import processing.core.PVector;

public class HomographyMatrix {

	public static boolean printMatrix = false;
	public static HomographyMatrix instance;	
	
	//homography plane corners
	PVector [] sourceCopy = new PVector[4];
	PVector [] targetCopy = new PVector[4];

	static double[][] homographyMatrix;




	public HomographyMatrix(){	}




	public static double[][] get(PVector[] source, PVector[]target) {
		if (instance == null) {
			instance = new HomographyMatrix();
			return  instance.getHomographyMatrix(source, target);
		}
		return instance.getHomographyMatrix(source, target);
	}


	public static PVector solve(PVector input, double[][] homographyMatrix) {
		if (instance == null) {
			instance = new HomographyMatrix();
			return instance.getHomographyCoord(input, homographyMatrix);
		}
		return instance.getHomographyCoord(input, homographyMatrix);
	}

	public static void printMatrix(boolean b){
		if (instance == null){
			instance = new HomographyMatrix();
			instance.printMatrix = b;
		}else{
			instance.printMatrix = b;
		}
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
	double[][] makeMatrixR(PVector [] vec2d) { //just one row, but using [][] is for convenience.
		int j = 0;
		int ptsNum = vec2d.length;
		double [][] m = new double[ptsNum*2][1]; //this is  u and v from projector (only col 0 is used)

		for (int i = 0; i < ptsNum; i++) {
			m[j][0] =  vec2d[i].x;
			j++;
			m[j][0] =  vec2d[i].y;
			j++;
		}
		return m;
	}





	/**
	 * below is this equation ProjMatrix = ( A.transpose * A ).invert() * ( A.transpose * R )
	 * @param ac Matrix left side
	 * @param rc Matrix right side
	 * @return double[3][3] Homography matrix
	 */
	double[][] makeHomographyMatrix(double[][] ac, double[][]rc) {


		Matrix A = new Matrix(ac);
		Matrix R = new Matrix(rc);

		if(printMatrix){
			A.print(7, 1);
			R.print(7, 1);
		}

		try{
			Matrix tmpA = (A.transpose()).times(A);
			Matrix tmpR = (A.transpose()).times(R);

			//tmpR.print(7,1);

			Matrix result = (tmpA.inverse()).times(tmpR);

			double[][] homographyMat = new double[3][3];

			int col = 0, row = 0;
			for(int i = 0; i<8; i++){

				homographyMat[row][col] = result.get(i, 0);
				col++;
				if(col > 2){
					col = 0;
					row++;
				}
			}

			homographyMat[2][2] = 1d;

			result = new Matrix(homographyMat);

			if(printMatrix){
				result.print(7, 5);
			}

			return homographyMat;
		}catch(RuntimeException e){
			System.out.println("Failed creating Homography plane");
			return null;
		}
	}





	/**
	 * uses the given array of 2D projected points and makes correspondences to
	 * to given 3D points in the given order.
	 * @param source is an array with 2D points 
	 * @param target is an array with 3D points
	 * @return Homography matrix
	 */
	public double[][] getHomographyMatrix( PVector[] source, PVector[]target ) {

		if( source.length == target.length){
			homographyMatrix = makeHomographyMatrix( makeMatrixAHomography(source, target), makeMatrixR(target) );
			System.out.println("R homograph matrix OK");

			return homographyMatrix;
		}

		else{
			return null;
		}
	}


	/**
	 * converts 3D coordinate to its correspondent projection in 2D coord
	 * @param input 3D point in space
	 * @param projMatrix homography matrix to be used
	 * @return output 2D point in projection coord.
	 */
	public PVector getHomographyCoord(PVector input, double[][] homographyMatrix) {
		Matrix hMatrix = new Matrix(homographyMatrix);
		Matrix in = new Matrix( new double[][]{{input.x, input.y, 1f }});

		in = in.transpose();
		hMatrix = hMatrix.times(in);	


		PVector out = new PVector(	(float)hMatrix.get(0, 0), (float)hMatrix.get(1, 0), (float)hMatrix.get(2, 0) );


		return new PVector( out.x/out.z, out.y/out.z );
	}



	/**
	 * updates a 2d point value for homography matrix calibration
	 * @param i the index order of the 2d point being updated
	 * @param p2d the current updating point value
	 * @return outputs the updated homography matrix
	 */
	public double[][] updateHomographyMatrix2d ( int i, PVector p2d) {
		targetCopy[i] = p2d;
		//projMatrix = makeHomographyMatrix( makeMatrixA3D(p2dcopy, p3dcopy), makeMatrixR(p2dcopy) );
		homographyMatrix = getHomographyMatrix( sourceCopy, targetCopy ) ;
		return homographyMatrix;
	}
}
