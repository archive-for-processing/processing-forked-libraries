package processing.button;

import processing.core.PApplet;

public class Timer {
    
    private PApplet parent;
    private int startTime;

    public Timer(PApplet parent) {
        this.parent = parent;
    }

    public void start() {
        this.startTime = this.parent.millis();
    }

    public int elapsed() {
        return this.parent.millis() - this.startTime;
    }

    public void reset() {
        this.startTime = 0;
    }
}
