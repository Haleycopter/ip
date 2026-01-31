package howly;

import java.io.File;

import howly.commands.Command;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.ui.Ui;

/**
 * The main class for the Howly chatbot application.
 * Howly is a task management tool that allows users to track todos, deadlines, and events.
 * It handles the initialization of core components and manages the main application loop.
 */
public class Howly {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
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
     * Starts the main application loop.
     * Continuously reads user commands, parses them, and executes the corresponding actions
     * until an exit command is received.
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
     * Entry point of the Howly application.
     * Initializes the app with the default file path and begins the program execution.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Howly("data" + File.separator + "howly.txt").run();
    }
}
