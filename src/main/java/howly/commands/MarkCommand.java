package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;
import howly.parser.Parser;

public class MarkCommand extends Command {
    private final String input;
    private final boolean isMark;

    public MarkCommand(String input, boolean isMark) {
        this.input = input;
        this.isMark = isMark;
    }

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
