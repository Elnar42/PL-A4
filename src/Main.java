import cli.Menu;

public class Main {

    /**
     * Runs java Main <image> <square size> <S|M>, asking again for a missing or invalid argument.
     */
    public static void main(String[] args) {
        new Menu().run(args);
    }
}
