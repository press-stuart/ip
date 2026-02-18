package bobby;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import javafx.application.Platform;

import bobby.exception.BobbyException;
import bobby.parser.Message;
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
        return Message.MESSAGE_INTRO;
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
                throw new BobbyException(Message.MESSAGE_INVALID_COMMAND);
            }
        } catch (Exception e) {
            response = e.getMessage();
        }

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
        return Message.MESSAGE_EXIT;
    }

    private String runListCommand() {
        return String.format(Message.MESSAGE_LIST_FORMAT, taskList);
    }

    private String runMarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        return String.format(Message.MESSAGE_MARK_FORMAT, task);
    }

    private String runUnmarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        return String.format(Message.MESSAGE_UNMARK_FORMAT, task);
    }

    private String runDeleteCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task deletedTask = taskList.deleteTask(taskIndex);
        return String.format(
                Message.MESSAGE_DELETE_FORMAT,
                deletedTask.toString(), taskList.getSize());
    }

    private String runTodoCommand(HashMap<String, String> inputParts) throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_TODO_EMPTY_DESCRIPTION);
        }

        boolean isDone = inputParts.containsKey("done");
        TodoTask todo = new TodoTask(description, isDone);
        taskList.addTask(todo);
        return String.format(Message.MESSAGE_ADD_FORMAT, todo, taskList.getSize());
    }

    private String runDeadlineCommand(HashMap<String, String> inputParts) throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_DEADLINE_EMPTY_DESCRIPTION);
        }

        String by = inputParts.get("by");
        if (by == null || by.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_DEADLINE_MISSING_BY);
        }

        LocalDate byDate;

        try {
            byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new BobbyException(Message.MESSAGE_INVALID_DATE_FORMAT);
        }

        boolean isDone = inputParts.containsKey("done");
        DeadlineTask deadline = new DeadlineTask(description, isDone, byDate);
        taskList.addTask(deadline);
        return String.format(Message.MESSAGE_ADD_FORMAT, deadline, taskList.getSize());
    }

    private String runEventCommand(HashMap<String, String> inputParts) throws BobbyException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_EVENT_EMPTY_DESCRIPTION);
        }

        String from = inputParts.get("from");
        if (from == null || from.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_EVENT_MISSING_FROM);
        }

        String to = inputParts.get("to");
        if (to == null || to.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_EVENT_MISSING_TO);
        }

        LocalDate fromDate;
        LocalDate toDate;

        try {
            fromDate = LocalDate.parse(from);
            toDate = LocalDate.parse(to);
        } catch (Exception e) {
            throw new BobbyException(Message.MESSAGE_INVALID_DATE_FORMAT);
        }

        boolean isDone = inputParts.containsKey("done");
        EventTask event = new EventTask(description, isDone, fromDate, toDate);
        taskList.addTask(event);
        return String.format(Message.MESSAGE_ADD_FORMAT, event, taskList.getSize());
    }

    private String runFindCommand(HashMap<String, String> inputParts) throws BobbyException {
        String keyword = inputParts.get("value");
        if (keyword == null || keyword.isEmpty()) {
            throw new BobbyException(Message.MESSAGE_FIND_EMPTY_KEYWORD);
        }

        TaskList foundTasks = taskList.findTasks(keyword);
        return String.format(Message.MESSAGE_FIND_FORMAT, foundTasks);
    }
}
