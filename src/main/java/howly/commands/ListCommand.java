package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;
/**
 * Represents a command to display all tasks currently in the task list.
 * This class ensures the command is used without extra arguments and iterates
 * through the list to print each task.
 */
public class ListCommand extends Command {
    private final String input;
    public ListCommand(String input) {
        this.input = input;
    }

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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        // Split to check if there is anything after "list"
        String[] parts = input.split(" ", 2);
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'list' command should not have any arguments after it.");
        }

        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }
}
