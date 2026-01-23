public class ExitCommand extends Command {
    private final String input;
    public ExitCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        // Check if there is anything after the word "bye"
        String[] parts = input.split(" ", 2);
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'bye' command should not have any arguments after it.");
        }
        ui.showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
