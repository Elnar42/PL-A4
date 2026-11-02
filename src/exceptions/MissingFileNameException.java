package exceptions;

/**
 * Reports that the image file name was required and none was given.
 */
public class MissingFileNameException extends ApplicationException {

    /**
     * Creates the missing-file-name error.
     */
    public MissingFileNameException() {
        super("File name is missing.");
    }
}
