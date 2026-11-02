import cli.Arguments;
import image.ImageLoader;
import image.RegionListener;
import image.ResultWriter;
import process.SingleThreadProcessor;
import view.ImageWindow;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        try {
            Arguments arguments = Arguments.parse(args);

            BufferedImage image = ImageLoader.load(arguments.fileName());
            RegionListener listener = ImageWindow.show(image);

            SingleThreadProcessor.INSTANCE.run(image, arguments.squareSize(), listener);
            ResultWriter.write(image);

            System.out.println("Saved " + ResultWriter.FILE_NAME);

        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}
