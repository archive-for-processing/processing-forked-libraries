package template.library;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SeeneObject {
	private PApplet mParent;
	private PShape mShape;
	private String mModelFilePath;
	private String mOwner;
	private String mCaption;
	private PImage mTexture;
	
	private SeeneObject()
	{
		
	}
	
	public SeeneObject(PApplet applet)
	{
		this(applet, null);
	}
	
	public SeeneObject(PApplet applet, String seeneURL)
	{
		mParent = applet;
		String oeModelandTextureURL[] = getOEModelAndTextureURL(seeneURL);
		mTexture = mParent.loadImage(oeModelandTextureURL[1]);
		InputStream is = null;
		try {
			is = new URL(oeModelandTextureURL[0]).openStream();
			loadSeeneShapeFromFile( is );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SeeneObject(PApplet applet, String oeModelFileName, String textureFileName)
	{
		mParent = applet;
		mModelFilePath = oeModelFileName;
		mTexture = mParent.loadImage(textureFileName);
		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(mModelFilePath));
			loadSeeneShapeFromFile( in );
		}
		catch(Exception e)
		{
			
		}
	}
	
	private String[] getOEModelAndTextureURL(String seeneURL)
	{
		Document doc = null;
        try
        {
        	System.out.println("Fetching " + seeneURL + "...");
        	doc = Jsoup.connect(seeneURL).get();        	
        }
        catch(Exception e)
        {        	
        	System.out.println(""+e + e.getStackTrace());
        	System.out.println("Failed to open " + seeneURL);	
        }
        Elements scriptElements = doc.getElementsByTag("script");

        String modelStr = "";
        String textureStr = "";
        for (Element element :scriptElements ){                
               for (DataNode node : element.dataNodes()) {
            	   String data = node.getWholeData();
            	   if(data != null)
            	   {
	            	   String[] processStr = data.split("oemodel");
	            	   if(processStr != null)
	            	   {
		            	   processStr = processStr[0].split("\"");
		            	   if(processStr.length > 0)
		            	   {
			            	   modelStr = processStr[processStr.length-1] + "oemodel";
			                   System.out.println(modelStr);
		            	   }
	            	   }
	            	   
	            	   processStr = data.split("fullsize_poster.jpg");
	            	   if(processStr != null)
	            	   {
		            	   processStr = processStr[0].split("\"");
		            	   if(processStr.length > 0)
		            	   {
		            		   textureStr = processStr[processStr.length-1] + "fullsize_poster.jpg";
			                   System.out.println(textureStr);
		            	   }
	            	   }
            	   }
               }
               System.out.println("-------------------");            
         }
        
		return new String []{modelStr,textureStr};
	}
	
	
	// reverses the endianess of the data stream so we can 
	// properly pull a 32 bit float out...  Slow but the only way I 
	// could figure to do it in Java...
	private float getFloatAtCurPos(DataInputStream in)
	{
		  byte[] bytes = new byte[4];
		  float result = 0;
		  try{
		    for(int i = bytes.length-1; i >= 0; i--)
		      bytes[i] = in.readByte();
		      
		    result = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN ).getFloat();
		  }
		  catch(Exception e)
		  {
			  System.out.println(e);
		  }
		  return result;
	}
	
	//this method is a crude reconstruction of the javascript that 
	//walks the oeModel binary and reconstructs the 3D Data from
	//it.  As of Oct. 28th 2013 the Java here faithfully reproduces 
	// the models found on the website and in the app.
	public void  loadSeeneShapeFromFile(InputStream is) 
	{
		mShape = null;
		try
		  {
//		    FileInputStream fin = new FileInputStream(mModelFilePath);
//		    System.out.println("fin.available(): " + fin.available());
//		    DataInputStream in = new DataInputStream(fin);

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
		    System.out.println("version: " + version); //should be '2'
		    //4 offset should be something like 720
		    int cameraWidth = Integer.reverseBytes(in.readInt());
		    System.out.println("cameraWidth: " + cameraWidth);
		    
		    //8 should be something like 720
		    int cameraHeight = Integer.reverseBytes(in.readInt());
		    System.out.println("cameraHeight: " + cameraHeight);
		    
		    //at byte 12 should be something like 1252.39842
		    float cameraFX = getFloatAtCurPos(in);
		    System.out.println("cameraFX: " + cameraFX);
		    
		    //at byte 16 should be something like 1247.39842
		    float cameraFY = getFloatAtCurPos(in);
		    System.out.println("cameraFy: " + cameraFY);
		    
		    //at byte 20 should be something like 0.023
		    float cameraK1 = getFloatAtCurPos(in);
		    System.out.println("cameraK1: " + cameraK1);
		    
		    //at byte 20 should be something like .3207...
		    float cameraK2 = getFloatAtCurPos(in);
		    System.out.println("cameraK2: " + cameraK2);
		    
		    //at byte 28 ~~90
		    int depthmapwidth = Integer.reverseBytes(in.readInt());
		    System.out.println("depthmapwidth: " + depthmapwidth);
		    //at byte 32 ~~90
		    int depthmapheight = Integer.reverseBytes(in.readInt());
		    System.out.println("depthmapheight: " + depthmapheight);
		    
		    int floatCount = depthmapheight* depthmapwidth;
		    float fOut[] = new float[floatCount];
		    float scale = 1;
		    for(int i = 0; i<fOut.length;i++)
		    {
		     fOut[i] = scale* getFloatAtCurPos(in);
//		     print(",[" +i + "]: " + fOut[i]);
		    }
		    
		    float D = cameraFX / cameraWidth;
		    float P = cameraFY / cameraHeight;
		    System.out.println("D/P: " + D +"/"+ P);
		    PImage tex = mTexture;
		    mParent.textureMode(PConstants.NORMAL);
		    mShape = mParent.createShape();
		    
		    mShape.beginShape(PConstants.TRIANGLES);
		    mShape.texture(tex);
		    mShape.noStroke();
		    
		    for(int j = 0; j < depthmapheight-1; j++)
		    {
		      for(int i = 0; i < depthmapwidth-1;i++)  
		      {
		        float depthVal = fOut[j * depthmapwidth + i];
		        PVector upperLeft = new PVector(-depthVal * ((i + .5f) / depthmapwidth - .5f) / D, 
		                                        -depthVal * ((j + .5f) / depthmapheight - .5f) / P, 
		                                        -(depthVal - 1));
		        PVector upperLeftUV = new PVector((i + .5f) / depthmapwidth,
		                                          (j + .5f) / depthmapheight);
		                                          
		        depthVal = fOut[j * depthmapwidth + i+1];
		        PVector upperRight = new PVector(-depthVal * ((i+1 + .5f) / depthmapwidth - .5f) / D, 
		                                        -depthVal * ((j + .5f) / depthmapheight - .5f) / P, 
		                                        -(depthVal - 1));
		        PVector upperRightUV = new PVector((i+1 + .5f) / depthmapwidth,
		                                          (j + .5f) / depthmapheight);                                     
		                                          
		        depthVal = fOut[(j+1) * depthmapwidth + i];
		        PVector lowerLeft = new PVector(-depthVal * ((i + .5f) / depthmapwidth - .5f) / D, 
		                                        -depthVal * ((j+1 + .5f) / depthmapheight - .5f) / P, 
		                                        -(depthVal - 1));
		        PVector lowerLeftUV = new PVector((i + .5f) / depthmapwidth,
		                                           (j+1 + .5f) / depthmapheight);

		        depthVal = fOut[(j+1) * depthmapwidth + i+1];
		         PVector lowerRight = new PVector(-depthVal * ((i+1 + .5f) / depthmapwidth - .5f) / D, 
		                                          -depthVal * ((j+1 + .5f) / depthmapheight - .5f) / P, 
		                                          -(depthVal - 1));
		        PVector lowerRightUV = new PVector((i+1 + .5f) / depthmapwidth,
		                                           (j+1 + .5f) / depthmapheight);
		                                           
		        PVector tri1Norm = PVector.sub(lowerLeft,upperLeft).cross( PVector.sub(upperRight,upperLeft));
		        tri1Norm.normalize();
		        PVector tri2Norm = PVector.sub(upperRight,lowerRight).cross( PVector.sub(lowerLeft,lowerRight));
		        tri2Norm.normalize();
		        
		        mShape.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);
		        mShape.vertex(upperLeft.x, upperLeft.y, upperLeft.z
		                        ,upperLeftUV.x,upperLeftUV.y
		                        );
		        mShape.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);                          
		        mShape.vertex(upperRight.x, upperRight.y, upperRight.z
		                        ,upperRightUV.x,upperRightUV.y
		                        );
		                                  
		        mShape.normal(tri1Norm.x,tri1Norm.y,tri1Norm.z);
		        mShape.vertex(lowerLeft.x, lowerLeft.y, lowerLeft.z
		                        ,lowerLeftUV.x,lowerLeftUV.y
		                        );
		                        
		        // new bottom triangle - add same vert again     
		        mShape.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z);        
		        mShape.vertex(lowerLeft.x, lowerLeft.y, lowerLeft.z
		                        ,lowerLeftUV.x,lowerLeftUV.y
		                        );
		                        
		        mShape.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z); 
		        mShape.vertex(upperRight.x, upperRight.y, upperRight.z
		                        ,upperRightUV.x, upperRightUV.y
		                        );

		        mShape.normal(tri2Norm.x,tri2Norm.y,tri2Norm.z); 
		        mShape.vertex(lowerRight.x, lowerRight.y, lowerRight.z
		                        ,lowerRightUV.x, lowerRightUV.y
		                         );             
