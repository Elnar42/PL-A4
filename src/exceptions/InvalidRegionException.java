package exceptions;

/**
 * Reports that a square does not have a usable position or size.
 */
public class InvalidRegionException extends ApplicationException {

    /**
     * Stores why the square was rejected.
     */
    public InvalidRegionException(String message) {
        super(message);
    }
}
