package process;

import image.ImageRegion;

import java.util.ArrayList;
import java.util.List;

public final class RowBand {

    private final List<ImageRegion> regions;

    private RowBand(List<ImageRegion> regions) {
        this.regions = List.copyOf(regions);
    }

    public List<ImageRegion> regions() {
        return regions;
    }

    public static List<RowBand> split(int imageWidth, int imageHeight, int squareSize) {
        List<List<ImageRegion>> rows = rowsOf(ImageRegion.forImage(imageWidth, imageHeight, squareSize));
        int workers = WorkerCount.forImage(imageHeight, squareSize);
        int base = rows.size() / workers;
        int extra = rows.size() % workers;

        List<RowBand> bands = new ArrayList<>();
        int index = 0;

        for (int worker = 0; worker < workers; worker++) {
            int rowCount = base + (worker < extra ? 1 : 0);
            List<ImageRegion> band = new ArrayList<>();

            for (int row = 0; row < rowCount; row++) {
                band.addAll(rows.get(index));
                index++;
            }

            bands.add(new RowBand(band));
        }

        return List.copyOf(bands);
    }

    private static List<List<ImageRegion>> rowsOf(List<ImageRegion> regions) {
        List<List<ImageRegion>> rows = new ArrayList<>();
        List<ImageRegion> row = new ArrayList<>();
        int y = regions.getFirst().y();

        for (ImageRegion region : regions) {
            if (region.y() != y) {
                rows.add(List.copyOf(row));
                row = new ArrayList<>();
                y = region.y();
            }

            row.add(region);
        }

        rows.add(List.copyOf(row));
        return rows;
    }
}
