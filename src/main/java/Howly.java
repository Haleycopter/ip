import java.util.Scanner;

public class Howly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Howly");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[100]; //fixed-size array to store tasks
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            try {
                String userInput = scanner.nextLine().trim();
                if (userInput.isEmpty()) continue;
                if (userInput.equals("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break;
                }

                if (userInput.equals("list")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("mark")) {
                    handleMarkUnmark(userInput, tasks, taskCount, true);
                } else if (userInput.startsWith("unmark")) {
                    handleMarkUnmark(userInput, tasks, taskCount, false);
                } else if (userInput.startsWith("todo") || userInput.startsWith("deadline")
                        || userInput.startsWith("event")) {
                    Task newTask = createTask(userInput);
                    tasks[taskCount] = newTask;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else {
                    throw new HowlyException("I'm sorry, please enter a valid task: todo, deadline or event");
                }
            } catch (HowlyException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Oh no. " + e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Please provide a valid task number.");
                System.out.println("____________________________________________________________");
            }
        }
    }

    private static Task createTask(String input) throws HowlyException {
        if (input.startsWith("todo")) {
            String desc = input.replaceFirst("todo", "").trim();
            if (desc.isEmpty()) {
                throw new HowlyException("You have to specify the task you want to do after the 'todo' command.");
            }
            return new ToDo(desc);

        } else if (input.startsWith("deadline")) {
            String content = input.replaceFirst("deadline", "").trim();
            if (content.isEmpty()) {
                throw new HowlyException("You have to specify the deadline using /by after the 'deadline' command.");
            }
            if (!content.contains("/by")) {
                throw new HowlyException("A deadline must include a time using /by. Eg /by 6th August");
            }
            String[] parts = content.split("/by", 2);
            return new Deadline(parts[0].trim(), parts[1].trim());

        } else { // event
            String content = input.replaceFirst("event", "").trim();
            if (content.isEmpty()) {
                throw new HowlyException("You have to specify the event start and end time after the 'event' command.");
            }
            if (!content.contains("/from") || !content.contains("/to")) {
                throw new HowlyException("An event must include /from and /to times. Eg /from 6th August 2pm /to 4pm");
            }
            String[] parts = content.split("/from|/to");
            return new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
    }

    private static void handleMarkUnmark(String input, Task[] tasks, int count, boolean isMark) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new HowlyException("Please specify a task number.");
        }
        int index = Integer.parseInt(parts[1]) - 1;
        if (index < 0 || index >= count) {
            throw new HowlyException("Task number " + (index + 1) + " does not exist.");
        }

        if (isMark) {
            tasks[index].markAsDone();
            System.out.println("____________________________________________________________");
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            tasks[index].markAsNotDone();
            System.out.println("____________________________________________________________");
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + tasks[index]);
        System.out.println("____________________________________________________________");
    }
}