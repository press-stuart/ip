package bobby;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import javafx.application.Platform;

import bobby.exception.BobbyException;
import bobby.parser.Parser;
import bobby.storage.Storage;
import bobby.task.DeadlineTask;
import bobby.task.EventTask;
import bobby.task.Task;
import bobby.task.TaskList;
import bobby.task.TodoTask;
import bobby.ui.Ui;

/**
 * Main class for Bobby the chatbot. Drives the initialization, user command loop
 * and cleanup on exit.
 */
public class Bobby {
    /** List of user tasks. */
    private TaskList taskList;

    /** Manager for the file storing the list of tasks. */
    private Storage storage;

    /** User interface. */
    private Ui ui;

    /**
     * Initializes Bobby.
     */
    public Bobby() {
        ui = new Ui();
        storage = new Storage();
        loadTasks();
    }

    /**
     * Loads tasks from storage, initializing an empty task list on failure.
     */
    public void loadTasks() {
        try {
            taskList = storage.load();
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
            taskList = new TaskList();
        }
    }

    /**
     * Gets the introductory message displayed at startup.
     */
    public String getIntroMessage() {
        return "Hello! I'm Bobby.\nWhat can I do for you?";
    }

    /**
     * Executes a user command and returns the response.
     *
     * @param input The user command input.
     * @return The response after executing the command.
     */
    public String executeCommandAndGetResponse(String input) {
        String response;

        try {
            HashMap<String, String> inputParts = Parser.parse(input);
            String command = inputParts.get("command");

            if (command.equalsIgnoreCase("bye")) {
                response = runByeCommand();
            } else if (command.equalsIgnoreCase("list")) {
                response = runListCommand();
            } else if (command.equalsIgnoreCase("mark")) {
                response = runMarkCommand(inputParts);
            } else if (command.equalsIgnoreCase("unmark")) {
                response = runUnmarkCommand(inputParts);
            } else if (command.equalsIgnoreCase("delete")) {
                response = runDeleteCommand(inputParts);
            } else if (command.equalsIgnoreCase("todo")) {
                response = runTodoCommand(inputParts);
            } else if (command.equalsIgnoreCase("deadline")) {
                response = runDeadlineCommand(inputParts);
            } else if (command.equalsIgnoreCase("event")) {
                response = runEventCommand(inputParts);
            } else if (command.equalsIgnoreCase("find")) {
                response = runFindCommand(inputParts);
            } else {
                throw new BobbyException("I don't know what that means :(\n"
                        + "(Hint: Use one of the recognised commands)");
            }
        } catch (Exception e) {
            response = e.getMessage();
        }

        assert response != null : "Response should not be null";
        return response;
    }

    /**
     * Attempts to save tasks to storage and closes the UI.
     */
    public void cleanUpAfterExit() {
        try {
            storage.save(taskList);
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
        }
        
        ui.close();
    }

    private String runByeCommand() {
        Platform.exit();
        return "Bye! Hope to see you again soon!";
    }

    private String runListCommand() {
        return "Tasks in your list:\n" + taskList.toString();
    }

    private String runMarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        return "Marked this task as done:\n  " + task;
    }

    private String runUnmarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        return "Marked this task as not done:\n  " + task;
    }

    private String runDeleteCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task deletedTask = taskList.deleteTask(taskIndex);
        return String.format(
                "Deleted this task:\n  %s\nNow you have %d tasks in the list.",
                deletedTask.toString(), taskList.getSize());
    }

    private String runTodoCommand(HashMap<String, String> inputParts) throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException("The description of a todo cannot be empty!");
        }

        boolean isDone = inputParts.containsKey("done");
        TodoTask todo = new TodoTask(description, isDone);
        taskList.addTask(todo);
        return String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                todo.toString(), taskList.getSize());
    }

    private String runDeadlineCommand(HashMap<String, String> inputParts) throws BobbyException {
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
        return String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                deadline.toString(), taskList.getSize());
    }

    private String runEventCommand(HashMap<String, String> inputParts) throws BobbyException {
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
        return String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                event.toString(), taskList.getSize());
    }

    private String runFindCommand(HashMap<String, String> inputParts) throws BobbyException {
        String keyword = inputParts.get("value");
        if (keyword == null || keyword.isEmpty()) {
            throw new BobbyException("The keyword for finding tasks cannot be empty!");
        }

        TaskList foundTasks = taskList.findTasks(keyword);
        return "Here are the matching tasks in your list:\n" + foundTasks.toString();
    }
}
