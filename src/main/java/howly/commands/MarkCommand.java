package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

/**
 * Represents a command to change the completion status of a task in the list.
 * This class can either mark a task as completed or revert it to a not-done state.
 */
public class MarkCommand extends Command {
    private final int targetIndex;
    private final boolean isMark;

    /**
     * @param index The full command string provided by the user (e.g., "mark 1").
     * @param isMark A boolean indicating if the task should be marked as done (true) or undone (false).
     */
    public MarkCommand(int index, boolean isMark) {
        this.targetIndex = index;
        this.isMark = isMark;
    }

    /**
     * Executes the mark or unmark command by validating the input index, updating
     * the task status, and persisting the change to storage.
     * <p>
     * This method performs strict validation to ensure exactly one argument is provided
     * and that the specified task index exists.
     * </p>
     *
     * @param tasks   The list of tasks containing the target task.
     * @param ui      The user interface used to provide confirmation to the user.
     * @param storage The storage handler used to save the updated task status.
     * @throws HowlyException If the argument count is incorrect or the index is out of bounds.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        assert targetIndex >= 0 : "Target index should be non-negative"; // Incorporating A-Assertions

        if (targetIndex >= tasks.size()) {
            throw new HowlyException("Task index " + (targetIndex + 1) + " does not exist.");
        }

        Task task = tasks.get(targetIndex);
        if (isMark) {
            task.markAsDone();
            storage.save(tasks.getTasks());
            return "Nice! I've marked this task as done:\n  " + task;
        } else {
            task.markAsNotDone();
            storage.save(tasks.getTasks());
            return "OK, I've marked this task as not done yet:\n  " + task;
        }
    }
}
