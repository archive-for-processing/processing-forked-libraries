import processing.button.*;

Mouse mouse;
Button myButton, b2;

void setup() {
    //frameRate(fps); // <- Need to slow things down?
    size(400, 300);
    background(255);

    int w = 100, h = 30;
    int x = -(w/2), y = (height/4); // 

    myButton = new RectButton(this, "click me", x, y, w, h);
    myButton.setEventHandler(new myButtonEventHandler());
    
    b2 = new RectButton(this, "click me2", x + 50, y, w, h);
    b2.setEventHandler(new myButtonEventHandler());
    
    mouse = Mouse.INSTANCE;
}

void draw() {
    background(255);
    text("fps: " + round(frameRate), 40, 20);

    translate(width/2, height/2);
    mouse.translate(width/2, height/2); // have to make mouse aware of translation

    myButton.draw();
}


class myButtonEventHandler extends ButtonEventHandler {
    /*
    * All available events implemented below,
     * however not necessary.
     */

    public void onHoverStart() {
        print("\nHOVERING STARTED  | ");
        text("HOVERING STARTED", 75, -60);
    }

    public void whenHovering() {
        print("\nSTILL HOVERING... | ");
        text("STILL HOVERING...", 75, -30);
    }

    public void onHoverEnd() {
        print("\nHOVERING ENDED    | ");
        text("HOVERING ENDED", 75, 0);
    }

    public void onPressStart() { 
        print("PRESS STARTED");
        text("PRESS STARTED", -75, -60);
    }

    public void whenPressed() { 
        print("STILL PRESSED...");
        text("STILL PRESSED...", -75, -30);
    }

    public void onPressRelease() {
        print("PRESS RELEASED");
        text("PRESS RELEASED", -75, 0);
    }
}
