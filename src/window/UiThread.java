package window;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public final class UiThread {

    /**
     * Prevents instances of this utility class.
     */
    private UiThread() {
    }

    /**
     * Runs the action on the Swing thread and waits until it finishes.
     */
    public static void run(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) {
            action.run();
            return;
        }

        try {
            SwingUtilities.invokeAndWait(action);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not update the window.");
        } catch (InvocationTargetException exception) {
            throw new IllegalStateException("Could not update the window.");
        }
    }
}
