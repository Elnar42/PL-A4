package exceptions;

/**
 * Reports a problem with the arguments, the image, or the window.
 */
public class ApplicationException extends RuntimeException {

    /**
     * Stores the message shown to the user.
     */
    public ApplicationException(String message) {
        super(message);
    }
}
