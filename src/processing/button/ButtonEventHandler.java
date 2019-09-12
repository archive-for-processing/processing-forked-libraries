package processing.button;


import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.lang.reflect.Method;

public abstract class ButtonEventHandler {

    public ButtonEventHandler() {
        String[] builtins = { "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll" };
        String[] events = { "onPressStart", "whenPressed", "onPressRelease", "onHoverStart", "whenHovering",
                "onHoverEnd" };

        final Set<String> allowedMethodNames = new HashSet<String>();
        allowedMethodNames.addAll(Arrays.asList(builtins));
        allowedMethodNames.addAll(Arrays.asList(events));

        Set<String> allMethods = new HashSet<String>();
        for (Method aMethod : getClass().getMethods()) {
            allMethods.add(aMethod.getName());
        }
        if (!allowedMethodNames.equals(allMethods)) {
            allMethods.removeAll(allowedMethodNames);
            throw new IllegalStateException(
                "methods of class ButtonEventHandler are only overrideable."
                + "\nfollowing methods not allowed:"
                + "\n\t<" + allMethods + ">"
                + "\navailable methods to override:"
                + "\n\t" + "<" + Arrays.asList(events) + ">");
        }
    }


    public void onHoverStart() {
    }

    public void whenHovering() {
    }

    public void onHoverEnd() {
    }

    // ------------------------------------------------------------------------------------

    public void onPressStart() {
    }

    public void whenPressed() {
    }

    public void onPressRelease() {
    }

}

final class DisabledButtonEventHandler extends ButtonEventHandler {
}
