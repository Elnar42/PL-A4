package image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageLoader {

    public static BufferedImage load(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file: " + fileName);
        }

        try (ImageInputStream input = ImageIO.createImageInputStream(file)) {
            if (input == null) {
                throw new IllegalArgumentException("Not an image: " + fileName);
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Not an image: " + fileName);
            }

            ImageReader reader = readers.next();
            try {
                reader.setInput(input);
                String format = reader.getFormatName();
                if (format == null || !format.equalsIgnoreCase("JPEG")) {
                    String name = format == null ? "unknown" : format;
                    throw new IllegalArgumentException(
                            "Unsupported file format: " + name + ". A JPEG file is required: " + fileName
                    );
                }

                return reader.read(0);
            } catch (IOException exception) {
                throw new IllegalArgumentException("Cannot read image: " + fileName);
            } finally {
                reader.dispose();
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Not an image: " + fileName);
        }
    }
}
