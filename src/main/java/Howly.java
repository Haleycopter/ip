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

        while (true) {
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
            } else if (userInput.startsWith("mark ")) {
                int index = Integer.parseInt(userInput.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                }
            } else if (userInput.startsWith("unmark ")) {
                int index = Integer.parseInt(userInput.split(" ")[1]) - 1;
                if (index >= 0 && index < taskCount) {
                    tasks[index].markAsNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                }
            } else { //add a new task
                tasks[taskCount] = new Task(userInput);
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println(" added: " + userInput);
                System.out.println("____________________________________________________________");
            }
        }
    }
}
