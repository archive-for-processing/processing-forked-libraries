package button.library;


FixedMouseStateChange notHovering = new FixedMouseStateChange(false, false), 
    hoverStart = new FixedMouseStateChange(false, true), 
    hovering = new FixedMouseStateChange(true, true), 
    hoverEnd = new FixedMouseStateChange(true, false), 
    notPressed = new FixedMouseStateChange(false, false), 
    pressStart = new FixedMouseStateChange(false, true), 
    pressed = new FixedMouseStateChange(true, true), 
    released = new FixedMouseStateChange(true, false);

class Mouse {
    public float x, y;
    public float xO, yO;

    private Timer pressTimer;
    private Timer hoverTimer;
    
    private int longPressDelay;
    private int longHoverDelay;

    private MouseStateChange pressStatus;
    private MouseStateChange hoverStatus;


    //public float y;

    public Mouse() {
        this.pressTimer = new Timer();
        this.hoverTimer = new Timer();
        
        this.longPressDelay = 200;
        this.longHoverDelay = 200;
        
        this.hoverStatus = new MouseStateChange();
        this.pressStatus = new MouseStateChange();
    }
    
    public void setLongPressDelay(int milliseconds) {
        this.longPressDelay = milliseconds;
    }
    
    public void longHoverDelay(int milliseconds) {
        this.longHoverDelay = milliseconds;
    }

    public void translate(float xO, float yO) {
        this.xO = xO;
        this.yO = yO;
    }

    public final void update() {
        this.pressStatus.update(mousePressed);
        this.x = mouseX - this.xO;
        this.y = mouseY - this.yO;
    }

    public final void relativeTo(Button shp) {
        this.hoverStatus.update(isOver(shp));
    }

    public final boolean hoverStarted() {        
        boolean hoverStarted = hoverStatus.equals(hoverStart);
        if (hoverStarted) {
            hoverTimer.start();
            return true;
        } else {
            return false;
        }
    }

    public final boolean isHovering() {
        return hoverStatus.equals(hovering) && hoverTimer.elapsed() >= this.longHoverDelay;
    }

    public final boolean hoverEnded() {
        boolean hoverEnded = hoverStatus.equals(hoverEnd);
        if (hoverEnded) {
            pressTimer.reset();
            return true;
        } else {
            return false;
        }
    }

    public final boolean notHovering() {
        return hoverStatus.equals(notHovering);
    }

    private boolean isOver(Button shp) {
        return (this.x >= shp.x && this.x <= shp.x + shp.w && 
            this.y >= shp.y && this.y <= shp.y + shp.h);
    }


    // ------------------------------------------------------------------------------------

    public final boolean pressStarted() {
        boolean pressStarted = pressStatus.equals(pressStart);
        if (pressStarted) {
            pressTimer.start();
            return true;
        } else {
            return false;
        }
    }

    public final boolean isPressed() {
        return pressStatus.equals(pressed) && pressTimer.elapsed() >= this.longPressDelay;
    }

    public final boolean pressReleased() {
        boolean pressReleased = pressStatus.equals(released);
        if (pressReleased) {
            pressTimer.reset();
            return true;
        } else {
            return false;
        }
    }

    public final boolean notPressed() {
        return !mousePressed;
    }
}
