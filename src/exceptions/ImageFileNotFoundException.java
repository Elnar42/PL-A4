package exceptions;

/**
 * Reports that the named image file does not exist.
 */
public class ImageFileNotFoundException extends ApplicationException {

    /**
     * Creates the file-not-found error for the given path.
     */
    public ImageFileNotFoundException(String fileName) {
        super("File not found: " + fileName);
    }
}
