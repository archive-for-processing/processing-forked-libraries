package patterning.library;

import clojure.lang.IPersistentVector;
import clojure.lang.IPersistentMap;
import com.alchemyislands.patterning.api;

public class Simple extends IPattern {
    IPersistentMap style;
    Float[][] points;
    Simple(Float[][] pts, PatterningStyle stl) {
      setStyle(stl);
      points = pts;
    }  
    Simple setStyle(PatterningStyle aStyle) { style = aStyle.val(); return this; }
    Simple setStyle(IPersistentMap style) { this.style = style; return this; }
    IPersistentVector val() { return api.makeShapePattern(style,points); }
    IPattern ipVal() { return this; }
}
