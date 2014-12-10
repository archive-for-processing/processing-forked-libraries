package patterning.library;

import clojure.lang.IPersistentMap;

import clojure.lang.Keyword;
import clojure.java.api.Clojure;
import com.alchemyislands.patterning.api;

class PatterningStyle {
	  IPersistentMap theStyle;
	  
	  Keyword STROKE = Keyword.intern("stroke");
	  Keyword FILL = Keyword.intern("fill");
	  Keyword STROKEWEIGHT = Keyword.intern("stroke-weight");
	  Keyword BEZIER = Keyword.intern("bezier");
	  Keyword HIDDEN = Keyword.intern("hidden");  
	  
	  PatterningStyle() { theStyle = api.emptyMap(); }
	  PatterningStyle(IPersistentMap style) { theStyle = style; }
	      
	  IPersistentMap val() { return theStyle; }
	  
	  PatterningStyle stroke(int r, int g, int b, int a) { return new PatterningStyle(api.setStroke(theStyle,r,g,b,a)); }
	  PatterningStyle stroke(int r, int g, int b) { return stroke(r,g,b,255); }
	  PatterningStyle stroke(int x) { return stroke(x,x,x,255); }
	  PatterningStyle stroke(int[] c) { return stroke(c[0],c[1],c[2],c[3]); }
	  
	  PatterningStyle fill(int r, int g, int b, int a) { return new PatterningStyle(api.setFill(theStyle,r,g,b,a)); }
	  PatterningStyle fill(int r, int g, int b) { return fill(r,g,b,255);}
	  PatterningStyle fill(int x) { return fill(x,x,x,255);}
	  PatterningStyle fill(int[] c) { return fill(c[0],c[1],c[2],c[3]); }
	 
	  PatterningStyle strokeWeight(int w) { return new PatterningStyle(api.setStrokeWeight(theStyle,w));  } 

	  boolean containsKey(Object key) { return theStyle.containsKey(key); }
	  Object valAt(Object key) { return theStyle.valAt(key); }
	  
	  PatterningStyle remove(Keyword p) { return new PatterningStyle( api.removeProperty(theStyle,p));  }
	  PatterningStyle noFill() { return remove(this.FILL); }  
	}
