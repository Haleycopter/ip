package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

/**
 * Represents a command to search for tasks that contain a specific keyword.
 * This class filters the task list and displays matches to the user.
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int count = 0;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.toString().contains(keyword)) {
                count++;
                sb.append(count).append(".").append(task).append("\n");
            }
        }
        return count == 0 ? "No matching tasks found for: " + keyword : sb.toString().trim();
    }
}
