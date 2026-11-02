package exceptions;

/**
 * Reports that a square was required and none was given.
 */
public class MissingRegionException extends ApplicationException {

    /**
     * Creates the missing-region error.
     */
    public MissingRegionException() {
        super("Region is missing.");
    }
}
