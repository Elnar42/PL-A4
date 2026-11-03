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
     * Writes the image to result.jpg and returns the full path of that file.
     */
    public static String write(BufferedImage image) {
        if (image == null) {
            throw new MissingImageException();
        }

        File file = new File(FILE_NAME).getAbsoluteFile();
        try {
            if (!ImageIO.write(asJpeg(image), "jpg", file)) {
                throw new ImageWriteException(file.getAbsolutePath());
            }
        } catch (IOException exception) {
            throw new ImageWriteException(file.getAbsolutePath());
        }
        return file.getAbsolutePath();
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
