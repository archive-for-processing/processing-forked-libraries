package processing.button;


import processing.core.*;
//import processing.event.MouseEvent;


public abstract class Button implements PConstants {
    
    private PApplet parent;
    private Mouse mouse;

    protected boolean 
        enabled, hidden, 
        intialHover, stillHovered, 
        intialPress, stillPressed, 
        released;

    protected PShape shp;
    protected String txt;
    
    protected int 
        shpType, btnStrokeWeight,txtSize,
        BLACK, GREY, LIGHT_GREY, WHITE,
        btnStroke, btnFill, txtint,
        btnHltStroke, btnHltFill, btnHltText,
        btnDblStroke, btnDblFill, btnDblText,
        btnPressStroke, btnPressFill, btnPressText;
    
    public int 
        x, y, h, w,
        aX, aY;
        
    protected ButtonEventHandler btnEventHandler;
    

    public Button(PApplet parent, int shpType, String txt, int x, int y, int w, int h) {
        this.parent = parent;
        this.mouse = Mouse.INSTANCE;
        this.mouse.init(parent);
        
        this.enabled = true;
        this.hidden = false;

        this.shpType = shpType;
        this.txt = txt;
        this.txtSize = 12;          // default
        this.btnStrokeWeight = 1;   // default

        this.x = x; 
        this.y = y; 
        this.w = w; 
        this.h = h;

        this.aX = CENTER;
        this.aY = CENTER;

        this.BLACK = 0;
        this.GREY = 186;
        this.LIGHT_GREY = 230;
        this.WHITE = 255;

        this.setNormalStyle(this.BLACK, this.WHITE, this.BLACK);
        this.setHighlightStyle(this.BLACK, this.LIGHT_GREY, this.BLACK);
        this.setPressStyle(this.BLACK, this.GREY, this.BLACK);

        //this.disabledButtonEventHandler = new DisabledButtonEventHandler();
    }

    public final void hidden(boolean truth) {
        this.hidden = truth;
    }

    public final void enabled(boolean truth) {
        this.enabled = truth;
    }

    public final void draw() {  
        post();

        mouse.update();
        mouse.relativeTo(this);

        if (mouse.hoverStarted()) {
            this.intialHover = true;
            this.btnEventHandler.onHoverStart();
        } else if (mouse.isHovering()) {
            this.intialHover = false;
            this.stillHovered = true;
            this.btnEventHandler.whenHovering();
        } else if (mouse.hoverEnded()) {
            this.intialHover = false;
            this.stillHovered = false;
            this.btnEventHandler.onHoverEnd();
        }

        if (this.intialHover || this.stillHovered) {
            if (mouse.pressStarted()) {
                this.intialPress = true;
                this.btnEventHandler.onPressStart();
            } else if (mouse.isPressed()) {
                this.intialPress = false;
                this.stillPressed = true;
                this.btnEventHandler.whenPressed();
            } else if (mouse.pressReleased()) {
                this.intialPress = false;
                this.stillPressed = false;
                this.btnEventHandler.onPressRelease();
            }
        }
    }

    private final void post() {
        //if (this.hidden) return;

        int _btnStroke, _btnFill, _txtint;
        if (!this.enabled) {
            _btnStroke = this.btnDblStroke;
            _btnFill = this.btnDblFill;
            _txtint = this.btnDblText;
        } else if (this.intialPress) {
            _btnStroke = this.btnPressStroke;
            _btnFill = this.btnPressFill;
            _txtint = this.btnPressText;
        } else if (this.stillPressed) {
            _btnStroke = this.btnPressStroke;
            _btnFill = this.btnPressFill;
            _txtint = this.btnPressText;
        } else if (this.intialHover) {
            _btnStroke = this.btnHltStroke;
            _btnFill = this.btnHltFill;
            _txtint = this.btnHltText;
        } else if (this.stillHovered) {
            _btnStroke = this.btnHltStroke;
            _btnFill = this.btnHltFill;
            _txtint = this.btnHltText;
        } else {
            _btnStroke = this.btnStroke;
            _btnFill = this.btnFill;
            _txtint = this.txtint;
        }

        this.parent.strokeWeight(this.btnStrokeWeight);
        this.parent.stroke(_btnStroke);
        this.parent.fill(_btnFill);
        //this.shp = createShape(this.shpType, this.x, this.y, this.w, this.h);
        this.parent.shape(this.parent.createShape(this.shpType, this.x, this.y, this.w, this.h));

        this.parent.noStroke();
        this.parent.fill(_txtint);
        this.parent.textAlign(this.aX, this.aY);
        this.parent.textSize(this.txtSize);
        this.parent.text(this.txt, this.x, this.y, this.w, this.h);
    }

    public final void setEventHandler(ButtonEventHandler btnEventHandler) {
        this.btnEventHandler = btnEventHandler;
    }

    public final void align(int aX, int aY) {
        if (aX == CENTER) this.x = (this.parent.width / 2) - (this.w / 2);
        this.x = aX;
        this.y = aY;
    }

    public final void alignText(int aX, int aY) {
        this.aX = aX;
        this.aY = aY;
    }

    public final void setNormalStyle(int btnStroke, int btnFill, int txtint) {
        this.btnStroke = btnStroke;
        this.btnFill = btnFill;
        this.txtint = txtint;
    }

    public final void setHighlightStyle(int btnHltStroke, int btnHltFill, int btnHltText) {
        this.btnHltStroke = btnHltStroke;
        this.btnHltFill = btnHltFill;
        this.btnHltText = btnHltText;
    }

    public final void setPressStyle(int btnStroke, int btnFill, int txtint) {
        this.btnPressStroke = btnStroke;
        this.btnPressFill = btnFill;
        this.btnPressText = txtint;
    }

    public final void setTextSize(int txtSize) {
        this.txtSize = txtSize;
    }
    
}




