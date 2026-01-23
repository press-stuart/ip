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
    private static HashMap<String, String> parse(String line) {
        HashMap<String, String> components = new HashMap<>();

        if (line == null || line.isBlank()) {
            // TODO: exception handling
            return components;
        }

        // Split by one or more whitespace characters followed by '/'
        String[] sections = line.split("\\s+/");

        String commandValueSection = sections[0];
        String[] commandValueTokens = commandValueSection.split("\\s+", 2);

        // TODO: exception handling
        String command = commandValueTokens[0];
        components.put("command", command);

        String value = commandValueTokens.length < 2 ? "" : commandValueTokens[1];
        components.put("value", value);

        // Process additional parameters
        for (int i = 1; i < sections.length; i++) {
            String parameterSection = sections[i];
            String[] parts = parameterSection.split("\\s+", 2);

            // TODO: exception handling
            String parameterName = parts[0];
            String parameterValue = parts.length < 2 ? "" : parts[1];
            components.put(parameterName, parameterValue);
        }

        return components;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        taskList = new TaskList();

        printMessage("Hello! I'm Bobby.\nWhat can I do for you?");

        while (true) {
            String inputLine = sc.nextLine();
            String[] tokens = inputLine.split("\\s+");
            String command = tokens[0];

            if (inputLine.equalsIgnoreCase("bye")) {
                break;
            } else if (inputLine.equalsIgnoreCase("list")) {
                printMessage(taskList.toString());
            } else if (command.equalsIgnoreCase("mark")) {
                int taskIndex = Integer.valueOf(tokens[1]);
                Task task = taskList.getTask(taskIndex);
                task.markDone();
                printMessage("Marked this task as done:\n  " + task);
            } else if (command.equalsIgnoreCase("unmark")) {
                int taskIndex = Integer.valueOf(tokens[1]);
                Task task = taskList.getTask(taskIndex);
                task.unmarkDone();
                printMessage("Marked this task as not done:\n  " + task);
            } else {
                Task task = new Task(inputLine);
                taskList.addTask(task);
                printMessage("added: " + inputLine);
            }
        }

        printMessage("Bye! Hope to see you again soon!");

        sc.close();
    }
}
