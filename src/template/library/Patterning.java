/**
 * ##library.name##
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

package template.library;
import processing.core.*;

import java.util.Iterator;
import java.lang.Long;

import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import clojure.lang.IPersistentList;
import clojure.lang.ISeq;
import clojure.lang.IPersistentMap;
import clojure.lang.IFn;
import clojure.lang.IMapEntry;
import clojure.lang.SeqIterator;
import clojure.lang.Indexed;
import clojure.lang.Keyword;

import clojure.java.api.Clojure;
import com.alchemyislands.patterning.api;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template'
 * to your own library or tool naming convention.
 *
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello
 */

public class Patterning {
    IFn txpt;
    IPattern main;

    PApplet app;

    public final static String VERSION = "##library.prettyVersion##";
    public static String version() {return VERSION;	}

    public Patterning(PApplet theParent) {
        app = theParent;
        setup();
    }

    private void setup() {
        txpt = api.makeTransformPointFunction(-1.0, -1.0, 1.0, 1.0, 0.0, 0.0, app.width, app.height);
        System.out.println("##library.name## ##library.prettyVersion## by ##author##");
    }


    // Making styles

    int[] pColor(float r, float g, float b, float a) { return new int[]{(int)r,(int)g,(int)b,(int)a}; }
    int[] pColor(float r, float g, float b) { return pColor(r,g,b,255); }
    int[] pColor(float x) { return pColor(x,x,x,255); }


    PatterningStyle style(float sw, int sr, int sg, int sb, int sa, int fr, int fg, int fb, int fa) {
        return (new PatterningStyle()).strokeWeight((int)sw).stroke(sr,sg,sb,sa).fill(fr,fg,fb,fa);
    }

    PatterningStyle style(float sw, int[] c) { return (new PatterningStyle()).strokeWeight((int)sw).stroke(c); }
    PatterningStyle style(float sw, int[] sc, int[] fc) { return style(sw,sc).fill(fc); }

    // Drawing
    void drawSShape(IFn txpt, IPersistentMap sshape) {
        PatterningStyle style = new PatterningStyle ( (IPersistentMap)sshape.valAt(Clojure.read(":style")) );
        if (style.containsKey(style.HIDDEN)) { return; }

        IPersistentMap tsshape = api.transformSShape(txpt,sshape);

        app.pushStyle();
        if (style.containsKey(style.STROKE)) { app.stroke(mkColor((IPersistentVector)style.valAt(style.STROKE))); }
        if (style.containsKey(style.FILL))  {
            app.fill(mkColor((IPersistentVector)style.valAt(style.FILL)));
        } else {
            app.noFill();
        }
        if (style.containsKey(style.STROKEWEIGHT)) { app.strokeWeight(iv(style.valAt(style.STROKEWEIGHT))); }
        if (style.containsKey(style.BEZIER)) {
            /*
              (if (contains? style :bezier)
              (let [ls (sshapes/flat-point-list tsshape) ]
              (apply bezier ls)
              )
            */

        } else {
            app.beginShape();
            Object pts = tsshape.valAt(Clojure.read(":points"));
            for (Iterator points = new SeqIterator((ISeq)pts);points.hasNext();) {
                IPersistentVector p = (IPersistentVector)points.next();
                app.vertex(((Double)p.nth(0)).floatValue(), ((Double)p.nth(1)).floatValue());
            }
            app.endShape();
        }
        app.popStyle();
    }

    void draw(IPersistentVector group) {
        for (int i=0;i<group.count();i++) {
            drawSShape(txpt,(IPersistentMap)group.nth(i));
        }
    }

    void draw(IPattern pat) {
        draw(pat.val());
        main = pat;
    }

    // Some basic shapes

    IPattern poly(float cx, float cy, float d, float n, PatterningStyle style) {
        return new Pattern(api.poly(cx,cy,d,(int)n,style.val()));
    }

    IPattern turtle(float x, float y, String script, PatterningStyle style) {
        return new Turtle().move(x,y).setInitialAngle(-PApplet.PI/2).setTurnAngle(PApplet.PI/8)
            .setDist((float)0.05).setScript(script).setStyle(style);
    }

    Simple simple(Float[][] pts, PatterningStyle style) { return new Simple(pts,style); }

