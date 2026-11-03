package band;

import exceptions.MissingImageException;
import exceptions.MissingPixelsException;
import image.AverageColor;
import image.ImageRegion;
import image.RegionListener;
import worker.WorkerCount;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * This band represents the row of the image that each processor takes in multithreading
 */
public final class RowBand {

    private final List<ImageRegion> regions;

    /**
     * Stores the squares that belong to one band.
     */
    private RowBand(List<ImageRegion> regions) {
        this.regions = Collections.unmodifiableList(new ArrayList<>(regions));
    }

    /**
     * Averages each square in this band until a stop is requested.
     */
    public void process(BufferedImage image, int[] sourcePixels, RegionListener listener, BooleanSupplier stopped) {
        if (image == null) {
            throw new MissingImageException();
        }

        if (sourcePixels == null) {
            throw new MissingPixelsException();
        }

        for (ImageRegion region : regions) {
            if (stopped.getAsBoolean()) {
                return;
            }

            AverageColor color = AverageColor.extract(
                    sourcePixels,
                    image.getWidth(),
                    image.getHeight(),
                    region
            );
            finish(image, region, color, listener);
        }
    }

    /**
     * Paints a finished square through the listener, or directly when there is no listener.
     */
    private static void finish(
            BufferedImage image,
            ImageRegion region,
            AverageColor color,
            RegionListener listener
    ) {
        if (listener != null) {
            listener.onRegionFinished(region, color);
        } else {
            color.paint(image, region);
        }
    }

    /**
     * Divides the square rows into contiguous bands, one per worker.
     */
    public static List<RowBand> split(int imageWidth, int imageHeight, int squareSize) {
        List<List<ImageRegion>> rows = rowsOf(ImageRegion.forImage(imageWidth, imageHeight, squareSize));
        int workers = WorkerCount.forImage(imageHeight, squareSize);
        int base = rows.size() / workers;
        int extra = rows.size() % workers;

        List<RowBand> bands = new ArrayList<RowBand>();
        int index = 0;

        for (int worker = 0; worker < workers; worker++) {
            int rowCount = base + (worker < extra ? 1 : 0);
            List<ImageRegion> band = new ArrayList<ImageRegion>();

            for (int row = 0; row < rowCount; row++) {
                band.addAll(rows.get(index));
                index++;
            }

            bands.add(new RowBand(band));
        }

        return Collections.unmodifiableList(bands);
    }

    /**
     * Groups squares that share the same top edge into rows.
     */
    private static List<List<ImageRegion>> rowsOf(List<ImageRegion> regions) {
        List<List<ImageRegion>> rows = new ArrayList<>();
        List<ImageRegion> row = new ArrayList<ImageRegion>();
        int y = regions.get(0).y();

        for (ImageRegion region : regions) {
            if (region.y() != y) {
                rows.add(Collections.unmodifiableList(new ArrayList<>(row)));
                row = new ArrayList<>();
                y = region.y();
            }

            row.add(region);
        }

        rows.add(Collections.unmodifiableList(new ArrayList<>(row)));
        return rows;
    }
}
