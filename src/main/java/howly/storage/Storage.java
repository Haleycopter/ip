package howly.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import howly.tasks.*;
import howly.common.HowlyException;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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