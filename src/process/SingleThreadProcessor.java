package process;

import image.AverageColor;
import image.ImageRegion;

import java.awt.image.BufferedImage;

public class SingleThreadProcessor extends ThreadProcessor {

    public static final SingleThreadProcessor INSTANCE = new SingleThreadProcessor();

    private SingleThreadProcessor() {
    }

    @Override
    public void process(BufferedImage image, int squareSize) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        for (ImageRegion region : ImageRegion.forImage(
                image.getWidth(), image.getHeight(), squareSize)) {
            AverageColor.apply(image, region);
        }
    }
}