package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    /**
     * Reads an image file and rejects a missing file or a file that is not an image.
     */
    public static BufferedImage load(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file: " + fileName);
        }

        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                throw new IllegalArgumentException("Not an image: " + fileName);
            }
            return image;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read image: " + fileName);
        }
    }
}
