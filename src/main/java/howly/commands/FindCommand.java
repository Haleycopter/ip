package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        ui.showLine();
        System.out.println(" Here are the matching tasks in your list:");
        
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            // Check if the task description contains the keyword
            if (task.toString().contains(keyword)) {
                count++;
                System.out.println(" " + count + "." + task);
            }
        }

        if (count == 0) {
            System.out.println(" No matching tasks found.");
        }
        ui.showLine();
    }
}