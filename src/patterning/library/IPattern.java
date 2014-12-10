package patterning.library;

import clojure.lang.IPersistentVector;
import com.alchemyislands.patterning.api;

public abstract class IPattern {
    Float[] viewport = {(float) -1.0, (float) -1.0, (float) 1.0, (float) 1.0};    
    IPattern  stack(IPersistentVector group) { return new Pattern(api.superimpose(this.val(),group));  }
    IPattern rStack(IPersistentVector group) { return new Pattern(api.superimpose(group,this.val()));  }
      
    IPattern  stack(IPattern pattern) { return stack(pattern.val()); }
    IPattern rStack(IPattern pattern) { return rStack(pattern.val()); }

    abstract IPersistentVector val();
    abstract IPattern ipVal();

    String svg() { return api.makeSVG(viewport, 1000, 1000, this.val()); }  
}
    