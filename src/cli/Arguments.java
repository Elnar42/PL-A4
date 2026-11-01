package cli;

public record Arguments(String fileName, int squareSize, Mode mode) {

    public static final String USAGE = """
            Usage: java Main <file name> <square size> <mode>
              file name    JPEG file to average
              square size  side length of each square, in pixels
              mode         S for single-threaded, M for multi-threaded
            Example: java Main somefile.jpg 5 S
            """;

    public Arguments {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "File name is missing. \n" + USAGE
            );
        }

        if (squareSize <= 0) {
            throw new IllegalArgumentException(
                    "Square size must be a positive integer. \n" + USAGE
            );
        }

        if (mode == null) {
            throw new IllegalArgumentException(
                    "Mode cannot be null. \n" + USAGE
            );
        }

    }

    public static Arguments parse(String[] args) {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException(USAGE);
        }

        return new Arguments(
                args[0],
                parseSquareSize(args[1]),
                parseMode(args[2])
        );
    }

    private static int parseSquareSize(String text) {
        try {
            int size = Integer.parseInt(text);

            if (size > 0) {
                return size;
            }
        } catch (NumberFormatException ignored) {
        }

        throw new IllegalArgumentException(
                "Square size must be a positive integer. \n" + USAGE
        );
    }

    private static Mode parseMode(String text) {
        if (text != null && text.length() == 1) {
            return switch (Character.toUpperCase(text.charAt(0))) {
                case 'S' -> Mode.SINGLE;
                case 'M' -> Mode.MULTI;
                default -> rejectMode();
            };
        }

        return rejectMode();
    }

    private static Mode rejectMode() {
        throw new IllegalArgumentException(
                "Mode must be S (single-threaded) or M (multi-threaded). \n" + USAGE
        );
    }
}
