package processing.button;

public final class MouseState {
    public static FixedMouseStateChange 
            hoverStart = new FixedMouseStateChange(false, true),
            hovering = new FixedMouseStateChange(true, true),
            hoverEnd = new FixedMouseStateChange(true, false),
            notPressed = new FixedMouseStateChange(false, false),
            pressStart = new FixedMouseStateChange(false, true),
            pressed = new FixedMouseStateChange(true, true),
            released = new FixedMouseStateChange(true, false);
}
