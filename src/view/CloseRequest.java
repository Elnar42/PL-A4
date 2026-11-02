package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class CloseRequest extends WindowAdapter {

    private volatile Runnable action = () -> {
    };

    void setAction(Runnable action) {
        this.action = action == null ? () -> {
        } : action;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        action.run();
    }
}
