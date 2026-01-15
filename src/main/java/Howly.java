import java.util.Scanner;
import java.util.ArrayList;

public class Howly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHorizontalLine();
        System.out.println(" Hello! I'm Howly\n What can I do for you?");
        printHorizontalLine();

        ArrayList<Task> tasks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            try {
                String userInput = scanner.nextLine().trim();
                if (userInput.isEmpty()) continue;

                String[] parts = userInput.split(" ", 2);
                CommandType command = CommandType.fromString(parts[0]);

                switch (command) {
                    case BYE:
                        printHorizontalLine();
                        System.out.println(" Bye. Hope to see you again soon!");
                        printHorizontalLine();
                        return;

                    case LIST:
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
                        throw new HowlyException("I'm sorry, please enter a valid command: todo, deadline, event, list, mark, unmark, delete, or bye.");
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
                throw new HowlyException("A deadline must include /by. Eg: deadline return book /by Sunday");
            }
            String[] parts = content.split("/by", 2);
            newTask = new Deadline(parts[0].trim(), parts[1].trim());
        } else { // EVENT
            String content = input.replaceFirst("(?i)event", "").trim();
            if (!content.contains("/from") || !content.contains("/to")) {
                throw new HowlyException("An event must include /from and /to. Eg: event meeting /from Aug 6th 2pm /to 4pm");
            }
            String[] parts = content.split("/from|/to");
            newTask = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }

        tasks.add(newTask);
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
        printHorizontalLine();
        if (isMark) {
            task.markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
        printHorizontalLine();
    }

    private static void handleDelete(String input, ArrayList<Task> tasks) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new HowlyException("Please specify which task number to delete.");

        int index = Integer.parseInt(parts[1]) - 1;
        if (index < 0 || index >= tasks.size()) throw new HowlyException("That task doesn't exist.");

        Task removedTask = tasks.remove(index);
        printHorizontalLine();
        System.out.println(" Noted. I've removed this task:\n  " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printHorizontalLine();
    }

    private static void printHorizontalLine() {
        System.out.println("____________________________________________________________");
    }
}