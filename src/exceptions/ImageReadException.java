package exceptions;

/**
 * Reports that an image file could not be read.
 */
public class ImageReadException extends ApplicationException {

    /**
     * Creates the read error for the given path.
     */
    public ImageReadException(String fileName) {
        super("Cannot read image: " + fileName);
    }
}
