package howly.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import howly.tasks.*;
import howly.common.HowlyException;
/**
 * Manages persistent data storage for the Howly application.
 * This class handles reading tasks from and writing tasks to a local hard disk file,
 * ensuring that the task list is preserved between application sessions.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     * <p>
     * If the file does not exist, an empty list is returned. If the file is corrupted
     * or uses an incorrect format, the method catches the exception and returns
     * a fresh list to avoid application crashes.
     * </p>
     *
     * @return An {@code ArrayList} of {@code Task} objects reconstructed from the file.
     * @throws HowlyException If a major error occurs during file access.
     */
    public ArrayList<Task> load() throws HowlyException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File f = new File(filePath);
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

    /**
     * Saves the current list of tasks to the data file.
     * <p>
     * This method ensures the parent directory exists before writing. It iterates
     * through the provided task list and converts each task into its machine-readable
     * file format using {@link Task#toFileFormat()}.
     * </p>
     *
     * @param tasks The {@code ArrayList} of tasks to be persisted to disk.
     * @throws HowlyException If an I/O error occurs while writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws HowlyException {
        try {
            File f = new File(filePath);
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
}