package view;

import exceptions.MissingImageException;
import exceptions.MissingSummaryException;
import exceptions.WindowException;
import image.RegionListener;
import window.CloseRequest;
import window.UiThread;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ImageWindow {

    private final ImagePanel panel;
    private final JLabel status;
    private final String summary;
    private final CloseRequest closeRequest;

    /**
     * Keeps the panel, status line, and close action for one window.
     */
    private ImageWindow(ImagePanel panel, JLabel status, String summary, CloseRequest closeRequest) {
        this.panel = panel;
        this.status = status;
        this.summary = summary;
        this.closeRequest = closeRequest;
    }

    /**
     * Shows the original image and waits until the first paint finishes.
     */
    public static ImageWindow show(BufferedImage image, String summary) {
        if (image == null) {
            throw new MissingImageException();
        }

        if (summary == null || summary.trim().isEmpty()) {
            throw new MissingSummaryException();
        }

        CountDownLatch painted = new CountDownLatch(1);
        AtomicReference<ImageWindow> window = new AtomicReference<>();
        UiThread.run(() -> window.set(open(image, summary, painted)));

        try {
            painted.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new WindowException("Could not show the image.");
        }

        return window.get();
    }

    /**
     * Returns the listener that paints each finished square.
     */
    public RegionListener listener() {
        return panel;
    }

    /**
     * Runs the given action when the window is closing.
     */
    public void onClose(Runnable action) {
        closeRequest.setAction(action);
    }

    /**
     * Adds the save message to the status line.
     */
    public void saved(String line) {
        UiThread.run(() -> status.setText(summary + "    " + line));
    }

    /**
     * Builds the frame, fits it to the screen, and shows it.
     */
    private static ImageWindow open(BufferedImage image, String summary, CountDownLatch painted) {
        JFrame frame = new JFrame("PL-A4");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        CloseRequest closeRequest = new CloseRequest();
        frame.addWindowListener(closeRequest);

        ImagePanel panel = new ImagePanel(image, painted);
        JLabel status = new JLabel(summary);
        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel content = new JPanel(new BorderLayout());
        content.add(panel, BorderLayout.CENTER);
        content.add(status, BorderLayout.PAGE_END);
        frame.setContentPane(content);
        frame.pack();

        Insets insets = frame.getInsets();
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int maxWidth = Math.max(1, screen.width - insets.left - insets.right);
        int maxHeight = Math.max(1, screen.height - insets.top - insets.bottom - status.getHeight());
        panel.setPreferredSize(fittedSize(image.getWidth(), image.getHeight(), maxWidth, maxHeight));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return new ImageWindow(panel, status, summary, closeRequest);
    }

    /**
     * Scales the image down to the available screen area, keeping its aspect ratio.
     */
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
