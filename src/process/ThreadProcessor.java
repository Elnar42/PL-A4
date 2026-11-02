package process;

import image.RegionListener;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ThreadProcessor {

    private final String name;
    private volatile boolean stopped;

    /**
     * Stores the name of the background thread.
     */
    protected ThreadProcessor(String name) {
        this.name = name;
    }

    /**
     * Asks processing to finish the current square and then stop.
     */
    public void stop() {
        stopped = true;
    }

    /**
     * Reports whether a stop has been requested.
     */
    protected boolean isStopped() {
        return stopped;
    }

    /**
     * Runs process on a background thread and waits until that thread finishes.
     */
    public final void run(BufferedImage image, int squareSize, RegionListener listener) throws InterruptedException {
        if (stopped) {
            return;
        }

        AtomicReference<RuntimeException> failure = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                process(image, squareSize, listener);
            } catch (RuntimeException exception) {
                failure.set(exception);
            }
        }, name);

        thread.start();
        thread.join();

        if (failure.get() != null) {
            throw failure.get();
        }
    }

    /**
     * Averages every square of the image.
     */
    protected abstract void process(BufferedImage image, int squareSize, RegionListener listener);
}
