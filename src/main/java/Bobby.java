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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        taskList = new TaskList();

        printMessage("Hello! I'm Bobby.\nWhat can I do for you?");

        while (true) {
            String inputLine = sc.nextLine();

            if (inputLine.equalsIgnoreCase("bye")) {
                break;
            } else if (inputLine.equalsIgnoreCase("list")) {
                printMessage(taskList.toString());
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
