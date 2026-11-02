package window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CloseRequest extends WindowAdapter {

    private volatile Runnable action = () -> {
    };

    /**
     * Sets the action that runs when the window is closing.
     */
    public void setAction(Runnable action) {
        this.action = action == null ? () -> {
        } : action;
    }

    /**
     * Runs the close action.
     */
    @Override
    public void windowClosing(WindowEvent event) {
        action.run();
    }
}
