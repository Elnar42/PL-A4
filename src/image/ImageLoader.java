package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    public static BufferedImage load(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("Cannot read image: " + fileName);
        }

        try {
            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                throw new IllegalArgumentException("Cannot read image: " + fileName);
            }

            return image;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read image: " + fileName);
        }
    }
}
