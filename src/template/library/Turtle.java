package template.library;

import clojure.lang.IPersistentVector;
import clojure.lang.IPersistentMap;
import com.alchemyislands.patterning.api;

class Turtle extends IPattern {
	  IPersistentMap style;
	  float x,y,a,d,da;
	  String script;

	  Turtle() { style = new PatterningStyle().val(); }
	  Turtle setStyle(PatterningStyle aStyle) { style = aStyle.val(); return this; }
	  Turtle setStyle(IPersistentMap style) { this.style = style; return this; }
	  Turtle setScript(String s) { script = s; return this;}
	  Turtle move(float x, float y) { this.x=x; this.y=y; return this; }
	  Turtle setInitialAngle(float a) { this.a = a; return this;}
	  Turtle setTurnAngle(float da) { this.da = da; return this;}
	  Turtle setDist(float d) {this.d = d; return this; }

	  IPersistentVector val() { return api.basicTurtle(x,y,d,a,da, script, api.emptyMap(), style); }
	  IPattern ipVal() { return this; }
	}
