package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class AverageColor {

    private final int red;
    private final int green;
    private final int blue;

    public AverageColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Color channels must be between 0 and 255.");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public static void apply(BufferedImage image, ImageRegion region) {
        int rgb = extractAverageColour(image, region).getRgb();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                image.setRGB(x, y, rgb);
            }
        }
    }

    public static AverageColor extractAverageColour(BufferedImage image, ImageRegion region) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        if (region == null) {
            throw new IllegalArgumentException("Region is missing.");
        }

        if (region.x() + region.width() > image.getWidth()
                || region.y() + region.height() > image.getHeight()) {
            throw new IllegalArgumentException("Region does not fit inside the image.");
        }

        long red = 0;
        long green = 0;
        long blue = 0;
        long count = (long) region.width() * region.height();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                Color color = new Color(image.getRGB(x, y));
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
            }
        }

        return new AverageColor(
                channel(red, count),
                channel(green, count),
                channel(blue, count)
        );
    }


    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getRgb() {
        return new Color(red, green, blue).getRGB();
    }

    private static int channel(long sum, long count) {
        return (int) ((sum + count / 2) / count);
    }
}
