package cli;

import process.MultiThreadProcessor;
import process.SingleThreadProcessor;
import process.ThreadProcessor;

public enum Mode {
    SINGLE('S', SingleThreadProcessor.INSTANCE),
    MULTI('M', MultiThreadProcessor.INSTANCE);

    private final char letter;
    private final ThreadProcessor processor;

    Mode(char letter, ThreadProcessor processor) {
        this.letter = letter;
        this.processor = processor;
    }

    public char letter() {
        return letter;
    }

    public ThreadProcessor processor() {
        return processor;
    }
}
