import java.util.Scanner;

public class Howly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String welcomeMsg = "____________________________________________________________\n"
                + " Hello! I'm Howly\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        System.out.println(welcomeMsg);

        Task[] tasks = new Task[100]; //fixed-size array to store tasks
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim();
            if (userInput.equals("bye")) {
                String exitMsg = "____________________________________________________________\n"
                        + " Bye. Hope to see you again soon!\n"
                        + "____________________________________________________________\n";
                System.out.println(exitMsg);
                break;
            }

            if (userInput.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
                continue;
            }

            if (userInput.startsWith("mark ")) {
                int index = Integer.parseInt(userInput.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                }
                continue;
            }

            if (userInput.startsWith("unmark ")) {
                int index = Integer.parseInt(userInput.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                }
                continue;
            }

            //add new tasks
            Task newTask = null;
            if (userInput.startsWith("todo ")) {
                String desc = userInput.substring(5).trim();
                newTask = new ToDo(desc);
            } else if (userInput.startsWith("deadline ")) {
                String[] parts = userInput.substring(9).split("/by", 2);
                if (parts.length == 2) {
                    String desc = parts[0].trim();
                    String by = parts[1].trim();
                    newTask = new Deadline(desc, by);
                }
            } else if (userInput.startsWith("event ")) {
                String[] parts = userInput.substring(6).split("/from|/to");
                if (parts.length == 3) {
                    String desc = parts[0].trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();
                    newTask = new Event(desc, from, to);
                }
            }
            if (newTask != null) {
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            }
        }
    }
}
