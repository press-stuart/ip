import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * 
     * If no file exists, a new file is created and an empty TaskList is returned.
     * 
     * @return A TaskList containing all Tasks stored in the file.
     * @throws DukeException
     */
    public TaskList load() throws DukeException {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException ioe) {
                throw new DukeException("Error: Could not create file :(");
            }
        }

        try {
            TaskList taskList = new TaskList();
            List<String> lines = Files.readAllLines(path);
            
            for (String line : lines) {
                // TODO: eliminate code duplication
                HashMap<String, String> inputParts = Parser.parse(line);
                String command = inputParts.get("command");

                if (command.equalsIgnoreCase("todo")) {
                    runTodoCommand(inputParts, taskList);
                } else if (command.equalsIgnoreCase("deadline")) {
                    runDeadlineCommand(inputParts, taskList);
                } else if (command.equalsIgnoreCase("event")) {
                    runEventCommand(inputParts, taskList);
                } else {
                    throw new DukeException("Invalid command");
                }
            }

            return taskList;
        } catch (Exception e) {
            throw new DukeException("Error: Could not load file :(");
        }
    }

    /**
     * Retrieves the Tasks in the given TaskList instance and saves them in the file.
     * 
     * @param taskList List of tasks.
     * @throws DukeException
     */
    public void save(TaskList taskList) throws DukeException {
        try {
            List<String> lines = new ArrayList<>();
            taskList.getAllTasks().forEach(task -> lines.add(task.toCommand()));
            Files.write(path, lines);
        } catch (Exception e) {
            throw new DukeException("Error: Could not save data to file :(");
        }
    }

    private void runTodoCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of a todo cannot be empty!");
        }

        boolean isDone = inputParts.containsKey("done");
        Todo todo = new Todo(description, isDone);
        taskList.addTask(todo);
    }

    private void runDeadlineCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of a deadline cannot be empty!");
        }

        String by = inputParts.get("by");
        if (by == null || by.isEmpty()) {
            throw new DukeException("I couldn't find the deadline!\n"
                    + "(Hint: Use the /by parameter)");
        }

        boolean isDone = inputParts.containsKey("done");
        Deadline deadline = new Deadline(description, isDone, by);
        taskList.addTask(deadline);
    }

    private void runEventCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of an event cannot be empty!");
        }

        String from = inputParts.get("from");
        if (from == null || from.isEmpty()) {
            throw new DukeException("I couldn't find the start time!\n"
                    + "(Hint: Use the /from parameter)");
        }

        String to = inputParts.get("to");
        if (to == null || to.isEmpty()) {
            throw new DukeException("I couldn't find the end time!\n"
                    + "(Hint: Use the /to parameter)");
        }

        boolean isDone = inputParts.containsKey("done");
        Event event = new Event(description, isDone, from, to);
        taskList.addTask(event);
    }
}
