import java.util.Scanner;

public class Howly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String welcomeMsg = "____________________________________________________________\n"
                + " Hello! I'm Howly\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        System.out.println(welcomeMsg);

        String[] tasks = new String[100]; //fixed-size array to store tasks
        int taskCount = 0;

        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                String exitMsg = "____________________________________________________________\n"
                        + " Bye. Hope to see you again soon!\n"
                        + "____________________________________________________________\n";
                System.out.println(exitMsg);
                break;
            }

            if (userInput.equals("list")) {
                System.out.println("____________________________________________________________");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else {
                tasks[taskCount] = userInput;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println(" added: " + userInput);
                System.out.println("____________________________________________________________");
            }
        }
    }
}
