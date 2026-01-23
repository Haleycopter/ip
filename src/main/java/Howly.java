import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.io.File;

public class Howly {
    private Storage storage;
    private Ui ui;
    private ArrayList<Task> tasks;

    public Howly(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            tasks = storage.load();
        } catch (HowlyException e) {
            ui.showError(e.getMessage());
            tasks = new ArrayList<>();
        }
    }

    public void run() {
        ui.showWelcome();
        while (true) {
            try {
                String userInput = ui.readCommand().trim();
                if (userInput.isEmpty()) {
                    continue;
                }
                String[] parts = userInput.split(" ", 2);
                CommandType command = CommandType.fromString(parts[0]);
                switch(command) {
                case BYE:
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        throw new HowlyException("The 'bye' command should not have any arguments after it.");
                    }
                    ui.showLine();
                    System.out.println(" Bye. Hope to see you again soon!");
                    ui.showLine();
                    return;

                case LIST:
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        throw new HowlyException("The 'list' command should not have any arguments after it.");
                    }
                    printList(tasks);
                    break;

                case MARK:
                    handleMarkUnmark(userInput, true);
                    break;

                case UNMARK:
                    handleMarkUnmark(userInput, false);
                    break;

                case DELETE:
                    handleDelete(userInput);
                    break;

                case FINDDATE:
                    handleFindDate(userInput);
                    break;

                case TODO:
                case DEADLINE:
                case EVENT:
                    addTask(userInput, command);
                    break;

                case UNKNOWN:
                default:
                    throw new HowlyException("I'm sorry, please enter a valid command.");
                }
            } catch (HowlyException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please provide a valid task number.");
            }
        }
    }

    private void printList(ArrayList<Task> tasks) {
        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }

    private void addTask(String input, CommandType type) throws HowlyException {
        Task newTask;
        if (type == CommandType.TODO) {
            String desc = input.replaceFirst("(?i)todo", "").trim();
            if (desc.isEmpty()) {
                throw new HowlyException("You have to specify the task after 'todo'.");
            }
            newTask = new ToDo(desc);
        } else if (type == CommandType.DEADLINE) {
            String content = input.replaceFirst("(?i)deadline", "").trim();
            if (!content.contains("/by")) {
                throw new HowlyException("A deadline must include /by. Eg: deadline return book /by 2025-12-31");
            }
            String[] parts = content.split("/by", 2);
            try {
                newTask = new Deadline(parts[0].trim(), parts[1].trim());
            } catch (DateTimeParseException e) {
                throw new HowlyException("Please use the date format yyyy-mm-dd (Eg: 2025-10-15)");
            }
        } else { // type == EVENT
            String content = input.replaceFirst("(?i)event", "").trim();
            if (!content.contains("/from") || !content.contains("/to")) {
                throw new HowlyException("An event must include /from and /to." +
                        "Eg: event meeting /from 2025-10-15 /to 2025-10-16");
            }
            String[] parts = content.split("/from|/to");
            try {
                newTask = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
            } catch (DateTimeParseException e) {
                throw new HowlyException("Please use the date format yyyy-mm-dd after both /from and /to.");
            }
        }

        tasks.add(newTask);
        storage.save(tasks); // Save tasks in hard disk automatically whenever task list changes
        ui.showLine();
        System.out.println(" Got it. I've added this task:\n   " + newTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    private void handleMarkUnmark(String input, boolean isMark) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new HowlyException("Please specify a task number.");

        int index = Integer.parseInt(parts[1]) - 1;
        if (index < 0 || index >= tasks.size()) throw new HowlyException("Task does not exist.");

        Task task = tasks.get(index);
        if (isMark) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks); // Save after marking or unmarking
        ui.showLine();
        System.out.println(isMark ? " Nice! I've marked this task as done:" :
                " OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        ui.showLine();
    }

    private void handleDelete(String input) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new HowlyException("Please specify which task number to delete.");

        int index = Integer.parseInt(parts[1]) - 1;
        if (index < 0 || index >= tasks.size()) throw new HowlyException("That task doesn't exist.");

        Task removedTask = tasks.remove(index);
        storage.save(tasks); // Save after deleting
        ui.showLine();
        System.out.println(" Noted. I've removed this task:\n  " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    private void handleFindDate(String input) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new HowlyException("Please specify a date in yyyy-mm-dd format. Eg: finddate 2025-12-31");
        }
        try {
            LocalDate searchDate = LocalDate.parse(parts[1]);
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
        } catch (DateTimeParseException e) {
            throw new HowlyException("Please use the date format yyyy-mm-dd for searching.");
        }
    }

    public static void main(String[] args) {
        new Howly("data" + File.separator + "howly.txt").run();
    }
}