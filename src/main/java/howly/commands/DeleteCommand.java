package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;
import howly.parser.Parser;
/**
 * Represents a command to delete a specific task from the task list.
 * This class handles the validation of the task index and the removal of the task.
 */
public class DeleteCommand extends Command {
    private final String input;

    public DeleteCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the delete command by parsing the task index, removing the task
     * from the list, and updating the storage file.
     * * This method performs strict validation to ensure only one argument is provided
     * and that the index exists within the current task list.
     *
     * @param tasks The list of tasks from which the task will be removed.
     * @param ui The user interface used to provide feedback to the user.
     * @param storage The storage handler used to persist the updated task list.
     * @throws HowlyException If the input format is invalid or the task index is out of bounds.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length > 2) {
            throw new HowlyException("The 'delete' command only accepts a single task number.");
        }

        int index = Parser.parseIndex(input);
        if (index < 0 || index >= tasks.size()) {
            throw new HowlyException("That task doesn't exist.");
        }

        Task removedTask = tasks.delete(index);
        storage.save(tasks.getTasks());
        ui.showLine();
        System.out.println(" Noted. I've removed this task:\n  " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}