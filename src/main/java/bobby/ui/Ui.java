package bobby.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Represents the user interface of the chatbot. Manages input and output.
 */
public class Ui {
    /** Number of spaces to add before each horizontal line frame. */
    private static final int FRAME_INDENTATION = 4;

    /** Number of spaces to add before each line of text in the message. */
    private static final int TEXT_INDENTATION = 5;

    /** Length of each horizontal line frame. */
    private static final int FRAME_LENGTH = 67;

    private Scanner scanner;
    private PrintStream printStream;
    
    public Ui(InputStream is, PrintStream ps) {
        this.scanner = new Scanner(is);
        this.printStream = ps;
    }

    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Closes the scanner. To be run when the user exits.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Reads one line of input representing a command from the user.
     * 
     * @return User command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the given message with indentation and horizontal lines above and below the message.
     */
    public void printMessage(String message) {
        String frameIndent = " ".repeat(FRAME_INDENTATION);
        String textIndent = " ".repeat(TEXT_INDENTATION);
        String horizontalLine = "_".repeat(FRAME_LENGTH);

        String[] messageLines = message.split("\n");

        printStream.println(frameIndent + horizontalLine);
        for (String line : messageLines) {
            printStream.println(textIndent + line);
        }
        printStream.println(frameIndent + horizontalLine);
        printStream.println();
    }
}
