package exceptions;

/**
 * Reports that the mode letter is not S or M.
 */
public class InvalidModeException extends ApplicationException {

    /**
     * Creates the invalid-mode error.
     */
    public InvalidModeException() {
        super("Enter S or M.");
    }
}
