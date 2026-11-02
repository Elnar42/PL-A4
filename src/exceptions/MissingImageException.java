package exceptions;

/**
 * Reports that an image was required and none was given.
 */
public class MissingImageException extends ApplicationException {

    /**
     * Creates the missing-image error.
     */
    public MissingImageException() {
        super("Image is missing.");
    }
}
