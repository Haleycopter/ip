package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.ui.Ui;

/**
 * Represents a command to terminate the Howly application.
 * This class handles the validation of the exit command to ensure no extra
 * arguments are provided and signals the main loop to stop.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command by verifying that the input contains no trailing
     * arguments and displaying a farewell message to the user.
     *
     * @param tasks The current list of tasks (unused by this command).
     * @param ui The user interface used to display the farewell message.
     * @param storage The storage system (unused by this command).
     * @throws HowlyException If there are trailing arguments after the 'bye' command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Signals to the main application loop that it is time to exit.
     *
     * @return {@code true} to indicate the application should terminate.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
