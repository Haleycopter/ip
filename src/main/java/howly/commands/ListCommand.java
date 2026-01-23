public class ListCommand extends Command {
    private final String input;
    public ListCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        // Split to check if there is anything after "list"
        String[] parts = input.split(" ", 2);
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'list' command should not have any arguments after it.");
        }

        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }
}
