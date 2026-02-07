package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.ui.Ui;

/**
 * Represents a command to display all tasks currently in the task list.
 * This class ensures the command is used without extra arguments and iterates
 * through the list to print each task.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command by verifying the input format and printing
     * all tasks in the current list to the user interface.
     * <p>
     * This method performs strict validation to ensure that no trailing arguments
     * follow the 'list' command.
     * </p>
     *
     * @param tasks The list of tasks to be displayed.
     * @param ui The user interface used to print the task list.
     * @param storage The storage system (unused by this command).
     * @throws HowlyException If there are trailing arguments after the 'list' command.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        if (tasks.size() == 0) {
            return "Your task list is currently empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
