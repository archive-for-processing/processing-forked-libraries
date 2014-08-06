/**
 * ####
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package GLSLShaders.library;


import java.util.ArrayList;

import processing.core.*;
import processing.opengl.PShader;
import processing.*;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 * @example Hello 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */




public class GLSLShader extends PApplet {
	
	// myParent is a reference to the parent sketch
	PApplet parent;

	String name;
	int type;
	public PShader shader;
	ArrayList<Param> parameters;
	
	public final static String VERSION = "0.1b";

	final static int TYPE_COLOR = 0;
	final static int TYPE_TEXTURE = 1;	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public GLSLShader(PApplet _parent, String _name) {
		
		name=_name;
		parent = _parent;
		parameters = new ArrayList<Param>();
	    setShader(_name);
	}
	
	
	private void setShader(String _name) {
		
		if (_name.equals("warp")){
		       
        	type= TYPE_COLOR;
        	addParameter("detail", 0, 2);
            addParameter("complexity", 0, 2);
    		shader = parent.loadShader("warp.glsl");
	}
		
		if (_name.equals("blobby")){
       
            	type= TYPE_COLOR;
            	addParameter("depth", 0, 2);
                addParameter("rate", 0, 2);
        		shader = parent.loadShader("blobby.glsl");
		}
                
		if (_name.equals("drip")){
            	type= TYPE_COLOR;
            	addParameter("intense", 0, 1);
                addParameter("speed", 0, 1);
                addParameter("graininess", 0, 1, 0, 1);
        		shader = parent.loadShader("drip.glsl");
		}
                
		if (_name.equals("electro")){
            	type= TYPE_COLOR;
            	addParameter("rings", 5, 40);
                addParameter("complexity", 1, 60);
        		shader = parent.loadShader("electro.glsl");
		}
                
		if (_name.equals("eye")){
            	type=TYPE_COLOR;
            	addParameter("mouse", 0,width,0, height);
        		shader = parent.loadShader("eye.glsl");
		}
                
		if (_name.equals("bands")){
            	type=TYPE_COLOR;
            	addParameter("noiseFactor", 5, 100);
            	addParameter("stripes", 0, 100);
        		shader = parent.loadShader("bands.glsl");
		}
                
		if (_name.equals("sinewave")){
            	type=TYPE_COLOR;
            	addParameter("colorMult", (float) 0.5, (float) 5.0,(float) 0.5,(float) 2.0);
            	addParameter("coeffx", 10, 50);
            	addParameter("coeffy", 0, 90);
            	addParameter("coeffz", 1, 200);
        		shader = parent.loadShader("sinewave.glsl");
		}
                 
		if (_name.equals("noisy")){
            	type=TYPE_COLOR;
            	addParameter("noiseFactor", 0, 10, 0, 10);
            	addParameter("noiseFactorTime", 0, 2);
        		shader = parent.loadShader("noisy.glsl");
		}  
                
		if (_name.equals("nebula")){
            	type=TYPE_COLOR;
            	addParameter("starspeed", 0, 100);
        		shader = parent.loadShader("nebula.glsl");
		}
                
		if (_name.equals("landscape")){
            	type=TYPE_COLOR;
            	addParameter("dir", (float) 1.5, 5);
        		shader = parent.loadShader("landscape.glsl");
		}  
                             
		if (_name.equals("monjori")){
            	type=TYPE_COLOR;
            	addParameter("graininess", 10, 100);
            	addParameter("pace", 20, 80);
            	addParameter("twist", 0, 100);
        		shader = parent.loadShader("monjori.glsl");
		}   
                
		if (_name.equals("bits")){
            	type=TYPE_COLOR;
            	addParameter("mx", 0, 1);
            	addParameter("my", 0, 1);
        		shader = parent.loadShader("bits.glsl");
		}                
		if (_name.equals("rain")){
            	type=TYPE_COLOR;
            	addParameter("hue",(float) 0,(float) 0.1);
            	addParameter("fade", 0, 1);
            	addParameter("slow",(float) 0.1, 3);
            	addParameter("gray", 0, 1);
        		shader = parent.loadShader("rain.glsl");
		}     
                
		if (_name.equals("brcosa")){
            	type=TYPE_TEXTURE;
            	addParameter("brightness", 0, 2);
            	addParameter("saturation", -5, 5);
            	addParameter("contrast", -5, 5);
        		shader = parent.loadShader("brcosa.glsl");
		}            
                
		if (_name.equals("hue")){
            	type=TYPE_TEXTURE;
            	addParameter("hue", 0, TWO_PI);
        		shader = parent.loadShader("hue.glsl");
		}
                
		if (_name.equals("pixelate")){
            	type=TYPE_TEXTURE;
            	addParameter("pixels", 1, 100, 1, 100);
        		shader = parent.loadShader("pixelate.glsl");
		}    
                
		if (_name.equals("blur")){
            	type=TYPE_TEXTURE;
            	addParameter("sigma", 0, 10);
            	addParameter("blurSize", 0, 30, true);
            	addParameter("texOffset", 0, 5,0,5);
        		shader = parent.loadShader("blur.glsl");
		}
                
		if (_name.equals("channels")){
            	type=TYPE_TEXTURE;
            	  addParameter("rbiasx", -1, 1);
            	  addParameter("rbiasy", -1, 1);
            	  addParameter("gbiasx", -1, 1);
            	  addParameter("gbiasy", -1, 1);
            	  addParameter("bbiasx", -1, 1);
            	  addParameter("bbiasy", -1, 1);
            	  addParameter("rmultx", 0, 2);
            	  addParameter("rmultaddy", 0, 2);
            	  addParameter("gmultx", 0, 2);
            	  addParameter("gmulty", 0, 2);
            	  addParameter("bmultx", 0, 2);
            	  addParameter("bmulty", 0, 2);
        		shader = parent.loadShader("channels.glsl");
		}      
                
		if (_name.equals("threshold")){
            	type=TYPE_TEXTURE;
            	addParameter("threshold", 0, 1);
        		shader = parent.loadShader("threshold.glsl");
		}    
                
		if (_name.equals("neon")){
            	type=TYPE_TEXTURE;
            	addParameter("brt",(float) 0, (float)0.5);
            	addParameter("rad", 0, 3, true);
        		shader = parent.loadShader("neon.glsl");
		}
                
		if (_name.equals("edges")){
            	type=TYPE_TEXTURE;
        		shader = parent.loadShader("edges.glsl");
		}
                
		if (_name.equals("wrap")){
            	type=TYPE_TEXTURE;
            	addParameter("radius", 0, 2);
            	addParameter("radTwist", 1, 10);
        		shader = parent.loadShader("wrap.glsl");
		}
                
		if (_name.equals("deform")){
            	type=TYPE_TEXTURE;
            	addParameter("mouse", 0, 1, 0, 1);
            	addParameter("turns", 2, 10);
            	addParameter("time", 0, 1000000);
        		shader = parent.loadShader("deform.glsl");
		}  
                
		if (_name.equals("pixelrolls")){
            	type=TYPE_TEXTURE;
            	addParameter("pixels", 0, 200, 0, 200);
            	addParameter("rollRate", 0, 10);
            	addParameter("rollAmount", 0, 1);
            	addParameter("time", 0, 1000000);
        		shader = parent.loadShader("pixelrolls.glsl");
		}  
                
		if (_name.equals("patches")){
            	type=TYPE_TEXTURE;
            	addParameter("row", 0, 1);
            	addParameter("col", 0, 1);
            	addParameter("time", 0, 1000000);

        		shader = parent.loadShader("patches.glsl");
		}
                
		if (_name.equals("modcolor")){
            	type=TYPE_TEXTURE;
            	addParameter("modr",(float) 0,(float) 0.5);
            	addParameter("modg",(float) 0,(float) 0.5);
            	addParameter("modb",(float) 0,(float) 0.5);
        		shader = parent.loadShader("modcolor.glsl");
		}  
                
		if (_name.equals("halftone")){
            	type=TYPE_TEXTURE;
            	addParameter("pixelsPerRow", 2, 100, true);
        		shader = parent.loadShader("halftone.glsl");
		} 
                
		if (_name.equals("halftone_cmky")){
            	type=TYPE_TEXTURE;
            	addParameter("density", 0, 1);
            	addParameter("frequency", 0, 100);
        		shader = parent.loadShader("halftone_cmyk.glsl");
		} 
                
		if (_name.equals("invert")){
            	type=TYPE_TEXTURE;
        		 shader = parent.loadShader("invert.glsl");
		}
		
		if (_name.equals("bilateral_filter")){
            	type=TYPE_TEXTURE;
            	addParameter("sigma", 0, 2);
			    addParameter("resolution",0,1000000,0,1000000);

        		shader = parent.loadShader("bilateral_filter.glsl");
		}   
		
		
                
		if (_name.equals("ripple")){
            	type=TYPE_TEXTURE;
            	addParameter("ctr", 0, 1,0,1);
        		shader = parent.loadShader("ripple.glsl");
                   
		}    
		
		 if (type == TYPE_COLOR) {
			    addParameter("time", 0, 100000);
			    addParameter("resolution",0,1000000,0,1000000);
	    }	
	}
	
