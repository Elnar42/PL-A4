package exceptions;

/**
 * Reports that an image width or height cannot be used.
 */
public class InvalidImageSizeException extends ApplicationException {

    /**
     * Stores why the image size was rejected.
     */
    public InvalidImageSizeException(String message) {
        super(message);
    }
}
