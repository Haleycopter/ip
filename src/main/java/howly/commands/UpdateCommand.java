package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

/**
 * Command to update the description of an existing task, given specified index of task.
 */
public class UpdateCommand extends Command {
    private final int index;
    private final String newDescription;

    /**
     * Constructs an {@code UpdateCommand} with the specified index and new description.
     *
     * @param index The 0-based index of the task to be updated.
     * @param newDescription The new textual description to be assigned to the task.
     */
    public UpdateCommand(int index, String newDescription) {
        this.index = index;
        this.newDescription = newDescription;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        if (index < 0 || index >= tasks.size()) {
            throw new HowlyException("Task index out of bounds. Current list size: " + tasks.size());
        }

        Task taskToUpdate = tasks.get(index);
        String oldDescription = taskToUpdate.getDescription();
        taskToUpdate.setDescription(newDescription);
        
        // Save changes to disk
        storage.save(tasks.getTasks());

        return "Got it. I've updated the description for this task:\n"
                + "  From: " + oldDescription + "\n"
                + "  To:   " + newDescription;
    }
}
