import java.io.FileInputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
ArrayList<float[]> verts = new ArrayList<float[]>();

void setup()
{
  size(500,500,P3D);
  try
  {
    FileInputStream fin = new FileInputStream("/Users/admin/processing-library-template/seene/data/scene.oemodel");
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
    
    
    for(int j = 0; j < depthmapheight;j++)
    {
      for(int i = 0; i < depthmapwidth;i++)  
      {
        float k = fOut[j * depthmapwidth + i];
        float vert[] = new float[]{k * ((i + .5) / depthmapwidth - .5) / D, 
                                  -k * ((j + .5) / depthmapheight - .5) / P, 
                                  -(k - 1)};
        verts.add(vert);
    println("vert[" + (j * depthmapwidth + i) + "]: " + vert[0] + ", "+ vert[1] + ", "+ vert[2]);
      }
    }
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
  stroke(255);
  for(float[] item : verts )
  {
    point(item[0],item[1],item[2]);
//   System.out.println(item);
  }
}
