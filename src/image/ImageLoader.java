package image;

import exceptions.ImageFileNotFoundException;
import exceptions.ImageReadException;
import exceptions.NotAFileException;
import exceptions.NotAnImageException;

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
            throw new ImageFileNotFoundException(fileName);
        }

        if (!file.isFile()) {
            throw new NotAFileException(fileName);
        }

        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                throw new NotAnImageException(fileName);
            }
            return image;
        } catch (IOException exception) {
            throw new ImageReadException(fileName);
        }
    }
}
