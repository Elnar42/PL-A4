package exceptions;

/**
 * Reports that the window status text was required and none was given.
 */
public class MissingSummaryException extends ApplicationException {

    /**
     * Creates the missing-summary error.
     */
    public MissingSummaryException() {
        super("Summary is missing.");
    }
}
