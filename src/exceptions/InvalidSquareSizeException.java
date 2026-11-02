package exceptions;

/**
 * Reports that the square size is not a positive whole number.
 */
public class InvalidSquareSizeException extends ApplicationException {

    /**
     * Creates the invalid-square-size error.
     */
    public InvalidSquareSizeException() {
        super("Square size must be a positive integer.");
    }
}
