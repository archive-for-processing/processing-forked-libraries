package processing.button;


import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.lang.reflect.Method;

public abstract class ButtonEventHandler {

    public ButtonEventHandler() {
        String[] builtins = { "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll" };
        String[] events = { "whenHovering", "onPressRelease", "onHoverStart", "whenNotPressed", 
            "whenPressed", "whenNotHovering", "onHoverEnd", "onPressStart" };

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
                "ButtonEventHandler is only overrideable. Following methods not allowed <" + allMethods + ">");
        }
    }


    public void onHoverStart() {
    }

    public void whenHovering() {
    }

    public void onHoverEnd() {
    }

    public void whenNotHovering() {
    }

    // ------------------------------------------------------------------------------------

    public void onPressStart() {
    }

    public void whenPressed() {
    }

    public void onPressRelease() {
    }

    public void whenNotPressed() {
    }
}

final class DisabledButtonEventHandler extends ButtonEventHandler {
}
