package image;

public interface RegionListener {

    /**
     * Receives one finished square and its average color.
     */
    void onRegionFinished(ImageRegion region, AverageColor color);
}
