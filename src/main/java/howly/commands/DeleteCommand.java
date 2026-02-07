package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

/**
 * Represents a command to delete a specific task from the task list.
 * This class handles the validation of the task index and the removal of the task.
 */
public class DeleteCommand extends Command {
    private final int targetIndex;

    public DeleteCommand(int index) {
        this.targetIndex = index;
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
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        assert targetIndex >= 0 : "Target index for deletion should be non-negative"; //

        if (targetIndex >= tasks.size()) {
            throw new HowlyException("Task index " + (targetIndex + 1) + " does not exist.");
        }

        Task deletedTask = tasks.delete(targetIndex);
        storage.save(tasks.getTasks());
        return "Noted. I've removed this task:\n  " + deletedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }
}
