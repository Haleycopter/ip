package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.ui.Ui;

/**
 * Represents an executable command within the Howly application.
 * This abstract class serves as the base for all specific command types
 * such as Add, Delete, and Exit.
 */
public abstract class Command {
    /**
     * Executes the specific logic associated with the command.
     * @param tasks The list of tasks to be operated on.
     * @param ui The user interface used to interact with the user.
     * @param storage The storage system used to save or load task data.
     * @throws HowlyException If an error occurs during the command execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException;

    /**
     * Indicates whether this command should terminate the application's main loop.
     * By default, commands return false unless overridden by specific termination commands.
     * @return True if the application should exit, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
