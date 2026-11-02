package view;

import image.RegionListener;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ImageWindow {

    public static RegionListener show(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        CountDownLatch painted = new CountDownLatch(1);
        AtomicReference<ImagePanel> panel = new AtomicReference<>();
        UiThread.run(() -> panel.set(open(image, painted)));

        try {
            painted.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Could not show the image.");
        }

        return panel.get();
    }

    private static ImagePanel open(BufferedImage image, CountDownLatch painted) {
        JFrame frame = new JFrame("PL-A4");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImagePanel panel = new ImagePanel(image, painted);
        frame.setContentPane(panel);
        frame.pack();

        Insets insets = frame.getInsets();
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int maxWidth = Math.max(1, screen.width - insets.left - insets.right);
        int maxHeight = Math.max(1, screen.height - insets.top - insets.bottom);
        panel.setPreferredSize(fittedSize(image.getWidth(), image.getHeight(), maxWidth, maxHeight));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return panel;
    }

    private static Dimension fittedSize(int imageWidth, int imageHeight, int maxWidth, int maxHeight) {
        double scale = Math.min(1.0, Math.min(
                maxWidth / (double) imageWidth,
                maxHeight / (double) imageHeight
        ));
        int width = Math.max(1, (int) Math.round(imageWidth * scale));
        int height = Math.max(1, (int) Math.round(imageHeight * scale));
        return new Dimension(width, height);
    }
}