    Float[][] points(float... fs) {

        int l = (int)(fs.length/2);
        Float[][] ps = new Float[l+1][2];
        for (int i=0;i<l;i=i+1) {
            ps[i][0] = fs[i*2];
            ps[i][1] = fs[(i*2)+1];
        }
        ps[l][0]=fs[0];
        ps[l][1]=fs[1];
        return ps;
    }

    IPattern[] patterns(IPattern... ps) {
        int l = ps.length;
        IPattern[] ips = new IPattern[l];
        for (int i=0;i<l;i++) { ips[i] = ps[i]; }
        return ips;
    }

    IPattern stack(IPattern... ps) {
        if (ps.length==0) { return simple(points(), new PatterningStyle()); }
        IPattern p = ps[0];
        for (int i=1;i<ps.length;i++) {
            p = p.stack(ps[i]);
        }
        return p;
    }

    IPattern scale(float val, IPattern p) { return new Pattern( api.scale(val, p.val())); }
    IPattern translate(float dx, float dy, IPattern p) { return new Pattern( api.translate(dx,dy,p.val())); }
    IPattern translateTo(float x, float y, IPattern p) { return new Pattern( api.translateTo(x,y,p.val())); }
    IPattern hReflect(IPattern p) { return new Pattern( api.hReflect(p.val())); }
    IPattern vReflect(IPattern p) { return new Pattern( api.vReflect(p.val())); }
    IPattern stretch(float sx, float sy, IPattern p) { return new Pattern( api.stretch(sx,sy,p.val())); }
    IPattern rotate(float da, IPattern p) { return new Pattern( api.rotate(da,p.val())); }

    IPattern fourMirror(IPattern p)   { return new Pattern(api.fourMirror(p.val())); }
    IPattern fourRound(IPattern p)    { return new Pattern(api.fourRound(p.val())); }
    IPattern clock(int n, IPattern p) { return new Pattern(api.clock(n,p.val())); }
    IPattern grid(int n, IPattern p)  { return new Pattern(api.grid(n,p.val())); }
    IPattern gridlist(int n, IPattern[] pats)  {
        IPersistentVector[] ps = new PersistentVector[pats.length];
        for (int i=0;i<pats.length;i++) { ps[i]=pats[i].val();  }
        return new Pattern(api.gridList(n,ps));
    }


    // User interface
    int _qw = 1;
    int _as = 1;
    int _zx = 1;
    int _er = 1;
    int _df = 1;
    int _cv = 1;

    int change(int x, int min, int max, int oset) {x=x+oset; if (x>max) {x=max;}; if (x<0) {x=0;}; return x;}
    int change(int x, int max, int oset) {return change(x,0,max,oset);}

    void keyPressed() {
        switch (app.key) {
        case 32:
            app.save("out.png");
            String[] strings = new String[1];
            strings[0] = main.svg();
            app.saveStrings("out.svg",strings);
            break;

        case 'q':
            _qw=change(_qw,100,-1);
            break;
        case 'w':
            _qw=change(_qw,100,1);
            break;

        case 'a':
            _as=change(_as,100,-1);
            break;
        case 's':
            _as=change(_as,100,1);
            break;

        case 'z':
            _as=change(_zx,100,-1);
            break;
        case 'x':
            _as=change(_zx,100,1);
            break;

        }
    }


    int iv(Object o) { return ((Number)o).intValue(); }

    int mkColor(IPersistentVector col) {
        return app.color(iv(col.nth(0)),iv(col.nth(1)),iv(col.nth(2)),iv(col.nth(3)));
    }

    float mouseX() { return PApplet.map(app.mouseX,0,app.width,-1,1); }
    float mouseX(int max) { return app.map(app.mouseX,0,app.width,0,max); }

    float mouseY() { return PApplet.map(app.mouseY,0,app.height,-1,1); }
    float mouseY(int max) { return app.map(app.mouseY,0,app.height,0,max); }

    float qw() { return _qw; }
    float qw(int max) { return PApplet.map(_qw,0,100,0,max); }

    float as() { return _as; }
    float as(int max) { return PApplet.map(_as,0,100,0,max); }

    float zx() { return _zx; }
    float zx(int max) { return PApplet.map(_zx,0,100,0,max); }

}
