package exceptions;

/**
 * Reports that image processing was interrupted before it finished.
 */
public class ProcessingInterruptedException extends ApplicationException {

    /**
     * Creates the interrupted-processing error.
     */
    public ProcessingInterruptedException() {
        super("Image processing was interrupted.");
    }
}
