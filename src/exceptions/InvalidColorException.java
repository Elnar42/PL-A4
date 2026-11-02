package exceptions;

/**
 * Reports that a color channel is outside 0 to 255.
 */
public class InvalidColorException extends ApplicationException {

    /**
     * Creates the invalid-color error.
     */
    public InvalidColorException() {
        super("Color channels must be between 0 and 255.");
    }
}
