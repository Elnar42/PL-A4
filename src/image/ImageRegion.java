package image;

import exceptions.InvalidImageSizeException;
import exceptions.InvalidRegionException;
import exceptions.InvalidSquareSizeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ImageRegion {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    /**
     * Rejects a negative origin or a non-positive width or height.
     */
    public ImageRegion(int x, int y, int width, int height) {
        if (x < 0 || y < 0) {
            throw new InvalidRegionException("Region origin cannot be negative.");
        }

        if (width <= 0 || height <= 0) {
            throw new InvalidRegionException("Region width and height must be positive.");
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the left edge of the region.
     */
    public int x() {
        return x;
    }

    /**
     * Returns the top edge of the region.
     */
    public int y() {
        return y;
    }

    /**
     * Returns the width of the region in pixels.
     */
    public int width() {
        return width;
    }

    /**
     * Returns the height of the region in pixels.
     */
    public int height() {
        return height;
    }

    /**
     * Splits the image into left-to-right, top-to-bottom squares, including smaller squares on the edges.
     */
    public static List<ImageRegion> forImage(int imageWidth, int imageHeight, int squareSize) {
        if (imageWidth <= 0 || imageHeight <= 0) {
            throw new InvalidImageSizeException("Image width and height must be positive.");
        }

        if (squareSize <= 0) {
            throw new InvalidSquareSizeException();
        }

        List<ImageRegion> regions = new ArrayList<ImageRegion>();

        for (int y = 0; y < imageHeight; ) {
            int height = Math.min(squareSize, imageHeight - y);

            for (int x = 0; x < imageWidth; ) {
                int width = Math.min(squareSize, imageWidth - x);
                regions.add(new ImageRegion(x, y, width, height));
                x += width;
            }

            y += height;
        }

        return Collections.unmodifiableList(regions);
    }
}
