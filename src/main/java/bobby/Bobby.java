package bobby;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import bobby.exception.DukeException;
import bobby.parser.Parser;
import bobby.storage.Storage;
import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Task;
import bobby.task.TaskList;
import bobby.task.Todo;
import bobby.ui.Ui;

public class Bobby {
    /** List of user tasks. */
    private TaskList taskList;

    /** Manager for the file storing the list of tasks. */
    private Storage storage;

    /** User interface. */
    private Ui ui;

    public static void main(String[] args) {
        Bobby bobby = new Bobby();
        bobby.runLoopUntilExit();
        bobby.cleanUpAfterExit();
    }

    public Bobby() {
        ui = new Ui();
        storage = new Storage();
        try {
            taskList = storage.load();
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
            taskList = new TaskList();
        }
    }

    /**
     * Reads and executes user commands until the exit command is found.
     */
    public void runLoopUntilExit() {
        boolean isFinished = false;

        ui.printMessage("Hello! I'm Bobby.\nWhat can I do for you?");

        while (!isFinished) {
            String inputLine = ui.readCommand();

            try {
                HashMap<String, String> inputParts = Parser.parse(inputLine);
                String command = inputParts.get("command");

                if (command.equalsIgnoreCase("bye")) {
                    isFinished = true;
                } else if (command.equalsIgnoreCase("list")) {
                    runListCommand();
                } else if (command.equalsIgnoreCase("mark")) {
                    runMarkCommand(inputParts);
                } else if (command.equalsIgnoreCase("unmark")) {
                    runUnmarkCommand(inputParts);
                } else if (command.equalsIgnoreCase("delete")) {
                    runDeleteCommand(inputParts);
                } else if (command.equalsIgnoreCase("todo")) {
                    runTodoCommand(inputParts);
                } else if (command.equalsIgnoreCase("deadline")) {
                    runDeadlineCommand(inputParts);
                } else if (command.equalsIgnoreCase("event")) {
                    runEventCommand(inputParts);
                } else {
                    throw new DukeException("I don't know what that means :(\n"
                            + "(Hint: Use one of the recognised commands)");
                }
            } catch (Exception e) {
                ui.printMessage(e.getMessage());
            }
        }

        ui.printMessage("Bye! Hope to see you again soon!");
    }

    private void cleanUpAfterExit() {
        try {
            storage.save(taskList);
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
        }
        
        ui.close();
    }

    private void runListCommand() {
        ui.printMessage("Tasks in your list:\n" + taskList.toString());
    }

    private void runMarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        ui.printMessage("Marked this task as done:\n  " + task);
    }

    private void runUnmarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        ui.printMessage("Marked this task as not done:\n  " + task);
    }

    private void runDeleteCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task deletedTask = taskList.deleteTask(taskIndex);
        ui.printMessage(String.format(
                "Deleted this task:\n  %s\nNow you have %d tasks in the list.",
                deletedTask.toString(), taskList.getSize()));
    }

    private void runTodoCommand(HashMap<String, String> inputParts) throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of a todo cannot be empty!");
        }

        boolean isDone = inputParts.containsKey("done");
        Todo todo = new Todo(description, isDone);
        taskList.addTask(todo);
        ui.printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                todo.toString(), taskList.getSize()));
    }

    private void runDeadlineCommand(HashMap<String, String> inputParts) throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of a deadline cannot be empty!");
        }

        String by = inputParts.get("by");
        if (by == null || by.isEmpty()) {
            throw new DukeException("I couldn't find the deadline!\n"
                    + "(Hint: Use the /by parameter)");
        }

        LocalDate byDate;

        try {
            byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new DukeException("I don't understand this date format!\n"
                    + "(Hint: Use the yyyy-mm-dd format)");
        }

        boolean isDone = inputParts.containsKey("done");
        Deadline deadline = new Deadline(description, isDone, byDate);
        taskList.addTask(deadline);
        ui.printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                deadline.toString(), taskList.getSize()));
    }

    private void runEventCommand(HashMap<String, String> inputParts) throws DukeException {
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

        LocalDate fromDate;
        LocalDate toDate;

        try {
            fromDate = LocalDate.parse(from);
            toDate = LocalDate.parse(to);
        } catch (Exception e) {
            throw new DukeException("I don't understand this date format!\n"
                    + "(Hint: Use the yyyy-mm-dd format)");
        }

        boolean isDone = inputParts.containsKey("done");
        Event event = new Event(description, isDone, fromDate, toDate);
        taskList.addTask(event);
        ui.printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                event.toString(), taskList.getSize()));
    }
}
