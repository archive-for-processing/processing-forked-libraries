package button.library;


import processing.core.*;


Mouse mouse = new Mouse();

abstract class Button {

    protected boolean enabled, hidden, 
        intialHover, stillHovered, 
        intialPress, stillPressed, released;

    protected int shpType;
    protected PShape shp;
    protected String txt;
    protected int txtSize;
    protected int btnStrokeWeight;

    public int x, y, h, w;

    protected int aX, aY;

    protected color BLACK, GREY, LIGHT_GREY, WHITE;
    protected color btnStroke, btnFill, txtColor;
    protected color btnHltStroke, btnHltFill, btnHltText;
    protected color btnDblStroke, btnDblFill, btnDblText;
    protected color btnPressStroke, btnPressFill, btnPressText;

    protected ButtonEventHandler btnEventHandler;

    public Button(int shpType, String txt, int x, int y, int w, int h) {
        this.enabled = true;
        this.hidden = false;

        this.shpType = shpType;
        this.txt = txt;
        this.txtSize = 12; // default
        this.btnStrokeWeight = 1; // default

        this.x = x; 
        this.y = y; 
        this.w = w; 
        this.h = h;

        this.aX = CENTER;
        this.aY = CENTER;

        this.BLACK = color(0);
        this.GREY = color(186, 186, 186);
        this.LIGHT_GREY = color(230, 230, 230);
        this.WHITE = color(255);

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

        color _btnStroke, _btnFill, _txtColor;
        if (!this.enabled) {
            _btnStroke = this.btnDblStroke;
            _btnFill = this.btnDblFill;
            _txtColor = this.btnDblText;
        } else if (this.intialPress) {
            _btnStroke = this.btnPressStroke;
            _btnFill = this.btnPressFill;
            _txtColor = this.btnPressText;
        } else if (this.stillPressed) {
            _btnStroke = this.btnPressStroke;
            _btnFill = this.btnPressFill;
            _txtColor = this.btnPressText;
        } else if (this.intialHover) {
            _btnStroke = this.btnHltStroke;
            _btnFill = this.btnHltFill;
            _txtColor = this.btnHltText;
        } else if (this.stillHovered) {
            _btnStroke = this.btnHltStroke;
            _btnFill = this.btnHltFill;
            _txtColor = this.btnHltText;
        } else {
            _btnStroke = this.btnStroke;
            _btnFill = this.btnFill;
            _txtColor = this.txtColor;
        }

        strokeWeight(this.btnStrokeWeight);
        stroke(_btnStroke);
        fill(_btnFill);
        //this.shp = createShape(this.shpType, this.x, this.y, this.w, this.h);
        shape(createShape(this.shpType, this.x, this.y, this.w, this.h));

        noStroke();
        fill(_txtColor);
        textAlign(this.aX, this.aY);
        textSize(this.txtSize);
        text(this.txt, this.x, this.y, this.w, this.h);
    }

    public final void setEventHandler(ButtonEventHandler btnEventHandler) {
        this.btnEventHandler = btnEventHandler;
    }

    public final void align(int aX, int aY) {
        if (aX == CENTER) this.x = (width / 2) - (this.w / 2);
        this.x = aX;
        this.y = aY;
    }

    public final void alignText(int aX, int aY) {
        this.aX = aX;
        this.aY = aY;
    }

    public final void setNormalStyle(color btnStroke, color btnFill, color txtColor) {
        this.btnStroke = btnStroke;
        this.btnFill = btnFill;
        this.txtColor = txtColor;
    }

    public final void setHighlightStyle(color btnHltStroke, color btnHltFill, color btnHltText) {
        this.btnHltStroke = btnHltStroke;
        this.btnHltFill = btnHltFill;
        this.btnHltText = btnHltText;
    }

    public final void setPressStyle(color btnStroke, color btnFill, color txtColor) {
        this.btnPressStroke = btnStroke;
        this.btnPressFill = btnFill;
        this.btnPressText = txtColor;
    }

    public final void setTextSize(int txtSize) {
        this.txtSize = txtSize;
    }
}

class RectButton extends Button {

    public RectButton(String txt, int x, int y, int w, int h) {
        super(RECT, txt, x, y, w, h);
    }
}

class EllipticalButton extends Button {

    public EllipticalButton(String txt, int x, int y, int w, int h) {
        super(ELLIPSE, txt, x, y, w, h);
    }
}
