package cli;

import exceptions.InvalidSquareSizeException;
import exceptions.MissingFileNameException;
import exceptions.MissingModeException;

public final class Arguments {

    private final String fileName;
    private final int squareSize;
    private final Mode mode;

    /**
     * Rejects a blank file name, a non-positive square size, or a missing mode.
     */
    public Arguments(String fileName, int squareSize, Mode mode) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new MissingFileNameException();
        }

        if (squareSize <= 0) {
            throw new InvalidSquareSizeException();
        }

        if (mode == null) {
            throw new MissingModeException();
        }

        this.fileName = fileName;
        this.squareSize = squareSize;
        this.mode = mode;
    }

    /**
     * Returns the image file name.
     */
    public String fileName() {
        return fileName;
    }

    /**
     * Returns the side length of each square.
     */
    public int squareSize() {
        return squareSize;
    }

    /**
     * Returns the single-thread or multi-thread mode.
     */
    public Mode mode() {
        return mode;
    }
}
