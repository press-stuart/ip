import java.util.HashMap;
import java.util.Scanner;

public class Bobby {
    /** Number of spaces to add before each horizontal line frame. */
    private static final int FRAME_INDENTATION = 4;

    /** Number of spaces to add before each line of text in the message. */
    private static final int TEXT_INDENTATION = 5;

    /** Length of each horizontal line frame. */
    private static final int FRAME_LENGTH = 67;

    /** List of user tasks. */
    private static TaskList taskList;

    /** Indicates whether the user finished interaction. */
    private static boolean isFinished;

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

    /**
     * Parses a line of user input to extract the command, value and parameters.
     * 
     * The command is the first word, using whitespace characters as delimiters. The value is the
     * text between the command and the first parameter, or the end of the string if no parameters
     * exist.
     * 
     * Forward slashes `/` define the beginnings of parameters. After removing the forward slash,
     * the first word is the parameter name. All remaining text forms the parameter value.
     * 
     * @param line String containing a line of user input.
     * @return A HashMap
     */
    private static HashMap<String, String> parse(String line) throws DukeException {
        HashMap<String, String> components = new HashMap<>();

        if (line == null || line.isBlank()) {
            throw new DukeException("Did you say something?\n"
                    + "(Hint: Input cannot be blank)");
        }

        // Split by one or more whitespace characters followed by '/'
        String[] sections = line.split("\\s+/");

        String commandValueSection = sections[0];
        String[] commandValueTokens = commandValueSection.split("\\s+", 2);

        if (commandValueTokens.length == 0) {
            throw new DukeException("I don't know what that means :(\n"
                    + "(Hint: Use one of the recognised commands)");
        }

        String command = commandValueTokens[0];
        components.put("command", command);

        String value = commandValueTokens.length < 2 ? "" : commandValueTokens[1];
        components.put("value", value);

        // Process additional parameters
        for (int i = 1; i < sections.length; i++) {
            String parameterSection = sections[i];
            String[] parts = parameterSection.split("\\s+", 2);

            if (parts.length == 0 || parts[0].isBlank()) {
                throw new DukeException("I couldn't find a parameter name :(\n"
                        + "(Hint: Don't leave a space after the '/')");
            }

            String parameterName = parts[0];
            String parameterValue = parts.length < 2 ? "" : parts[1];
            components.put(parameterName, parameterValue);
        }

        return components;
    }

    private static void runByeCommand() {
        isFinished = true;
    }

    private static void runListCommand() {
        printMessage("Tasks in your list:\n" + taskList.toString());
    }

    private static void runMarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        printMessage("Marked this task as done:\n  " + task);
    }

    private static void runUnmarkCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        printMessage("Marked this task as not done:\n  " + task);
    }

    private static void runDeleteCommand(HashMap<String, String> inputParts) {
        int taskIndex = Integer.valueOf(inputParts.get("value"));
        Task deletedTask = taskList.deleteTask(taskIndex);
        printMessage(String.format(
                "Deleted this task:\n  %s\nNow you have %d tasks in the list.",
                deletedTask.toString(), taskList.getSize()));
    }

    private static void runTodoCommand(HashMap<String, String> inputParts)
            throws DukeException {
        String description = inputParts.get("value");
        if (description == null || description.isEmpty()) {
            throw new DukeException("The description of a todo cannot be empty!");
        }

        Todo todo = new Todo(description);
        taskList.addTask(todo);
        printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                todo.toString(), taskList.getSize()));
    }

    private static void runDeadlineCommand(HashMap<String, String> inputParts)
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

        Deadline deadline = new Deadline(description, by);
        taskList.addTask(deadline);
        printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                deadline.toString(), taskList.getSize()));
    }

    private static void runEventCommand(HashMap<String, String> inputParts)
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

        Event event = new Event(description, from, to);
        taskList.addTask(event);
        printMessage(String.format(
                "Added this task:\n  %s\nNow you have %d tasks in the list.",
                event.toString(), taskList.getSize()));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        taskList = new TaskList();
        isFinished = false;

        printMessage("Hello! I'm Bobby.\nWhat can I do for you?");

        while (!isFinished) {
            String inputLine = sc.nextLine();

            try {
                HashMap<String, String> inputParts = parse(inputLine);
                String command = inputParts.get("command");

                if (command.equalsIgnoreCase("bye")) {
                    runByeCommand();
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

        sc.close();
    }
}
