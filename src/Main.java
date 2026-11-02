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
            String summary = "Mode: " + modeLetter(arguments.mode())
                    + "    Square size: " + arguments.squareSize();
            ImageWindow window = ImageWindow.show(image, summary);

            SingleThreadProcessor.INSTANCE.run(image, arguments.squareSize(), window.listener());
            ResultWriter.write(image);
            window.saved("Saved " + ResultWriter.FILE_NAME);

            System.out.println("Saved " + ResultWriter.FILE_NAME);

        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    private static char modeLetter(Mode mode) {
        return mode == Mode.SINGLE ? 'S' : 'M';
    }
}
