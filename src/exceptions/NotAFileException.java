package exceptions;

/**
 * Reports that the path exists but is not a file.
 */
public class NotAFileException extends ApplicationException {

    /**
     * Creates the not-a-file error for the given path.
     */
    public NotAFileException(String fileName) {
        super("Not a file: " + fileName);
    }
}
