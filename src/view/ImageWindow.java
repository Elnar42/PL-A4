package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageWindow {

    public static void show(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image is missing.");
        }

        BufferedImage loaded = copy(image);
        SwingUtilities.invokeLater(() -> open(loaded));
    }

    private static void open(BufferedImage image) {
        JFrame frame = new JFrame("PL-A4");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImagePanel panel = new ImagePanel(image);
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

    private static BufferedImage copy(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return copy;
    }

    private static final class ImagePanel extends JPanel {

        private final BufferedImage image;

        private ImagePanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D drawing = (Graphics2D) graphics;
            drawing.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            drawing.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
