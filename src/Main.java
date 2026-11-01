import cli.Arguments;
import cli.Mode;
import image.ImageLoader;
import image.ResultWriter;
import process.SingleThreadProcessor;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        Arguments arguments;
        try {
            arguments = Arguments.parse(args);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("File name: " + arguments.fileName());
        System.out.println("Square size: " + arguments.squareSize());
        System.out.println("Mode: " + modeLetter(arguments.mode()));

        BufferedImage image;
        try {
            image = ImageLoader.load(arguments.fileName());
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Width: " + image.getWidth());
        System.out.println("Height: " + image.getHeight());

        if (arguments.mode() != Mode.SINGLE) {
            return;
        }

        try {
            SingleThreadProcessor.process(image, arguments.squareSize());
            ResultWriter.write(image);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Saved " + ResultWriter.FILE_NAME);
    }

    private static char modeLetter(Mode mode) {
        return mode == Mode.SINGLE ? 'S' : 'M';
    }
}