	public void addParameter(String name, float minVal, float maxVal, boolean isInt) {
	    Param param = new Param(name, minVal, maxVal, isInt);
	    parameters.add(param);
	  }
	  
	public  void addParameter(String name, float minVal, float maxVal) {
	    addParameter(name, minVal, maxVal, false);
	  }
	  
	public  void addParameter(String name, float minVal1, float maxVal1, float minVal2, float maxVal2, boolean isInt) {
	    Param param = new Param(name, minVal1, maxVal1, minVal2, maxVal2, isInt);
	    parameters.add(param);
	  }

	public  void addParameter(String name, float minVal1, float maxVal1, float minVal2, float maxVal2) {
	    addParameter(name, minVal1, maxVal1, minVal2, maxVal2, false);
	  }
	  
	public  void setShaderParameters(int width,int height) {
	    shader.set("time", (float) (millis()/1000.0));
	    shader.set("resolution", (float)width, (float)height);
	    for (Param p : parameters) {
	      if (p.is2d) {
	        if (p.isInt)  shader.set(p.name, (int) p.value2.x, (int) p.value2.y);
	        else          shader.set(p.name, p.value2.x, p.value2.y);
	      } else {
	        if (p.isInt)  shader.set(p.name, (int) p.value);
	        else          shader.set(p.name, p.value);        
	      }
	    }
	    if (name.equals("ripple")) {
	      shader.set("time", (float)(millis() / 1000.0));
	    }
	  }
	
	public  void setParameter(String _name, float _value) {
	   
	            for (Param p : parameters) {
	    if (p.name.equals(_name))	
	    	
	      if (p.is2d == false) 
	        if (p.isInt)  shader.set(p.name, (int) _value);
	        else          shader.set(p.name, _value);  
	    
	    }
	  }
	
public  void setParameter(String _name, float _value, float _value2) {
	    
	    for (Param p : parameters) {
	    if (p.name.equals(_name))	
	      if (p.is2d == true) 
	        if (p.isInt)  shader.set(p.name, (int) _value, (int) _value2);
	        else          shader.set(p.name, _value, _value2);  
	    }
	  }
	
	

	
}



