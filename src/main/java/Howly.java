import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.File;

public class Howly {
    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    public Howly(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (HowlyException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
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

    private void printList(TaskList tasks) {
        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }

    private void addTask(String input, CommandType type) throws HowlyException {
        Task newTask;
        try {
            switch (type) {
            case TODO:
                newTask = new ToDo(Parser.parseTodo(input));
                break;

            case DEADLINE:
                String[] dParts = Parser.parseDeadline(input);
                newTask = new Deadline(dParts[0], dParts[1]);
                break;

            case EVENT:
                String[] eParts = Parser.parseEvent(input);
                newTask = new Event(eParts[0], eParts[1], eParts[2]);
                break;

            default:
                throw new HowlyException("Please specify an appropriate command to add: todo, deadline, event");
            }

            tasks.add(newTask);
            storage.save(tasks.getTasks()); // Save tasks in hard disk automatically whenever task list changes
            ui.showLine();
            System.out.println(" Got it. I've added this task:\n   " + newTask);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            ui.showLine();
        } catch (java.time.format.DateTimeParseException e) {
            throw new HowlyException("Please use the date format yyyy-mm-dd.");
        }
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
        storage.save(tasks.getTasks()); // Save after marking or unmarking
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

        Task removedTask = tasks.delete(index);
        storage.save(tasks.getTasks()); // Save after deleting
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