import java.util.Scanner;

public class Howly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String welcomeMsg = "____________________________________________________________\n"
                + " Hello! I'm Howly\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        System.out.println(welcomeMsg);

        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                String exitMsg = "____________________________________________________________\n"
                        + " Bye. Hope to see you again soon!\n"
                        + "____________________________________________________________";
                System.out.println(exitMsg);
                break;
            }
            String echoMsg = "____________________________________________________________\n"
                    + " " + userInput + "\n"
                    + "____________________________________________________________";
            System.out.println(echoMsg);
        }
    }
}
