package view;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

class UiThread {

    private UiThread() {
    }

    static void run(Runnable action) {
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