//		    System.out.println("vert[" + (j * depthmapwidth + i) + "]: " + vert[0] + ", "+ vert[1] + ", "+ vert[2]);
		      }		    
		    }
		    mShape.endShape();
		    mShape.rotateZ(-PConstants.PI/2);
		    //apparently setting the scale flattens the mesh???
		    mShape.is3D(true);
		    mShape.scale(400,400,400);
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
		    System.out.println("done. len = " + fOut.length/3);
		  }
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
//		return seeneImg;
	}
	
	public PShape getShape()
	{
		return mShape;// mParent.createShape(mShape);
	}
	
	public void draw()
	{
		mParent.shape(mShape);
	}
	
	public void draw(float x, float y)
	{
		this.draw(x,y,0);
	}
	
	
	public void draw(float x, float y, float z)
	{
		mParent.pushMatrix();
		mParent.translate(x, y, z);
		mParent.shape(mShape);
		mParent.popMatrix();
	}
	
	/*
	 * window.appBootstrapData = 
{
"scene":{
"short_code":"abQCaL",
"identifier":"f3309098-4d40-459b-bb85-38d5ed056689",
"caption":null,
"shared":true,
"poster":{"fullsize":"https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/fullsize_poster.jpg",
"iphone":"https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/iphone_poster.jpg",
"tile":"https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/tile_poster.jpg",
"blur":"https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/blur_poster.jpg",
"original":"https://camera-storage.s3.amazonaws.com/uploads/scene/poster/f3309098-4d40-459b-bb85-38d5ed056689/poster.jpg"},
"model_url":"https://camera-storage.s3.amazonaws.com/uploads/scene/model/f3309098-4d40-459b-bb85-38d5ed056689/scene.oemodel",
"short_url":"http://seene.co/s/abQCaL",
"likes_count":0,
"view_count":0,
"hashtags":[],
"orientation":0,
"editors_pick":false,"captured_at":"2013-10-16T02:31:54Z",
"created_at":"2013-10-28T05:08:17Z",
"updated_at":"2013-10-28T05:08:31Z",
"max_depth":"1.0",
"safety_level":0,
"state":"ready",
"user":{"id":223068,
"username":"lorenipsum",
"name":"lorenipsum",
"avatar":{"thumb":"",
"thumb_retina":"",
"original":""},
"bio":null,
"shared_scenes_count":1,
"followings_count":0,
"followers_count":0,
"created_at":"2013-10-28T05:07:21Z"},
"recent_likes":[]}};

	 * 
	 * 
	 * 
	 * 
	 * */
}
