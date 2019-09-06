package button.library;


class FixedMouseStateChange {

    protected boolean previousState, currentState;

    public FixedMouseStateChange(boolean previousState, boolean currentState) {
        this.previousState = previousState;
        this.currentState = currentState;
    }

    public final boolean equals(FixedMouseStateChange otherFMSC) {
        return (this.previousState == otherFMSC.previousState &&
            this.currentState == otherFMSC.currentState);
    }
}


class MouseStateChange extends FixedMouseStateChange {

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
