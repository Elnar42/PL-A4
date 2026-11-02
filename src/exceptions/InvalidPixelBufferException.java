package exceptions;

/**
 * Reports that a pixel buffer does not cover the image.
 */
public class InvalidPixelBufferException extends ApplicationException {

    /**
     * Creates the invalid-pixel-buffer error.
     */
    public InvalidPixelBufferException() {
        super("Pixel buffer does not match the image.");
    }
}
