package cli;

import exceptions.InvalidModeException;
import image.ImageLoader;
import log.Log;
import image.ResultWriter;
import process.ThreadProcessor;
import view.ImageWindow;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Menu {

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Uses the file name, square size, and mode from the command line, and asks again for each invalid one.
     */
    public void run(String[] args) {
        if (args == null) {
            args = new String[0];
        }
        if (args.length != 3) {
            Log.line("Usage: java Main <image> <square size> <S|M>");
            Log.line("Example: java Main image.png 20 S");
        }

        String fileName = args.length > 0 ? args[0].trim() : "";
        if (fileName.isEmpty()) {
            fileName = readFileName();
            if (fileName == null) {
                System.exit(0);
            }
        }

        Integer squareSize = args.length > 1 ? parsedSquareSize(args[1]) : null;
        if (squareSize == null) {
            if (args.length > 1) {
                Log.line("Square size must be a positive whole number.");
            }
            squareSize = readSquareSize();
        }

        Mode mode = null;
        if (args.length > 2) {
            try {
                mode = Mode.parse(args[2]);
            } catch (InvalidModeException exception) {
                Log.line(exception.getMessage());
            }
        }
        if (mode == null) {
            mode = readMode();
        }

        while (!process(fileName, squareSize, mode)) {
            fileName = readFileName();
            if (fileName == null) {
                System.exit(0);
            }
        }
    }

    /**
     * Returns a positive square size, or null when the text is not one.
     */
    private Integer parsedSquareSize(String text) {
        try {
            int size = Integer.parseInt(text.trim());
            if (size > 0) {
                return size;
            }
        } catch (NumberFormatException exception) {
            return null;
        }
        return null;
    }

    /**
     * Lists the files currently in img/ and reads a number, the user's own image path, or Q to quit.
     */
    private String readFileName() {
        while (true) {
            List<File> images = imagesInFolder();
            Log.blank();
            Log.line("PL-A4");
            Log.line("Each square of the image is replaced by its average color.");
            Log.blank();

            if (images.isEmpty()) {
                Log.line("img/ is empty.");
            } else {
                Log.line("Images in img/:");
                for (int index = 0; index < images.size(); index++) {
                    Log.line((index + 1) + ". " + images.get(index).getName());
                }
            }

            Log.blank();
            Log.line("Enter a number or a name from the list, or type the path of your own image.");
            String answer = prompt("Image, or Q to quit: ");
            if (answer.equalsIgnoreCase("Q")) {
                return null;
            }
            if (answer.isEmpty()) {
                Log.line("File name is missing.");
                continue;
            }
            if (answer.matches("\\d+")) {
                try {
                    int number = Integer.parseInt(answer);
                    if (number >= 1 && number <= images.size()) {
                        return images.get(number - 1).getPath();
                    }
                } catch (NumberFormatException exception) {
                    Log.line("Enter a number or a name from the list, or the path of your own image.");
                    continue;
                }
                Log.line("Enter a number or a name from the list, or the path of your own image.");
                continue;
            }
            return imagePath(answer, images);
        }
    }

    /**
     * Resolves a listed name to its file in img/, and leaves any other path unchanged.
     */
    private String imagePath(String answer, List<File> images) {
        for (File image : images) {
            if (image.getName().equalsIgnoreCase(answer)) {
                return image.getPath();
            }
        }

        File inFolder = new File(imageFolder(), answer);
        if (inFolder.isFile()) {
            return inFolder.getPath();
        }
        return answer;
    }

    /**
     * Returns the img folder from the working directory, or from src/img when the program is started from the project root.
     */
    private File imageFolder() {
        File direct = new File("img");
        File underSource = new File("src", "img");
        if (containsFile(direct)) {
            return direct;
        }
        if (containsFile(underSource)) {
            return underSource;
        }
        if (direct.isDirectory()) {
            return direct;
        }
        if (underSource.isDirectory()) {
            return underSource;
        }
        return direct;
    }

    /**
     * Reports whether the folder exists and contains at least one file.
     */
    private boolean containsFile(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return false;
        }
        for (File file : files) {
            if (file.isFile()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every file currently in img/, sorted by name.
     */
    private List<File> imagesInFolder() {
        File[] files = imageFolder().listFiles();
        List<File> images = new ArrayList<>();
        if (files == null) {
            return images;
        }

        for (File file : files) {
            if (file.isFile()) {
                images.add(file);
            }
        }

        images.sort(Comparator.comparing(File::getName));
        return images;
    }

    /**
     * Asks for a positive square size until the answer is a whole number greater than zero.
     */
    private int readSquareSize() {
        while (true) {
            String answer = prompt("Square size in pixels: ");
            try {
                int size = Integer.parseInt(answer);
                if (size > 0) {
                    return size;
                }
            } catch (NumberFormatException exception) {
                // The message below asks for the size again.
            }
            Log.line("Square size must be a positive whole number.");
        }
    }

    /**
     * Asks for S or M until the answer is one of those letters.
     */
    private Mode readMode() {
        while (true) {
            String answer = prompt("Mode, S for one thread or M for many: ");
            try {
                return Mode.parse(answer);
            } catch (InvalidModeException exception) {
                Log.line(exception.getMessage());
            }
        }
    }

    /**
     * Loads the image and averages it, asking for another file when the load fails.
     */
    private boolean process(String fileName, int squareSize, Mode mode) {
        BufferedImage image;
        try {
            image = ImageLoader.load(fileName);
        } catch (RuntimeException exception) {
            Log.line(exception.getMessage());
            return false;
        }

        try {
            Arguments arguments = new Arguments(fileName, squareSize, mode);
            String summary = "Mode: " + arguments.mode().letter()
                    + "    Square size: " + arguments.squareSize();
            ImageWindow window = ImageWindow.show(image, summary);
            ThreadProcessor processor = arguments.mode().processor();
            window.onClose(processor::stop);
            window.startTimer();
            try {
                processor.run(image, arguments.squareSize(), window.listener());
                window.refresh();
            } finally {
                window.stopTimer();
            }
            String saved = "Saved " + ResultWriter.write(image);
            window.saved(saved);
            Log.line(saved);
            return true;
        } catch (Exception exception) {
            String message = exception.getMessage();
            Log.line(message == null ? "Could not process the image." : message);
            return true;
        }
    }

    /**
     * Prints a question and reads one trimmed answer.
     */
    private String prompt(String question) {
        Log.prompt(question);
        try {
            String line = input.readLine();
            if (line == null) {
                System.exit(0);
            }
            return line.trim();
        } catch (IOException exception) {
            Log.line("Could not read the answer.");
            return "";
        }
    }
}
