import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Launch {

    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));
    private static String[] programArguments = new String[0];

    /**
     * Compiles the program for Java 8 and starts Main with the command-line arguments.
     */
    public static void main(String[] args) {
        if (args != null) {
            programArguments = args;
        }
        try {
            runApplication();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Compiles the sources and runs Main, asking again when compilation or startup fails.
     */
    private static void runApplication() throws IOException {
        while (true) {
            if (compile()) {
                int code = startMain();
                if (code == 0) {
                    return;
                }
                System.out.println("The program stopped unexpectedly (exit " + code + ").");
            }

            if (quitRequested("Enter Y to try again, or Q to quit: ")) {
                return;
            }
        }
    }

    /**
     * Returns true when the user chooses Q or the input ends.
     */
    private static boolean quitRequested(String question) throws IOException {
        while (true) {
            System.out.print(question);
            String answer = readLine();
            if (answer == null || answer.equalsIgnoreCase("Q")) {
                return true;
            }
            if (answer.equalsIgnoreCase("Y")) {
                return false;
            }
            System.out.println("Enter Y to try again, or Q to quit.");
        }
    }

    /**
     * Reads one trimmed console line, or null when the input ends.
     */
    private static String readLine() throws IOException {
        String line = INPUT.readLine();
        if (line == null) {
            return null;
        }
        return line.trim();
    }

    /**
     * Reads the major Java version, treating an unreadable version as 8.
     */
    private static int javaMajor() {
        String specification = System.getProperty("java.specification.version", "1.8");
        try {
            if (specification.startsWith("1.")) {
                return Integer.parseInt(specification.substring(2));
            }
            int end = specification.indexOf('.');
            String number = end < 0 ? specification : specification.substring(0, end);
            return Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            return 8;
        }
    }

    /**
     * Compiles every source file under src so the classes run on Java 8 and newer.
     */
    private static boolean compile() throws IOException {
        File javac = tool("javac");
        if (javac == null) {
            if (new File(new File("out"), "Main.class").isFile()) {
                return true;
            }
            System.out.println("A JDK is required. Install any JDK 8 or newer and add its bin folder to PATH.");
            return false;
        }

        List<String> sources = new ArrayList<String>();
        collectSources(new File("src"), sources);
        if (sources.isEmpty()) {
            System.out.println("No Java sources were found in src. Run Launch from the project directory.");
            return false;
        }

        File output = new File("out");
        if (!output.isDirectory() && !output.mkdirs()) {
            System.out.println("Could not create the out directory.");
            return false;
        }

        List<String> command = new ArrayList<String>();
        command.add(javac.getAbsolutePath());
        if (javaMajor() >= 9) {
            command.add("--release");
            command.add("8");
        } else {
            command.add("-source");
            command.add("8");
            command.add("-target");
            command.add("8");
        }
        command.add("-d");
        command.add(output.getAbsolutePath());
        command.addAll(sources);

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO();
        builder.directory(workingDirectory());
        return waitFor(builder.start()) == 0;
    }

    /**
     * Adds every .java file under the directory to the source list.
     */
    private static void collectSources(File directory, List<String> sources) {
        File[] children = directory.listFiles();
        if (children == null) {
            return;
        }

        for (int index = 0; index < children.length; index++) {
            File child = children[index];
            if (child.isDirectory()) {
                collectSources(child, sources);
            } else if (child.getName().endsWith(".java")) {
                sources.add(child.getAbsolutePath());
            }
        }
    }

    /**
     * Starts Main with the same command-line arguments and waits until that process exits.
     */
    private static int startMain() throws IOException {
        File java = tool("java");
        if (java == null) {
            System.out.println("Could not find java in the running JDK.");
            return 1;
        }

        List<String> command = new ArrayList<String>();
        command.add(java.getAbsolutePath());
        command.add("-cp");
        command.add(new File("out").getAbsolutePath());
        command.add("Main");
        for (int index = 0; index < programArguments.length; index++) {
            command.add(programArguments[index]);
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO();
        builder.directory(workingDirectory());
        return waitFor(builder.start());
    }

    /**
     * Returns java or javac from this JDK, including a Java 8 layout where java lives in the JRE folder.
     */
    private static File tool(String name) {
        String fileName = System.getProperty("os.name", "").toLowerCase().contains("win") ? name + ".exe" : name;
        String home = System.getProperty("java.home");
        File direct = new File(new File(home, "bin"), fileName);
        if (direct.isFile()) {
            return direct;
        }

        File parent = new File(home).getParentFile();
        if (parent != null) {
            File sibling = new File(new File(parent, "bin"), fileName);
            if (sibling.isFile()) {
                return sibling;
            }
        }
        return null;
    }

    /**
     * Waits for a process and reports an interrupt as a failure.
     */
    private static int waitFor(Process process) throws IOException {
        try {
            return process.waitFor();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("The program was interrupted.");
        }
    }

    /**
     * Returns the project directory from which Launch was started.
     */
    private static File workingDirectory() {
        return new File(System.getProperty("user.dir"));
    }
}
