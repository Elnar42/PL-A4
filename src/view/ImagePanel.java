package view;

import image.ImageRegion;
import image.RegionListener;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

class ImagePanel extends JPanel implements RegionListener {

    private final BufferedImage image;
    private final CountDownLatch painted;
    private boolean shown;

    ImagePanel(BufferedImage image, CountDownLatch painted) {
        this.image = image;
        this.painted = painted;
    }

    @Override
    public void onRegionFinished(ImageRegion region) {
        UiThread.run(() -> paintImmediately(0, 0, getWidth(), getHeight()));
    }

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
