package bobby.storage;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bobby.exception.BobbyException;
import bobby.parser.Parser;
import bobby.task.DeadlineTask;
import bobby.task.EventTask;
import bobby.task.TaskList;
import bobby.task.TodoTask;

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
     * or if an error occurred while reading the file.
     */
    public TaskList load() throws BobbyException {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException ioe) {
                throw new BobbyException("Error: Could not create file :(");
            }
        }

        assert Files.exists(path) : "File should exist after creation";

        try {
            TaskList taskList = new TaskList();
            List<String> lines = Files.readAllLines(path);
            
            for (String line : lines) {
                HashMap<String, String> inputParts = Parser.parse(line);
                String command = inputParts.get("command");

                if (command.equalsIgnoreCase("todo")) {
                    runTodoCommand(inputParts, taskList);
                } else if (command.equalsIgnoreCase("deadline")) {
                    runDeadlineCommand(inputParts, taskList);
                } else if (command.equalsIgnoreCase("event")) {
                    runEventCommand(inputParts, taskList);
                } else {
                    throw new BobbyException("Invalid command");
                }
            }

            return taskList;
        } catch (Exception e) {
            throw new BobbyException("Error: Could not load file :(");
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
            throw new BobbyException("Error: Could not save data to file :(");
        }
    }

    private void runTodoCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException("The description of a todo cannot be empty!");
        }

        boolean isDone = inputParts.containsKey("done");
        TodoTask todo = new TodoTask(description, isDone);
        taskList.addTask(todo);
    }

    private void runDeadlineCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException("The description of a deadline cannot be empty!");
        }

        String by = inputParts.get("by");
        if (by == null || by.isEmpty()) {
            throw new BobbyException("I couldn't find the deadline!\n"
                    + "(Hint: Use the /by parameter)");
        }

        LocalDate byDate;

        try {
            byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new BobbyException("I don't understand this date format!\n"
                    + "(Hint: Use the yyyy-mm-dd format)");
        }

        boolean isDone = inputParts.containsKey("done");
        DeadlineTask deadline = new DeadlineTask(description, isDone, byDate);
        taskList.addTask(deadline);
    }

    private void runEventCommand(HashMap<String, String> inputParts, TaskList taskList)
            throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException("The description of an event cannot be empty!");
        }

        String from = inputParts.get("from");
        if (from == null || from.isEmpty()) {
            throw new BobbyException("I couldn't find the start time!\n"
                    + "(Hint: Use the /from parameter)");
        }

        String to = inputParts.get("to");
        if (to == null || to.isEmpty()) {
            throw new BobbyException("I couldn't find the end time!\n"
                    + "(Hint: Use the /to parameter)");
        }

        LocalDate fromDate;
        LocalDate toDate;

        try {
            fromDate = LocalDate.parse(from);
            toDate = LocalDate.parse(to);
        } catch (Exception e) {
            throw new BobbyException("I don't understand this date format!\n"
                    + "(Hint: Use the yyyy-mm-dd format)");
        }

        boolean isDone = inputParts.containsKey("done");
        EventTask event = new EventTask(description, isDone, fromDate, toDate);
        taskList.addTask(event);
    }
}
