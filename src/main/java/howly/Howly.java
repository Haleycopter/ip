package howly;

import howly.commands.Command;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.ui.Ui;
import java.io.File;

public class Howly {
    private final Storage storage;
    private final Ui ui;
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
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand().trim();
                if (fullCommand.isEmpty()) {
                    continue;
                }
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (HowlyException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please provide a valid task number.");
            }
        }
    }

    public static void main(String[] args) {
        new Howly("data" + File.separator + "howly.txt").run();
    }
}