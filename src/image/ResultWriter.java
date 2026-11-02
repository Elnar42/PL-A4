package image;

import exceptions.ImageWriteException;
import exceptions.MissingImageException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResultWriter {

    public static final String FILE_NAME = "result.jpg";

    /**
     * Writes the image to result.jpg in the working directory.
     */
    public static void write(BufferedImage image) {
        if (image == null) {
            throw new MissingImageException();
        }

        try {
            if (!ImageIO.write(asJpeg(image), "jpg", new File(FILE_NAME))) {
                throw new ImageWriteException(FILE_NAME);
            }
        } catch (IOException exception) {
            throw new ImageWriteException(FILE_NAME);
        }
    }

    /**
     * Copies the image to an RGB picture that a JPEG writer can save.
     */
    private static BufferedImage asJpeg(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_RGB) {
            return image;
        }

        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return copy;
    }
}
