package processing.button;

import processing.core.PApplet;
import processing.core.PConstants;

public class RectButton extends Button {

    public RectButton(PApplet parent, String txt, int x, int y, int w, int h) {
        super(parent, PConstants.RECT, txt, x, y, w, h);
    }
}
