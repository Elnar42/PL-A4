package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.IntBinaryOperator;

public record AverageColor(int red, int green, int blue) {

    public AverageColor {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Color channels must be between 0 and 255.");
        }

    }

    public void paint(BufferedImage image, ImageRegion region) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        if (region == null) {
            throw new IllegalArgumentException("Region is missing.");
        }

        int rgb = getRgb();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                image.setRGB(x, y, rgb);
            }
        }
    }

    public static AverageColor extract(BufferedImage image, ImageRegion region) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        if (region == null) {
            throw new IllegalArgumentException("Region is missing.");
        }

        return average(image.getWidth(), image.getHeight(), region, image::getRGB);
    }

    public static AverageColor extract(int[] pixels, int imageWidth, int imageHeight, ImageRegion region) {
        if (pixels == null) {
            throw new IllegalArgumentException("Pixels are missing.");
        }

        if (imageWidth <= 0 || imageHeight <= 0 || pixels.length < (long) imageWidth * imageHeight) {
            throw new IllegalArgumentException("Pixel buffer does not match the image.");
        }

        return average(imageWidth, imageHeight, region, (x, y) -> pixels[y * imageWidth + x]);
    }

    private static AverageColor average(
            int imageWidth,
            int imageHeight,
            ImageRegion region,
            IntBinaryOperator rgbAt
    ) {
        if (region == null) {
            throw new IllegalArgumentException("Region is missing.");
        }

        if (region.x() + region.width() > imageWidth || region.y() + region.height() > imageHeight) {
            throw new IllegalArgumentException("Region does not fit inside the image.");
        }

        long red = 0;
        long green = 0;
        long blue = 0;
        long count = (long) region.width() * region.height();

        for (int y = region.y(); y < region.y() + region.height(); y++) {
            for (int x = region.x(); x < region.x() + region.width(); x++) {
                Color color = new Color(rgbAt.applyAsInt(x, y));
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

    public int getRgb() {
        return new Color(red, green, blue).getRGB();
    }

    private static int channel(long sum, long count) {
        return (int) ((sum + count / 2) / count);
    }
}
