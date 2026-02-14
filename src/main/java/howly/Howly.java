package howly;

import java.io.File;

import howly.commands.Command;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.ui.Ui;

/**
 * Acts as the main controller for the Howly chatbot application.
 * Manages the initialization of core components and coordinates the execution of tasks.
 */
public class Howly {
    private static final String FILE_PATH = "data" + File.separator + "howly.txt";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Initializes the Howly application with the specified storage file path.
     * @param filePath The path to the file where task data is stored (e.g., "data/howly.txt").
     */
    public Howly(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (HowlyException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Initializes the Howly application with a default file path.
     * Provides a no-argument constructor for JavaFX compatibility.
     */
    public Howly() {
        this(FILE_PATH);
    }

    /**
     * Starts the main application loop.
     * Reads and executes commands until an exit command is received.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand().trim();
                if (fullCommand.isEmpty()) {
                    continue;
                }
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (HowlyException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please provide a valid task number.");
            }
        }
    }

    /**
     * Generates a response for the user's chat message input.
     *
     * @param input The raw user input string.
     * @return Resulting response message from the chatbot.
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            return c.execute(tasks, ui, storage);
        } catch (HowlyException | NumberFormatException e) {
            return "Gahhh! What's the point in living if I can't be beautiful...\n"
                    + e.getMessage();
        }
    }

    /**
     * Serves as the entry point for the command-line interface of Howly.
     * Initializes the app with the default file path and begins the program execution.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Howly().run();
    }
}
