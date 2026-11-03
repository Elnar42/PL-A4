package view;

import worker.WorkerCount;

final class PaintPace {

    private static final int MILLIS_PER_SQUARE = 40;
    private static final int BAND_LIMIT_MILLIS = 6000;

    private final int millis;

    /**
     * Stores how long to wait after one square.
     */
    private PaintPace(int millis) {
        this.millis = millis;
    }

    /**
     * Chooses a pause that keeps each square visible and still lets the bands finish together.
     */
    static PaintPace forImage(int imageWidth, int imageHeight, int squareSize) {
        int squares = squareCount(imageWidth, imageHeight, squareSize);
        int workers = WorkerCount.forImage(imageHeight, squareSize);
        int perWorker = (squares + workers - 1) / workers;
        int pause = MILLIS_PER_SQUARE;

        if ((long) pause * perWorker > BAND_LIMIT_MILLIS) {
            pause = (int) (BAND_LIMIT_MILLIS / perWorker);
            if (pause < 8) {
                pause = 8;
            }
        }

        return new PaintPace(pause);
    }

    /**
     * Waits so the square just painted can be seen before the next one.
     */
    void pause() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Counts the squares that cover the image, including the partial squares on the edges.
     */
    private static int squareCount(int imageWidth, int imageHeight, int squareSize) {
        int columns = (imageWidth + squareSize - 1) / squareSize;
        int rows = (imageHeight + squareSize - 1) / squareSize;
        return Math.max(1, columns * rows);
    }
}
