package cli;

public final class Arguments {

    private final String fileName;
    private final int squareSize;
    private final Mode mode;

    /**
     * Rejects a blank file name, a non-positive square size, or a missing mode.
     */
    public Arguments(String fileName, int squareSize, Mode mode) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is missing.");
        }

        if (squareSize <= 0) {
            throw new IllegalArgumentException("Square size must be a positive integer.");
        }

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null.");
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
