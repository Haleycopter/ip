public class AddCommand extends Command {
    private final String input;
    private final CommandType type;

    public AddCommand(String input, CommandType type) {
        this.input = input;
        this.type = type;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        try {
            Task newTask = switch (type) {
                case TODO -> new ToDo(Parser.parseTodo(input));
                case DEADLINE -> {
                    String[] dParts = Parser.parseDeadline(input);
                    yield new Deadline(dParts[0], dParts[1]);
                }
                case EVENT -> {
                    String[] eParts = Parser.parseEvent(input);
                    yield new Event(eParts[0], eParts[1], eParts[2]);
                }
                default -> throw new HowlyException("Please specify an appropriate command: todo, deadline, event");
            };

            tasks.add(newTask);
            storage.save(tasks.getTasks());
            ui.showLine();
            System.out.println(" Got it. I've added this task:\n   " + newTask);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            ui.showLine();
        } catch (java.time.format.DateTimeParseException e) {
            throw new HowlyException("Please use the date format yyyy-mm-dd (Eg: 2025-12-31).");
        }
    }
}
