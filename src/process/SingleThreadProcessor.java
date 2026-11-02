package process;

import image.AverageColor;
import image.ImageRegion;
import image.RegionListener;

import java.awt.image.BufferedImage;

public class SingleThreadProcessor extends ThreadProcessor {

    public static final SingleThreadProcessor INSTANCE = new SingleThreadProcessor();

    private SingleThreadProcessor() {
        super("image-processor");
    }

    @Override
    protected void process(BufferedImage image, int squareSize, RegionListener listener) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        for (ImageRegion region : ImageRegion.forImage(
                image.getWidth(), image.getHeight(), squareSize)) {
            if (isStopped()) {
                return;
            }

            AverageColor.apply(image, region);

            if (listener != null) {
                listener.onRegionFinished(region);
            }
        }
    }
}