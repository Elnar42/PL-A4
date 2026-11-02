package cli;

import process.MultiThreadProcessor;
import process.SingleThreadProcessor;
import process.ThreadProcessor;

public enum Mode {
    SINGLE('S', SingleThreadProcessor.INSTANCE),
    MULTI('M', MultiThreadProcessor.INSTANCE);

    private final char letter;
    private final ThreadProcessor processor;

    /**
     * Stores the mode letter and the processor that runs it.
     */
    Mode(char letter, ThreadProcessor processor) {
        this.letter = letter;
        this.processor = processor;
    }

    /**
     * Returns the mode for the letter S or M.
     */
    public static Mode parse(String text) {
        if (text != null && text.length() == 1) {
            switch (Character.toUpperCase(text.charAt(0))) {
                case 'S':
                    return SINGLE;
                case 'M':
                    return MULTI;
                default:
                    break;
            }
        }

        throw new IllegalArgumentException("Enter S or M.");
    }

    /**
     * Returns the letter shown for this mode.
     */
    public char letter() {
        return letter;
    }

    /**
     * Returns the processor that averages the image for this mode.
     */
    public ThreadProcessor processor() {
        return processor;
    }
}
