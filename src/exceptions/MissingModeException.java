package exceptions;

/**
 * Reports that the processing mode was required and none was given.
 */
public class MissingModeException extends ApplicationException {

    /**
     * Creates the missing-mode error.
     */
    public MissingModeException() {
        super("Mode cannot be null.");
    }
}
