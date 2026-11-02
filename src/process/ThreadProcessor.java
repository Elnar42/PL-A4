package process;

import image.RegionListener;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ThreadProcessor {

    private final String name;

    protected ThreadProcessor(String name) {
        this.name = name;
    }

    public final void run(BufferedImage image, int squareSize, RegionListener listener) throws InterruptedException {
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

    protected abstract void process(BufferedImage image, int squareSize, RegionListener listener);
}
