package exceptions;

/**
 * Reports that the averaged image could not be saved.
 */
public class ImageWriteException extends ApplicationException {

    /**
     * Creates the write error for the given path.
     */
    public ImageWriteException(String fileName) {
        super("Cannot write image: " + fileName);
    }
}
