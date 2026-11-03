package log;

public final class Log {

    /**
     * Prevents instances of this utility class.
     */
    private Log() {
    }

    /**
     * Prints one line to the console.
     */
    public static void line(String message) {
        System.out.println(message);
    }

    /**
     * Prints an empty line to the console.
     */
    public static void blank() {
        System.out.println();
    }

    /**
     * Prints a question on the current line and shows it before the program reads the answer.
     */
    public static void prompt(String message) {
        System.out.print(message);
        System.out.flush();
    }
}
