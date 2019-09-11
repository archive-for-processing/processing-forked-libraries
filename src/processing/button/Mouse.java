package processing.button;

import processing.core.PApplet;

public class Mouse {
    
    public static final Mouse INSTANCE = new Mouse();

    private PApplet parent;

    private Timer pressTimer;
    private Timer hoverTimer;

    private MouseStateChange pressStatus;
    private MouseStateChange hoverStatus;
    
    private int longPressDelay;
    private int longHoverDelay;

    float x, y;
    float xO, yO;

    private Mouse() {}

    public void init(PApplet parent) {
        this.parent = parent;
      
        this.hoverStatus = new MouseStateChange();
        this.pressStatus = new MouseStateChange();
        
        this.pressTimer = new Timer(parent);
        this.hoverTimer = new Timer(parent);
        
        this.longPressDelay = 200;
        this.longHoverDelay = 200;
    }     

    public void setLongPressDelay(int milliseconds) {
        this.longPressDelay = milliseconds;
    }

    public void setLongHoverDelay(int milliseconds) {
        this.longHoverDelay = milliseconds;
    }

    public void  translate(float xO, float yO) {
        this.xO = xO;
        this.yO = yO;
    }

    public final void update() {
        this.pressStatus.update(this.parent.mousePressed);
        this.x = this.parent.mouseX - this.xO;
        this.y = this.parent.mouseY - this.yO;
    }

    public final void relativeTo(Button shp) {
        this.hoverStatus.update(isOver(shp));
    }

    public boolean isOver(Button shp) {
        return (this.x >= shp.x && this.x <= shp.x + shp.w
                && this.y >= shp.y && this.y <= shp.y + shp.h);
    }

    public boolean hoverStarted() {
        boolean hoverStarted = this.hoverStatus
                .equals(MouseState.hoverStart);
        if (hoverStarted) {
            this.hoverTimer.start();
            return true;
        } else {
            return false;
        }
    }

    public final boolean isHovering() {
        return this.hoverStatus.equals(MouseState.hovering)
                && this.hoverTimer.elapsed() >= this.longHoverDelay;
    }

    public final boolean hoverEnded() {
        boolean hoverEnded = this.hoverStatus
                .equals(MouseState.hoverEnd);
        if (hoverEnded) {
            this.pressTimer.reset();
            return true;
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------

    public final boolean pressStarted() {
        boolean pressStarted = this.pressStatus
                .equals(MouseState.pressStart);
        if (pressStarted) {
            this.pressTimer.start();
            return true;
        } else {
            return false;
        }
    }

    public final boolean isPressed() {
        return this.pressStatus.equals(MouseState.pressed)
                && this.pressTimer.elapsed() >= this.longPressDelay;
    }

    public final boolean pressReleased() {
        boolean pressReleased = this.pressStatus
                .equals(MouseState.released);
        if (pressReleased) {
            this.pressTimer.reset();
            return true;
        } else {
            return false;
        }
    }

    public final boolean notPressed() {
        return !this.parent.mousePressed;
    }
}
