import java.util.ArrayList;
import java.util.List;


public record ImageRegion(int x, int y, int width, int height) {

    public ImageRegion {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Region origin cannot be negative.");
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Region width and height must be positive.");
        }
    }

    public static List<ImageRegion> forImage(int imageWidth, int imageHeight, int squareSize) {
        if (imageWidth <= 0 || imageHeight <= 0) {
            throw new IllegalArgumentException("Image width and height must be positive.");
        }

        if (squareSize <= 0) {
            throw new IllegalArgumentException("Square size must be a positive integer.");
        }

        List<ImageRegion> regions = new ArrayList<>();

        for (int y = 0; y < imageHeight; ) {
            int height = Math.min(squareSize, imageHeight - y);

            for (int x = 0; x < imageWidth; ) {
                int width = Math.min(squareSize, imageWidth - x);
                regions.add(new ImageRegion(x, y, width, height));
                x += width;
            }

            y += height;
        }

        return List.copyOf(regions);
    }
}
