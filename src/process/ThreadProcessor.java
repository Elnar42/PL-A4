package process;

import java.awt.image.BufferedImage;

public abstract class ThreadProcessor {
    public abstract void process(BufferedImage image, int squareSize);
}

