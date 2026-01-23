package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;
import howly.parser.Parser;

public class DeleteCommand extends Command {
    private final String input;
    public DeleteCommand(String input) {
        this.input = input;
    }

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