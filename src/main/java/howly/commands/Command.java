package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException;

    public boolean isExit() {
        return false;
    }
}