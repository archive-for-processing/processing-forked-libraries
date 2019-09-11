package processing.button;

import processing.core.PApplet;
import processing.core.PConstants;

class EllipButton extends Button {

    public EllipButton(PApplet parent, String txt, int x, int y, int w, int h) {
        super(parent, PConstants.ELLIPSE, txt, x, y, w, h);
    }
}
