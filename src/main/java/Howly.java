import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;

public class Howly {
    // Hard-code file name and relative path from project root
    private static final String FILE_PATH = "data" + File.separator + "howly.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHorizontalLine();
        System.out.println(" Hello! I'm Howly\n What can I do for you?");
        printHorizontalLine();

        // Load existing tasks from hard disk when chatbot starts up
        ArrayList<Task> tasks = loadTasks();

        while (scanner.hasNextLine()) {
            try {
                String userInput = scanner.nextLine().trim();
                if (userInput.isEmpty()) {
                    continue;
                }

                String[] parts = userInput.split(" ", 2);
                CommandType command = CommandType.fromString(parts[0]);

                switch (command) {
                case BYE:
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        throw new HowlyException("The 'bye' command should not have any arguments after it.");
                    }
                    printHorizontalLine();
                    System.out.println(" Bye. Hope to see you again soon!");
                    printHorizontalLine();
                    return;

                case LIST:
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        throw new HowlyException("The 'list' command should not have any arguments after it.");
                    }
                    printList(tasks);
                    break;

                case MARK:
                    handleMarkUnmark(userInput, tasks, true);
                    break;

                case UNMARK:
                    handleMarkUnmark(userInput, tasks, false);
                    break;

                case DELETE:
                    handleDelete(userInput, tasks);
                    break;

                case TODO:
                case DEADLINE:
                case EVENT:
                    addTask(userInput, tasks, command);
                    break;

                case UNKNOWN:
                default:
                    throw new HowlyException("I'm sorry, please enter a valid command: " +
                            "todo, deadline, event, list, mark, unmark, delete, or bye.");
                }
            } catch (HowlyException e) {
                printHorizontalLine();
                System.out.println(" Oh no. " + e.getMessage());
                printHorizontalLine();
            } catch (NumberFormatException e) {
                printHorizontalLine();
                System.out.println(" Please provide a valid task number.");
                printHorizontalLine();
            }
        }
    }

    private static void printList(ArrayList<Task> tasks) {
        printHorizontalLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        printHorizontalLine();
    }

    private static void addTask(String input, ArrayList<Task> tasks, CommandType type) throws HowlyException {
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
        saveTasks(tasks); // Save tasks in hard disk automatically whenever task list changes
        printHorizontalLine();
        System.out.println(" Got it. I've added this task:\n   " + newTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printHorizontalLine();
    }

    private static void handleMarkUnmark(String input, ArrayList<Task> tasks, boolean isMark) throws HowlyException {
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
        saveTasks(tasks); // Save after marking or unmarking
        printHorizontalLine();
        System.out.println(isMark ? " Nice! I've marked this task as done:" :
                " OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        printHorizontalLine();
    }

    private static void handleDelete(String input, ArrayList<Task> tasks) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new HowlyException("Please specify which task number to delete.");

        int index = Integer.parseInt(parts[1]) - 1;
        if (index < 0 || index >= tasks.size()) throw new HowlyException("That task doesn't exist.");

        Task removedTask = tasks.remove(index);
        saveTasks(tasks); // Save after deleting
        printHorizontalLine();
        System.out.println(" Noted. I've removed this task:\n  " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printHorizontalLine();
    }

    private static void printHorizontalLine() {
        System.out.println("____________________________________________________________");
    }

    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            File f = new File(FILE_PATH);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(f);
            for (Task t : tasks) {
                fw.write(t.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(" Error saving tasks: " + e.getMessage());
        }
    }

    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            return loadedTasks;
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] parts = line.split(" \\| ");
                Task t;
                // parts[0] is Type, parts[1] is isDone, parts[2] is task description
                switch (parts[0]) {
                case "T":
                    t = new ToDo(parts[2]);
                    break;
                case "D":
                    t = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    t = new Event(parts[2], parts[3], parts[4]);
                    break;
                default:
                    continue;
                }
                if (parts[1].equals("1")) {
                    t.markAsDone();
                }
                loadedTasks.add(t);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.out.println(" Warning: Data file corrupted or unreadable. Starting with fresh list.");
        }
        return loadedTasks;
    }
}