package exceptions;

/**
 * Reports that the file cannot be read as an image.
 */
public class NotAnImageException extends ApplicationException {

    /**
     * Creates the not-an-image error for the given path.
     */
    public NotAnImageException(String fileName) {
        super("Not an image: " + fileName);
    }
}
