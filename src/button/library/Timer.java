package button.library;


class Timer {
    
    private int startTime;

    public Timer() {
    }

    public void start() {
        this.startTime = millis();
    }

    public int elapsed() {
        return millis() - this.startTime;
    }

    public void reset() {
        this.startTime = 0;
    }
}
