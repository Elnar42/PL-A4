package image;

import exceptions.InvalidColorException;
import exceptions.InvalidPixelBufferException;
import exceptions.InvalidRegionException;
import exceptions.MissingImageException;
import exceptions.MissingPixelsException;
import exceptions.MissingRegionException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.IntBinaryOperator;

public final class AverageColor {

    private final int red;
    private final int green;
    private final int blue;

    /**
     * Rejects a color channel outside 0 to 255.
     */
    public AverageColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new InvalidColorException();
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Returns the red channel.
     */
    public int red() {
        return red;
    }

    /**
     * Returns the green channel.
     */
    public int green() {
        return green;
    }

    /**
     * Returns the blue channel.
     */
    public int blue() {
        return blue;
    }

    /**
     * Fills the region with this color.
     */
    public void paint(BufferedImage image, ImageRegion region) {
        if (image == null) {
            throw new MissingImageException();
        }

        if (region == null) {
            throw new MissingRegionException();
        }

        int rgb = getRgb();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                image.setRGB(x, y, rgb);
            }
        }
    }

    /**
     * Returns the average color of a region in the image.
     */
    public static AverageColor extract(BufferedImage image, ImageRegion region) {
        if (image == null) {
            throw new MissingImageException();
        }

        if (region == null) {
            throw new MissingRegionException();
        }

        return average(image.getWidth(), image.getHeight(), region, image::getRGB);
    }

    /**
     * Returns the average color of a region in a pixel buffer.
     */
    public static AverageColor extract(int[] pixels, int imageWidth, int imageHeight, ImageRegion region) {
        if (pixels == null) {
            throw new MissingPixelsException();
        }

        if (imageWidth <= 0 || imageHeight <= 0 || pixels.length < (long) imageWidth * imageHeight) {
            throw new InvalidPixelBufferException();
        }

        return average(imageWidth, imageHeight, region, (x, y) -> pixels[y * imageWidth + x]);
    }

    /**
     * Computes the per-channel mean of the pixels in a region.
     */
    private static AverageColor average(
            int imageWidth,
            int imageHeight,
            ImageRegion region,
            IntBinaryOperator rgbAt
    ) {
        if (region == null) {
            throw new MissingRegionException();
        }

        if (region.x() + region.width() > imageWidth || region.y() + region.height() > imageHeight) {
            throw new InvalidRegionException("Region does not fit inside the image.");
        }

        long redSum = 0;
        long greenSum = 0;
        long blueSum = 0;
        long count = (long) region.width() * region.height();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                Color color = new Color(rgbAt.applyAsInt(x, y));
                redSum += color.getRed();
                greenSum += color.getGreen();
                blueSum += color.getBlue();
            }
        }

        return new AverageColor(
                channel(redSum, count),
                channel(greenSum, count),
                channel(blueSum, count)
        );
    }

    /**
     * Returns this color as a packed RGB value.
     */
    public int getRgb() {
        return new Color(red, green, blue).getRGB();
    }

    /**
     * Rounds a channel sum to the nearest whole number.
     */
    private static int channel(long sum, long count) {
        return (int) ((sum + count / 2) / count);
    }
}
