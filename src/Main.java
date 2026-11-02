import cli.Arguments;
import image.ImageLoader;
import image.ResultWriter;
import process.ThreadProcessor;
import view.ImageWindow;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        try {
            Arguments arguments = Arguments.parse(args);

            BufferedImage image = ImageLoader.load(arguments.fileName());
            String summary = "Mode: " + arguments.mode().letter()
                    + "    Square size: " + arguments.squareSize();
            ImageWindow window = ImageWindow.show(image, summary);
            ThreadProcessor processor = arguments.mode().processor();
            window.onClose(processor::stop);

            processor.run(image, arguments.squareSize(), window.listener());
            ResultWriter.write(image);
            window.saved("Saved " + ResultWriter.FILE_NAME);

            System.out.println("Saved " + ResultWriter.FILE_NAME);

        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
