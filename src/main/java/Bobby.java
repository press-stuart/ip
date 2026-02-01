import java.util.HashMap;
import java.util.Scanner;

public class Bobby {
    /** Number of spaces to add before each horizontal line frame. */
    private static final int FRAME_INDENTATION = 4;

    /** Number of spaces to add before each line of text in the message. */
    private static final int TEXT_INDENTATION = 5;

    /** Length of each horizontal line frame. */
    private static final int FRAME_LENGTH = 67;

    /** Scanner to read user input. */
    private Scanner sc;

    /** List of user tasks. */
    private TaskList taskList;

    /** Manager for the file storing the list of tasks. */
    private Storage storage;

    public static void main(String[] args) {
        Bobby bobby = new Bobby();
        bobby.runLoopUntilExit();
        bobby.cleanUpAfterExit();
    }

    public Bobby() {
        sc = new Scanner(System.in);
        storage = new Storage();
        try {
            taskList = storage.load();
        } catch (Exception e) {
            printMessage(e.getMessage());
            taskList = new TaskList();
        }
    }

    /**
     * Reads and executes user commands until the exit command is found.
     */
    public void runLoopUntilExit() {
        boolean isFinished = false;

        printMessage("Hello! I'm Bobby.\nWhat can I do for you?");

        while (!isFinished) {
            String inputLine = sc.nextLine();

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
                printMessage(e.getMessage());
            }
        }

        printMessage("Bye! Hope to see you again soon!");
    }

    private void cleanUpAfterExit() {
        try {
            storage.save(taskList);
        } catch (Exception e) {
            printMessage(e.getMessage());
        }
        
        sc.close();
    }

    /**
     * Prints the given message with indentation and horizontal lines above and below the message.
     */
    private static void printMessage(String message) {
        String frameIndent = " ".repeat(FRAME_INDENTATION);
        String textIndent = " ".repeat(TEXT_INDENTATION);
        String horizontalLine = "_".repeat(FRAME_LENGTH);

        String[] messageLines = message.split("\n");

        System.out.println(frameIndent + horizontalLine);

        for (String line : messageLines) {
            System.out.println(textIndent + line);
        }

        System.out.println(frameIndent + horizontalLine);
        System.out.println();
    }

    private void runListCommand() {
        printMessage("Tasks in your list:\n" + taskList.toString());
    }

    private void runMarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        printMessage("Marked this task as done:\n  " + task);
    }

    private void runUnmarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        printMessage("Marked this task as not done:\n  " + task);
    }

    private void runDeleteCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task deletedTask = taskList.deleteTask(taskIndex);
        printMessage(String.format(
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
        printMessage(String.format(
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

        boolean isDone = inputParts.containsKey("done");
        Deadline deadline = new Deadline(description, isDone, by);
        taskList.addTask(deadline);
        printMessage(String.format(
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

        boolean isDone = inputParts.containsKey("done");
        Event event = new Event(description, isDone, from, to);
        taskList.addTask(event);
        printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                event.toString(), taskList.getSize()));
    }
}
