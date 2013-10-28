import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

import java.io.FileInputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.io.InputStream;import java.net.URL;
ArrayList<float[]> verts = new ArrayList<float[]>();
PShape seeneImg;

void setup()
{
  size(500,500,P3D);
  try
  {
    InputStream fin = null;
      fin = new URL("https://camera-storage.s3.amazonaws.com/uploads/scene/model/f3309098-4d40-459b-bb85-38d5ed056689/scene.oemodel").openStream();
//    FileInputStream fin = new FileInputStream("/Users/admin/processing-library-template/seene/data/scene (4).oemodel");
    println("fin.available(): " + fin.available());
    DataInputStream in = new DataInputStream(fin);


/*
var n = {version: z(e, t),
         camera_width: z(e, t + 4),
         camera_height: z(e, t + 8),
         camera_fx: W(e, t + 12),
         camera_fy: W(e, t + 16),
         camera_k1: W(e, t + 20),
         camera_k2: W(e, t + 24),
         depthmap_width: z(e, t + 28),
         depthmap_height: z(e, t + 32),
         header_bytes: 36}
*/
    //0 offset
    int version = Integer.reverseBytes(in.readInt());
    println("version: " + version); //should be '2'
    //4 offset should be something like 720
    int cameraWidth = Integer.reverseBytes(in.readInt());
    println("cameraWidth: " + cameraWidth);
    
    //8 should be something like 720
    int cameraHeight = Integer.reverseBytes(in.readInt());
    println("cameraHeight: " + cameraHeight);
    
    //at byte 12 should be something like 1252.39842
    float cameraFX = getFloatAtCurPos(in);
    println("cameraFX: " + cameraFX);
    
    //at byte 16 should be something like 1247.39842
    float cameraFY = getFloatAtCurPos(in);
    println("cameraFy: " + cameraFY);
    
    //at byte 20 should be something like 0.023
    float cameraK1 = getFloatAtCurPos(in);
    println("cameraK1: " + cameraK1);
    
    //at byte 20 should be something like .3207...
    float cameraK2 = getFloatAtCurPos(in);
    println("cameraK2: " + cameraK2);
    
    //at byte 28 ~~90
    int depthmapwidth = Integer.reverseBytes(in.readInt());
    println("depthmapwidth: " + depthmapwidth);
    //at byte 32 ~~90
    int depthmapheight = Integer.reverseBytes(in.readInt());
    println("depthmapheight: " + depthmapheight);
    
    int floatCount = depthmapheight* depthmapwidth;
    float fOut[] = new float[floatCount];
        for(int i = 0; i<fOut.length;i++)
    {
     fOut[i] =  getFloatAtCurPos(in);
//     print(",[" +i + "]: " + fOut[i]);
    }
    
    float D = cameraFX / cameraWidth;
    float P = cameraFY / cameraHeight;
    println("D/P: " + D +""+ P);
    PImage tex = loadImage("https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/poster.jpg");
    textureMode(NORMAL);
    seeneImg = createShape();
    
    seeneImg.beginShape(TRIANGLES);
    seeneImg.texture(tex);
    seeneImg.noStroke();
    
    for(int j = 0; j < depthmapheight-1; j++)
    {
      for(int i = 0; i < depthmapwidth-1;i++)  
      {
        float depthVal = fOut[j * depthmapwidth + i];
        PVector upperLeft = new PVector(-depthVal * ((i + .5) / depthmapwidth - .5) / D, 
                                        -depthVal * ((j + .5) / depthmapheight - .5) / P, 
                                        -(depthVal - 1));
        PVector upperLeftUV = new PVector((i + .5) / depthmapwidth,
                                          (j + .5) / depthmapheight);
                                          
        depthVal = fOut[j * depthmapwidth + i+1];
        PVector upperRight = new PVector(-depthVal * ((i+1 + .5) / depthmapwidth - .5) / D, 
                                        -depthVal * ((j + .5) / depthmapheight - .5) / P, 
                                        -(depthVal - 1));
        PVector upperRightUV = new PVector((i+1 + .5) / depthmapwidth,
                                          (j + .5) / depthmapheight);                                     
                                          
        depthVal = fOut[(j+1) * depthmapwidth + i];
        PVector lowerLeft = new PVector(-depthVal * ((i + .5) / depthmapwidth - .5) / D, 
                                        -depthVal * ((j+1 + .5) / depthmapheight - .5) / P, 
                                        -(depthVal - 1));
        PVector lowerLeftUV = new PVector((i + .5) / depthmapwidth,
                                           (j+1 + .5) / depthmapheight);

        depthVal = fOut[(j+1) * depthmapwidth + i+1];
         PVector lowerRight = new PVector(-depthVal * ((i+1 + .5) / depthmapwidth - .5) / D, 
                                          -depthVal * ((j+1 + .5) / depthmapheight - .5) / P, 
                                          -(depthVal - 1));
        PVector lowerRightUV = new PVector((i+1 + .5) / depthmapwidth,
                                           (j+1 + .5) / depthmapheight);
                                           
        PVector tri1Norm = PVector.sub(lowerLeft,upperLeft).cross( PVector.sub(upperRight,upperLeft));
        tri1Norm.normalize();
        PVector tri2Norm = PVector.sub(upperRight,lowerRight).cross( PVector.sub(lowerLeft,lowerRight));
        tri2Norm.normalize();
        
        seeneImg.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);
        seeneImg.vertex(upperLeft.x, upperLeft.y, upperLeft.z
                        ,upperLeftUV.x,upperLeftUV.y
                        );
        seeneImg.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);                          
        seeneImg.vertex(upperRight.x, upperRight.y, upperRight.z
                        ,upperRightUV.x,upperRightUV.y
                        );
                                  
        seeneImg.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);
        seeneImg.vertex(lowerLeft.x, lowerLeft.y, lowerLeft.z
                        ,lowerLeftUV.x,lowerLeftUV.y
                        );
                        
        // new bottom triangle - add same vert again     
        seeneImg.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z);        
        seeneImg.vertex(lowerLeft.x, lowerLeft.y, lowerLeft.z
                        ,lowerLeftUV.x,lowerLeftUV.y
                        );
                        
        seeneImg.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z); 
        seeneImg.vertex(upperRight.x, upperRight.y, upperRight.z
                        ,upperRightUV.x, upperRightUV.y
                        );

        seeneImg.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z); 
        seeneImg.vertex(lowerRight.x, lowerRight.y, lowerRight.z
                        ,lowerRightUV.x, lowerRightUV.y
                         );             
