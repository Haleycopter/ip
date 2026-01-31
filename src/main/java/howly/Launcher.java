package howly;

import javafx.application.Application;

/**
 * Entry point for the JavaFX application to bypass classpath limitations.
 */
public class Launcher {
    /**
     * Main method to start the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
