package howly.ui;

import java.util.Scanner;

/**
 * Handles the user interface of the Howly application.
 * This class is responsible for reading user input and displaying messages,
 * errors, and formatting lines to the console.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a horizontal separator line to the console.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the welcome message to the user when the application starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Howly\n What can I do for you?");
        showLine();
    }

    /**
     * Reads a full line of command input from the user.
     *
     * @return The raw input string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a formatted error message to the user.
     *
     * @param message The specific error message to be displayed.
     */
    public void showError(String message) {
        showLine();
        System.out.println(" Oh no. " + message);
        showLine();
    }

    /**
     * Combines multiple messages into a single string separated by newlines.
     *
     * @param messages The variable number of messages to combine.
     * @return A single formatted string containing all messages.
     */
    public String formatMessages(String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String m : messages) {
            sb.append(m).append("\n");
        }
        return sb.toString().trim(); // Trim to remove the final trailing newline
    }
}
