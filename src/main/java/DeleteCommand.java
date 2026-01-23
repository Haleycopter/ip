public class DeleteCommand extends Command {
    private final String input;
    public DeleteCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
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