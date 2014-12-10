package template.library;

import clojure.lang.IFn;
import com.alchemyislands.patterning.api;

class LSystem {
	  String[][] rules; 
	  IFn lsys;
	  
	  LSystem() { rules = new String[1][1]; }  
	  LSystem(String[][] rules) { setRules(rules); }
	  
	  LSystem setRules(String[][] rules) {
	    this.rules = rules; 
	    lsys = api.makeLSystem(rules);
	    return this;
	  }
	  
	  String run(int steps, String seed) {
	    return api.runLSystem(lsys,steps,seed);
	  }
	}
