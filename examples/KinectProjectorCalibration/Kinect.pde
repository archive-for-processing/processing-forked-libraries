import org.openkinect.freenect.*; //<>//
import org.openkinect.processing.*;
import java.util.*;

Kinect kinect;

float scaleFactor = 40;
//PVector center;
PMatrix3D kMatrix = new PMatrix3D();
float[] depthLookUp = new float[2048];
float kinectTilt = 0;
boolean tooClose = true;
int [] minMaxDist = {0, 2047};
int skip = 4;


float rawDepthToMeters(int depthValue) {
  if (depthValue < 2047) {
    return (float)(1.0 / ((double)(depthValue) * -0.0030711016 + 3.3309495161));
  }
  return 0.0f;
}


PVector depthToWorld(int x, int y, int depthValue) {
  final double fx_d = 1.0 / 5.9421434211923247e+02;
  final double fy_d = 1.0 / 5.9104053696870778e+02;
  final double cx_d = 3.3930780975300314e+02;
  final double cy_d = 2.4273913761751615e+02;
  PVector result = new PVector();
  double depth =  depthLookUp[depthValue];//rawDepthToMeters(depthValue);
  result.x = (float)((x - cx_d) * depth * fx_d);
  result.y = (float)((y - cy_d) * depth * fy_d);
  result.z = (float)(depth);
  return result;
}


void initKinect() {
  kinect = new Kinect(this);
  kinect.initDepth();
  kinect.enableMirror(false);
  //center = new PVector( width/2, height/2, -100);

  PMatrix3D mat = kMatrix;
  mat.invert();
  println( "->", new PVector( mat.m03, mat.m13, mat.m23 ) );

  kinect.setTilt(kinectTilt);

  for (int i = 0; i < depthLookUp.length; i++) {
    depthLookUp[i] = rawDepthToMeters(i);
  }
}

void buildPointCloud() {
  int[] depth = kinect.getRawDepth();
  int missingPoints = 0;
  ArrayList<Integer>rawDepthPoints = new ArrayList<Integer>();
  for (int x = 0; x < kinect.width; x += skip) {
    for (int y = 0; y < kinect.height; y += skip) {
      int offset = x + y*kinect.width;
      // Convert kinect data to world xyz coordinate
      int rawDepth = depth[offset];
      if (rawDepth > 0 && rawDepth < 2047) {
        rawDepthPoints.add(rawDepth) ;
        PVector p = depthToWorld(x, y, rawDepth);
        p.mult(scaleFactor);
        outputPoint(p, x, y, offset, rawDepth);
      }else{
        missingPoints++; //usually at 0,0,0
      }
    }
  }
 // minMaxDist = updateMinMaxDepth(rawDepthPoints);
  try{
    tooClose = false;
    minMaxDist[0] = Collections.min(rawDepthPoints);
    minMaxDist[1] = Collections.max(rawDepthPoints); 
    //println(" min max ", minMaxDist[0], minMaxDist[1] );
  }catch(Exception e){
    tooClose = true;
  }
  //println("missingPoints",missingPoints);
}

void outputPoint(PVector point, int x, int y, int offset, int depth ) {
  int finalOffset = kinect.width*kinect.height-(kinect.height*skip);
  int[] colorList = new int[]{#ff0000, #ffaa00, #44ff44, #00aaff};
  //int stroke =  setDepthGradient( depth, colorList );//lerpColor(#ff0000, #00aaff, norm(offset, 0, finalOffset)); 
  int stroke = setDepthGradientTime(depth, colorList, 0, 0);
  int weight = (x%64 == 0)?3:1;
  displayCPoint(point, stroke, weight);
}


void drawKMatrix() {
  pushMatrix();
  translate( kMatrix.m03, kMatrix.m13, kMatrix.m23);
  rotateX( getRotation(kMatrix)[0] );
  rotateY( getRotation(kMatrix)[1] );
  rotateZ( getRotation(kMatrix)[2] );  
  noFill();
  stroke(#ffaaaa);
  strokeWeight(1);
  box(10);
  popMatrix();
}


void displayCPoint(PVector p, int stroke, int weight) {
  stroke(stroke);
  strokeWeight(weight);
  pushMatrix();
  applyMatrix(kMatrix);
  translate(p.x, p.y, p.z);
  point(0, 0);
  popMatrix();
}

float[] getRotation(PMatrix3D  m) {
  float ry, rz, rx;
  if (m.m10 > 0.998) { // singularity at north pole
    ry = atan2(m.m02, m.m22);
    rz = HALF_PI;
    rx = 0;
    return new float[]{rx, ry, rz};
  }
  if (m.m10 < -0.998) { // singularity at south pole
    ry = atan2(m.m02, m.m22);
    rz = -HALF_PI;
    rx = 0;
    return new float[]{rx, ry, rz};
  }
  ry = atan2(-m.m20, m.m00);
  rx = atan2(-m.m12, m.m11);
  rz = asin(m.m10);
  return new float[]{rx, ry, rz};
}

PVector getCameraPosition() {
  PMatrix3D mat = (PMatrix3D)g.getMatrix(); //Get the model view matrix
  mat.invert();
  return new PVector( mat.m03, mat.m13, mat.m23 );
}