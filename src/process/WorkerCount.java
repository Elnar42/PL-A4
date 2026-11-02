package process;

public final class WorkerCount {

    private WorkerCount() {
    }

    public static int fromCores() {
        return Math.max(1, Runtime.getRuntime().availableProcessors());
    }

    public static int forImage(int imageHeight, int squareSize) {
        if (imageHeight <= 0) {
            throw new IllegalArgumentException("Image height must be positive.");
        }

        if (squareSize <= 0) {
            throw new IllegalArgumentException("Square size must be a positive integer.");
        }

        int rows = (int) Math.ceil(imageHeight / (double) squareSize);
        return Math.min(fromCores(), rows);
    }
}
