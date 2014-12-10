package patterning.library;

import clojure.lang.IPersistentVector;
import com.alchemyislands.patterning.api;

class Pattern extends IPattern {
	  IPersistentVector pattern;
	  Pattern() { pattern = api.emptyGroup();  }  
	  Pattern(IPersistentVector p) { pattern = p; }
	  Pattern(IPattern ip) { pattern = ip.val(); }
	  IPersistentVector val() { return pattern; }
	  IPattern ipVal() { return this; }
	}
