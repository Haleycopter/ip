import java.util.Scanner;

public class Ui {
    private final Scanner scanner;
    private static final String LINE = "____________________________________________________________";

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Howly\n What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(" Oh no. " + message);
        showLine();
    }
}
