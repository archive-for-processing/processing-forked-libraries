package processing.button;


public class MouseStateChange extends FixedMouseStateChange {

    protected boolean stateChangeLog[];

    public MouseStateChange() {
        super(false, false);
        this.stateChangeLog = new boolean[]{ false, false };
    }

    public MouseStateChange(boolean previousState, boolean currentState) {
        super(previousState, currentState);
        this.stateChangeLog = new boolean[]{ previousState, currentState };
    }

    public final void update(boolean record) {
        this.previousState = this.stateChangeLog[0] = this.stateChangeLog[1];
        this.currentState = this.stateChangeLog[1] = record;
    }

    public String toString() {
        return "PREVIOUS STATE: " + previousState + ", CURRENT STATE: " + currentState;
    }
}
