import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FindDateCommand extends Command {
    private final String input;
    public FindDateCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        LocalDate searchDate = Parser.parseDate(input);
        ui.showLine();
        System.out.println(" Here are the tasks occurring on " +
                searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isOnDate(searchDate)) {
                count++;
                System.out.println(" " + count + "." + tasks.get(i));
            }
        }
        if (count == 0) {
            System.out.println(" No tasks found for this date.");
        }
        ui.showLine();
    }
}