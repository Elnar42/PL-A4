package exceptions;

/**
 * Reports that the pixel buffer was required and none was given.
 */
public class MissingPixelsException extends ApplicationException {

    /**
     * Creates the missing-pixels error.
     */
    public MissingPixelsException() {
        super("Pixels are missing.");
    }
}
