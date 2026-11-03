package process;

import exceptions.MissingImageException;
import image.AverageColor;
import image.ImageRegion;
import image.RegionListener;

import java.awt.image.BufferedImage;

public class SingleThreadProcessor extends ThreadProcessor {

    public static final SingleThreadProcessor INSTANCE = new SingleThreadProcessor();

    /**
     * Creates the single-thread processor.
     */
    private SingleThreadProcessor() {
        super("image-processor");
    }

    /**
     * Averages each square from left to right and top to bottom on this thread.
     */
    @Override
    protected void process(BufferedImage image, int squareSize, RegionListener listener) {
        if (image == null) {
            throw new MissingImageException();
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int[] sourcePixels = image.getRGB(0, 0, width, height, null, 0, width);

        for (ImageRegion region : ImageRegion.forImage(width, height, squareSize)) {
            if (isStopped()) {
                return;
            }

            AverageColor color = AverageColor.extract(sourcePixels, width, height, region);

            if (listener != null) {
                listener.onRegionFinished(region, color);
            } else {
                color.paint(image, region);
            }
        }
    }
}