//    println("vert[" + (j * depthmapwidth + i) + "]: " + vert[0] + ", "+ vert[1] + ", "+ vert[2]);
      }
    
    }
seeneImg.endShape();
  seeneImg.rotateZ(-PI/2);
    /*
     var n = this, i = 0, s = [], o = [], u = [], a, f, l, y, b, w, E, S, x, T, N, C, k, L, A, O, M, _;
    var s = new Float32Array(e, i, r.depthmap_width * r.depthmap_height), 
        D = r.camera_fx / r.camera_width,
        P = r.camera_fy / r.camera_height;
            for (var H = 0; H < r.depthmap_height; ++H)
                for (var B = 0; B < r.depthmap_width; ++B) {
                    var j = s[H * r.depthmap_width + B];
//this.vertices.push(new t.Vector3(j * ((B + .5) / r.depthmap_width - .5) / D, -j * ((H + .5) / r.depthmap_height - .5) / P, -(j - 1))), u.push((B + .5) / r.depthmap_width), u.push(1 - (H + .5) / r.depthmap_height);

                    this.vertices.push(new t.Vector3(j * ((B + .5) / r.depthmap_width - .5) / D, 
                                                    -j * ((H + .5) / r.depthmap_height - .5) / P, 
                                                    -(j - 1))), 
                                u.push((B + .5) / r.depthmap_width), 
                                u.push(1 - (H + .5) / r.depthmap_height);
                    if (H > 0 && B > 0) {
                        var F = [(H - 1) * r.depthmap_width + B - 1, 
                                 (H - 1) * r.depthmap_width + B, 
                                 H * r.depthmap_width + B - 1, 
                                 H * r.depthmap_width + B];
                        h(n,F[0], F[2], F[1], 0), 
                        h(n, F[1], F[2], F[3], 0), 
                          m(n.faceVertexUvs[0], 
                            u[F[0] * 2], 
                            u[F[0] * 2 + 1], 
                            u[F[2] * 2], 
                            u[F[2] * 2 + 1], 
                            u[F[1] * 2], 
                            u[F[1] * 2 + 1]), 
                          m(n.faceVertexUvs[0], 
                            u[F[1] * 2], 
                            u[F[1] * 2 + 1], 
                            u[F[2] * 2], 
                            u[F[2] * 2 + 1], 
                            u[F[3] * 2], 
                            u[F[3] * 2 + 1])
                    }
                }
            this.computeCentroids(), this.computeFaceNormals()
            
            function m(e, n, r, i, s, o, u) 
            {
              e.push([new t.Vector2(n, r), 
              new t.Vector2(i, s), 
              new t.Vector2(o, u)])
            }
            // n r i - are indicies
            function h(e, n, r, i, s) {
              e.faces.push(new t.Face3(n, r, i, null, null, s))
        }
    */
    println("done. len = " + fOut.length/3);
}
catch(Exception e)
  {
   println(e); 
  }
}


float getFloatAtCurPos(DataInputStream in)
{
  byte[] bytes = new byte[4];
  float result = 0;
  try{
    for(int i = bytes.length-1; i >= 0; i--)
      bytes[i] = in.readByte();
      
    result = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN ).getFloat();
//  result = in.readFloat();
  }
  catch(Exception e)
  {
    println(e);
  }
  return result;
}


void draw()
{
  background(0);
//  lights();
  stroke(255);
  translate(width/2,height/2);
//  rotateX(millis()/1000.f);
//  rotateY(millis()/2200.f);

  scale(mouseY*1800/height);
  for(float[] item : verts )
  {
//    point(item[0],item[1],item[2]);
//   System.out.println(item);
  }
  shape(seeneImg);
//  image(loadImage("poster.jpg"),0,0);
}
