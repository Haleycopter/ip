package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;
import howly.parser.Parser;
/**
 * Represents a command to change the completion status of a task in the list.
 * This class can either mark a task as completed or revert it to a not-done state.
 */
public class MarkCommand extends Command {
    private final String input;
    private final boolean isMark;

    public MarkCommand(String input, boolean isMark) {
        this.input = input;
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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        String[] parts = input.split(" ");

        // Strict validation: check that there are exactly 2 parts
        if (parts.length != 2) {
            String cmd = isMark ? "mark" : "unmark";
            throw new HowlyException("The '" + cmd + "' command expects exactly one task number. Eg: " + cmd + " 2");
        }
        int index = Parser.parseIndex(input);
        if (index < 0 || index >= tasks.size()) {
            throw new HowlyException("Task does not exist.");
        }

        Task task = tasks.get(index);
        if (isMark) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }

        storage.save(tasks.getTasks());
        ui.showLine();
        System.out.println(isMark ? " Nice! I've marked this task as done:" :
                " OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        ui.showLine();
    }
}
