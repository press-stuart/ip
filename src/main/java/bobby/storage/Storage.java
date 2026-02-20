package bobby.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import bobby.commands.Command;
import bobby.exception.BobbyException;
import bobby.parser.Message;
import bobby.parser.Parser;
import bobby.task.TaskList;

/**
 * Manager for the file storing the list of tasks.
 */
public class Storage {
    /** Path of the file storing the list of tasks. */
    public static final String STORAGE_PATH = "tasks.txt";

    public final Path path;

    public Storage() {
        path = Paths.get(STORAGE_PATH);
    }

    /**
     * Loads the contents of the file storing the list of Tasks into a TaskList.
     * If no file exists, a new file is created and an empty TaskList is returned.
     *
     * @return A TaskList containing all Tasks stored in the file.
     * @throws BobbyException If the file did not exist and could not be created,
     *     or if an error occurred while reading the file.
     */
    public TaskList load() throws BobbyException {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException ioe) {
                throw new BobbyException(Message.MESSAGE_STORAGE_FILE_CREATION_ERROR);
            }
        }

        assert Files.exists(path) : "File should exist after creation";

        try {
            TaskList taskList = new TaskList();
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                Command command = Parser.parse(line);
                command.execute(taskList);
            }

            return taskList;
        } catch (Exception e) {
            throw new BobbyException(Message.MESSAGE_STORAGE_FILE_LOAD_ERROR);
        }
    }

    /**
     * Retrieves the Tasks in the given TaskList instance and saves them in the file.
     *
     * @param taskList List of tasks.
     * @throws BobbyException If an error occurs while writing to the file.
     */
    public void save(TaskList taskList) throws BobbyException {
        try {
            List<String> lines = new ArrayList<>();
            taskList.getAllTasks().forEach(task -> lines.add(task.toCommand()));
            Files.write(path, lines);
        } catch (Exception e) {
            throw new BobbyException(Message.MESSAGE_STORAGE_FILE_SAVE_ERROR);
        }
    }
}
