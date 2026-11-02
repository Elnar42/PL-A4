package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
        frame.setContentPane(new ImagePanel(image));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.drawImage(image, 0, 0, null);
        }
    }
}
