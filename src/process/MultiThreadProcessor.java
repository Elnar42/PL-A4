package process;

import image.RegionListener;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MultiThreadProcessor extends ThreadProcessor {

    public static final MultiThreadProcessor INSTANCE = new MultiThreadProcessor();

    /**
     * Creates the multi-thread processor.
     */
    private MultiThreadProcessor() {
        super("multi-processor");
    }

    /**
     * Averages horizontal bands at the same time, one band per worker.
     */
    @Override
    protected void process(BufferedImage image, int squareSize, RegionListener listener) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int[] sourcePixels = image.getRGB(0, 0, width, height, null, 0, width);
        List<RowBand> bands = RowBand.split(width, height, squareSize);
        AtomicReference<RuntimeException> failure = new AtomicReference<>();
        List<Thread> workers = new ArrayList<>();

        for (int index = 0; index < bands.size(); index++) {
            RowBand band = bands.get(index);
            workers.add(new Thread(
                    () -> runBand(image, sourcePixels, band, listener, failure),
                    "image-band-" + index
            ));
        }

        for (Thread worker : workers) {
            worker.start();
        }

        try {
            for (Thread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException exception) {
            stop();
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Image processing was interrupted.");
        }

        if (failure.get() != null) {
            throw failure.get();
        }
    }

    /**
     * Averages one band and records the first failure.
     */
    private void runBand(
            BufferedImage image,
            int[] sourcePixels,
            RowBand band,
            RegionListener listener,
            AtomicReference<RuntimeException> failure
    ) {
        try {
            band.process(image, sourcePixels, listener, this::isStopped);
        } catch (RuntimeException exception) {
            stop();
            failure.compareAndSet(null, exception);
        }
    }
}
