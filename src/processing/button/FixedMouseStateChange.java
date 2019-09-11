package processing.button;

public class FixedMouseStateChange {

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
