import cli.Arguments;
import cli.Mode;
import image.ImageLoader;
import image.ResultWriter;
import process.SingleThreadProcessor;
import view.ImageWindow;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        try {
            Arguments arguments = Arguments.parse(args);

            BufferedImage image = ImageLoader.load(arguments.fileName());
            ImageWindow.show(image);

            SingleThreadProcessor.INSTANCE.process(image, arguments.squareSize());
            ResultWriter.write(image);

            System.out.println("Saved " + ResultWriter.FILE_NAME);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
