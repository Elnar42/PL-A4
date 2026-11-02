package worker;

import exceptions.InvalidImageSizeException;
import exceptions.InvalidSquareSizeException;

public final class WorkerCount {

    /**
     * Prevents instances of this utility class.
     */
    private WorkerCount() {
    }

    /**
     * Returns the number of CPU cores, at least one.
     */
    public static int fromCores() {
        return Math.max(1, Runtime.getRuntime().availableProcessors());
    }

    /**
     * Returns how many workers to use, limited by the number of square rows.
     */
    public static int forImage(int imageHeight, int squareSize) {
        if (imageHeight <= 0) {
            throw new InvalidImageSizeException("Image height must be positive.");
        }

        if (squareSize <= 0) {
            throw new InvalidSquareSizeException();
        }

        int rows = (int) Math.ceil(imageHeight / (double) squareSize);
        return Math.min(fromCores(), rows);
    }
}
