package view;

import image.AverageColor;
import image.ImageRegion;
import image.RegionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

class ImagePanel extends JPanel implements RegionListener {

    private final BufferedImage image;
    private final int[] pixels;
    private final CountDownLatch painted;
    private final PaintPace pace;
    private Timer refresher;
    private boolean shown;

    /**
     * Stores the image that the panel draws and a pixel buffer the workers can fill.
     */
    ImagePanel(BufferedImage image, CountDownLatch painted, int squareSize) {
        this.image = image;
        this.painted = painted;
        pace = PaintPace.forImage(image.getWidth(), image.getHeight(), squareSize);
        pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
    }

    /**
     * Writes a finished square into the shared pixels, then pauses so that square stays visible.
     */
    @Override
    public void onRegionFinished(ImageRegion region, AverageColor color) {
        fill(region, color.getRgb());
        pace.pause();
    }

    /**
     * Starts copying finished squares onto the window about thirty times a second.
     */
    void startRefreshing() {
        if (refresher != null) {
            refresher.stop();
        }
        refresher = new Timer(30, event -> publish(false));
        refresher.start();
    }

    /**
     * Stops the refresh timer without drawing another frame.
     */
    void stopRefreshing() {
        if (refresher != null) {
            refresher.stop();
        }
    }

    /**
     * Copies every finished square onto the image and draws the window once.
     */
    void refresh() {
        stopRefreshing();
        publish(true);
    }

    /**
     * Writes one finished square into the shared pixel buffer.
     */
    private void fill(ImageRegion region, int rgb) {
        int width = image.getWidth();
        int yEnd = region.y() + region.height();
        int xStart = region.x();
        int rowWidth = region.width();
        synchronized (pixels) {
            for (int y = region.y(); y < yEnd; y++) {
                int offset = y * width + xStart;
                for (int x = 0; x < rowWidth; x++) {
                    pixels[offset + x] = rgb;
                }
            }
        }
    }

    /**
     * Copies the shared pixels onto the image and redraws the panel.
     */
    private void publish(boolean immediately) {
        synchronized (pixels) {
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        }
        if (!isDisplayable()) {
            return;
        }
        if (immediately) {
            paintImmediately(0, 0, getWidth(), getHeight());
        } else {
            repaint();
        }
    }

    /**
     * Draws the image scaled to the panel and signals the first paint.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D drawing = (Graphics2D) graphics;
        drawing.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        drawing.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        if (!shown) {
            shown = true;
            painted.countDown();
        }
    }
}
