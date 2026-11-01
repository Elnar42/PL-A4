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
    }

    private static char modeLetter(Mode mode) {
        return mode == Mode.SINGLE ? 'S' : 'M';
    }
}
