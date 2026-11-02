package exceptions;

/**
 * Reports that the window could not be shown or updated.
 */
public class WindowException extends ApplicationException {

    /**
     * Stores why the window operation failed.
     */
    public WindowException(String message) {
        super(message);
    }
}
