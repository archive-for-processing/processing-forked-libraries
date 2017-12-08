/**
 * Utilities for projection classes.
 *
 * @author       Anderson Sudario
 * @version      1.0
 * 2017
 */

package bontempos.ProjectionMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class ProjectionUtils {

	static PApplet papplet;

	boolean keyShortcuts = false;

	//projection display utils
	boolean screenCrossedCursor = false;
	boolean screenFrame = false;
	int generalLinesColor = 0xFF55FF88;
	int generalLinesWeight = 1;

	ViewProjection parentMat; //TODO works for only one matrix

	//Matrix Data
	private PrintWriter config;

	private BufferedReader reader;


	public ProjectionUtils(PApplet p) {
		papplet = p;
		p.registerMethod("keyEvent", this);
		p.registerMethod("draw", this);
		//p.registerMethod("mouseEvent", this);
		//System.out.println("ProjectionUtils created." );
	}


	/*
	 * @invisible
	 */

	public void keyEvent(KeyEvent e){
		if(keyShortcuts){
			switch(e.getAction()){
			case KeyEvent.PRESS:
				shortcuts(e.getKey());
				System.out.println("->> " + e.getKey());
				break;
			}

		}
	}

	//	public void mouseEvent(MouseEvent event) {
	//		int x = event.getX();
	//		int y = event.getY();
	//
	//		switch (event.getAction()) {
	//		case MouseEvent.PRESS:
	//			// do something for the mouse being pressed
	//			break;
	//		case MouseEvent.RELEASE:
	//			// do something for mouse released
	//			break;
	//		case MouseEvent.CLICK:
	//			// do something for mouse clicked
	//			break;
	//		case MouseEvent.DRAG:
	//			// do something for mouse dragged
	//			break;
	//		case MouseEvent.MOVE:
	//			break;
	//		}
	//	}

	void shortcuts(char c){
		if(c == 'c'){
			screenCrossedCursor =! screenCrossedCursor;
		}
		else if(c == 'f'){
			//screenFrame =! screenFrame;
		}
		else if(c == 't'){
			//JOptionPane.showMessageDialog(null, "It is a DialogBox test");
			//https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
		}
		else if(c == 's'){
			saveMatrix();
		}
		else if(c == 'l'){
			loadMatrix();
		}
	}





	//----------------------------------------------< SETTERS >

	public void setScreenCrossedCursor(boolean b){
		screenCrossedCursor = b;
	}

	public void setScreenFrame(boolean b){
		screenFrame = b;
	}

	public void setKeyShortcuts(boolean b){
		keyShortcuts = b;
	}

	public void setLineColor(int crossedCursorColor){
		this.generalLinesColor = crossedCursorColor;
	}

	public void setLineWeight(int crossedCursorWeight){
		this.generalLinesWeight = crossedCursorWeight;
	}


	//----------------------------------------------< DRAW >

	public void draw(){
		if(screenCrossedCursor) drawScreenCrossedCursor();
		if(screenFrame) drawScreenFrame();
		if(keyShortcuts && papplet.keyPressed) shortcutKeyPress (papplet.key);
	}

	private void shortcutKeyPress(char key) {

		//TODO -> how to select the proper matrix we want to update in scenes where many matrices are placed? popup?
		//for now, this shortcut works only for a single matrix.
		//	adjusting 2D calibration points
		if ( key >= 1 + '0' && key <= 6 + '0') {
			int index = (key - '0') - 1;
			parentMat.p2d[index].set (papplet.mouseX, papplet.mouseY);    
			ViewProjection.update();
		}

	}

	private void drawScreenFrame() {
		drawScreenFrame(generalLinesColor,generalLinesWeight);
	}

	private void drawScreenFrame(int color, int weight) {
		papplet.hint(PConstants.DISABLE_DEPTH_TEST);
		papplet.stroke(color);
		papplet.strokeWeight(weight);
		papplet.noFill();
		papplet.rect(1,1,papplet.width-2,papplet.height-2);
		papplet.hint(PConstants.ENABLE_DEPTH_TEST);
	}

	public void drawScreenCrossedCursor(){
		drawScreenCrossedCursor(generalLinesColor,generalLinesWeight);
	}

	public void drawScreenCrossedCursor(int color, int weight){
		papplet.hint(PConstants.DISABLE_DEPTH_TEST);
		papplet.stroke(color);
		papplet.strokeWeight(weight);
		papplet.line(0,papplet.mouseY,papplet.width,papplet.mouseY);
		papplet.line(papplet.mouseX,0,papplet.mouseX,papplet.height);
		papplet.hint(PConstants.ENABLE_DEPTH_TEST);
	}

	
	//http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToEuler/index.htm
	public float[] getEulerAngles(PMatrix3D  m) {
	  float ry,rz,rx;
	  if (m.m10 > 0.998) { // singularity at north pole
	    ry = (float) Math.atan2(m.m02,m.m22);
	    rz = (float) (Math.PI/2);
	    rx = 0;
	    return new float[]{rx, ry, rz};
	  }
	  if (m.m10 < -0.998) { // singularity at south pole
	    ry = (float) Math.atan2(m.m02,m.m22);
	    rz = -(float) (Math.PI/2);;
	    rx = 0;
	    return new float[]{rx, ry, rz};
	  }
	  ry = (float) Math.atan2(-m.m20,m.m00);
	  rx = (float) Math.atan2(-m.m12,m.m11);
	  rz = (float) Math.asin(m.m10);
	  return new float[]{rx, ry, rz};
	}
	


	/*
	 * *SAVE AND LOAD MATRIX
	 */
	//TODO valid for only one matrix;
	public void saveMatrix(){
		try {
			//ckDirectory:
			File directory = new File(papplet.sketchPath("Matrix"));
			if(!directory.exists()) directory.mkdir();
			File file =  new File( papplet.sketchPath("Matrix/ViewProjmatrix.txt") ) ;
			config = new PrintWriter(file);

			//points on plane 2D (uv);
			for ( int i = 0; i < ViewProjection.calibrationPointsNum; i++) {
				PVector p = parentMat.p2d[i];
				config.print( "uv"+ i + " " + p.x + " " + p.y);
				config.print(";");
			}
			config.println();

			//points on 3D space (3d);
			for ( int i = 0; i < ViewProjection.calibrationPointsNum; i++) {
				PVector p = parentMat.p3d[i];
				config.print( "3d"+ i + " " + p.x + " " + p.y + " " + p.z);
				config.print(";");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally  {
			if ( config != null ) 
			{	
				config.flush();
				config.close();
				System.out.println("Matrix saved");
			}
		}

	}
	//TODO designed only for reading 2 lines (first line for 2D (uv) points and next line for 3D points)
	public void loadMatrix(){

		File file =  new File( papplet.sketchPath("Matrix/ViewProjmatrix.txt") ) ;
		try {
			int lineNum = 0;
			 reader = new BufferedReader( new FileReader(file) );

			for (String next, line = reader.readLine(); line != null; line = next) {

				String[] pieces = line.split(";"); // pieces are ("pointId pointX pointY")
				

				if(lineNum == 0){
					
					if(parentMat != null){
						for ( int i = 0; i < ViewProjection.calibrationPointsNum; i++) {
							parentMat.p2d[i] = new PVector();
							parentMat.p2d[i].x = Float.parseFloat( pieces[i].split(" ")[1] ) ;
							parentMat.p2d[i].y = Float.parseFloat( pieces[i].split(" ")[2] );
						}
					}
				}
				else{
					if(parentMat != null){
						for ( int i = 0; i < ViewProjection.calibrationPointsNum; i++) {
							parentMat.p3d[i] = new PVector();
							parentMat.p3d[i].x = Float.parseFloat( pieces[i].split(" ")[1] ) ;
							parentMat.p3d[i].y = Float.parseFloat( pieces[i].split(" ")[2] );
							parentMat.p3d[i].z = Float.parseFloat( pieces[i].split(" ")[3] );
						}
					}
				}

				lineNum++;
				next = reader.readLine();
			}
			System.out.println("Matrix loaded");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
