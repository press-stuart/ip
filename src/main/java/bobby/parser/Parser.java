package bobby.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import bobby.commands.ByeCommand;
import bobby.commands.Command;
import bobby.commands.DeadlineCommand;
import bobby.commands.DeleteCommand;
import bobby.commands.EventCommand;
import bobby.commands.FindCommand;
import bobby.commands.ListCommand;
import bobby.commands.MarkCommand;
import bobby.commands.TodoCommand;
import bobby.commands.UnmarkCommand;
import bobby.exception.BobbyException;

/**
 * Parser for user input.
 */
public class Parser {
    /**
     * Parses a line of user input to extract the command, value and parameters, then creates
     * a Command object encapsulating all information.
     *
     * @param line String containing a line of user input.
     * @return A Command object representing the parsed input.
     * @throws BobbyException If the input is blank, the command is not recognized, or a
     * parameter name is missing.
     */
    public static Command parse(String line) throws BobbyException {
        HashMap<String, String> components = splitIntoComponents(line);
        return createCommandFromComponents(components);
    }

    /**
     * Parses a line of user input to extract Strings corresponding to the command, value and
     * parameters.
     * The command is the first token, using whitespace characters as delimiters. The value is the
     * text between the command and the first parameter, or the end of the string if no parameters
     * exist.
     * Forward slashes `/` define the beginnings of parameters. After removing the forward slash,
     * the first word is the parameter name. All remaining text forms the parameter value.
     * 
     * @param line String containing a line of user input.
     * @return A HashMap with keys "command", "value" and parameter names
     * @throws BobbyException If the input is blank (whitespace only), the command cannot be
     * found, or a parameter name is missing.
     */
    private static HashMap<String, String> splitIntoComponents(String line) throws BobbyException {
        HashMap<String, String> components = new HashMap<>();

        if (line == null || line.isBlank()) {
            throw new BobbyException(Message.MESSAGE_EMPTY_INPUT);
        }

        // Split by one or more whitespace characters followed by '/'
        String[] sections = line.split("\\s+/");

        String commandValueSection = sections[0];
        String[] commandValueTokens = commandValueSection.split("\\s+", 2);

        if (commandValueTokens.length == 0) {
            throw new BobbyException(Message.MESSAGE_INVALID_COMMAND);
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
                throw new BobbyException(Message.MESSAGE_EMPTY_PARAMETER_NAME);
            }

            String parameterName = parts[0];
            String parameterValue = parts.length < 2 ? "" : parts[1];
            components.put(parameterName, parameterValue);
        }

        return components;
    }

    private static Command createCommandFromComponents(HashMap<String, String> components)
            throws BobbyException {
        String commandType = components.get("command").toLowerCase();
        return switch (commandType) {
            case "bye" -> createByeCommand();
            case "deadline" -> createDeadlineCommand(components);
            case "delete" -> createDeleteCommand(components);
            case "event" -> createEventCommand(components);
            case "find" -> createFindCommand(components);
            case "list" -> createListCommand();
            case "mark" -> createMarkCommand(components);
            case "todo" -> createTodoCommand(components);
            case "unmark" -> createUnmarkCommand(components);
            default -> throw new BobbyException(Message.MESSAGE_INVALID_COMMAND);
        };
    }

    private static ByeCommand createByeCommand() {
        return new ByeCommand();
    }

    private static DeadlineCommand createDeadlineCommand(HashMap<String, String> components)
            throws BobbyException {
        String description = components.get("value");
        if (description == null || description.isBlank()) {
            throw new BobbyException(Message.MESSAGE_DEADLINE_EMPTY_DESCRIPTION);
        }

        boolean isDone = components.containsKey("done");

        String by = components.get("by");
        if (by == null || by.isBlank()) {
            throw new BobbyException(Message.MESSAGE_DEADLINE_MISSING_BY);
        }
        LocalDate byDate = parseDate(by);

        return new DeadlineCommand(description, isDone, byDate);
    }

    private static DeleteCommand createDeleteCommand(HashMap<String, String> components)
            throws BobbyException {
        String indexString = components.get("value");
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new BobbyException(Message.MESSAGE_INVALID_TASK_INDEX);
        }
        return new DeleteCommand(index);
    }

    private static EventCommand createEventCommand(HashMap<String, String> components)
            throws BobbyException {
        String description = components.get("value");
        if (description == null || description.isBlank()) {
            throw new BobbyException(Message.MESSAGE_EVENT_EMPTY_DESCRIPTION);
        }

        boolean isDone = components.containsKey("done");

        String from = components.get("from");
        if (from == null || from.isBlank()) {
            throw new BobbyException(Message.MESSAGE_EVENT_MISSING_FROM);
        }
        LocalDate fromDate = parseDate(from);

        String to = components.get("to");
        if (to == null || to.isBlank()) {
            throw new BobbyException(Message.MESSAGE_EVENT_MISSING_TO);
        }
        LocalDate toDate = parseDate(to);

        return new EventCommand(description, isDone, fromDate, toDate);
    }

    private static FindCommand createFindCommand(HashMap<String, String> components)
            throws BobbyException {
        String keyword = components.get("value");
        if (keyword == null || keyword.isBlank()) {
            throw new BobbyException(Message.MESSAGE_FIND_EMPTY_KEYWORD);
        }
        return new FindCommand(keyword);
    }

    private static ListCommand createListCommand() {
        return new ListCommand();
    }

    private static MarkCommand createMarkCommand(HashMap<String, String> components)
            throws BobbyException {
        String indexString = components.get("value");
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new BobbyException(Message.MESSAGE_INVALID_TASK_INDEX);
        }
        return new MarkCommand(index);
    }

    private static TodoCommand createTodoCommand(HashMap<String, String> components)
            throws BobbyException {
        String description = components.get("value");
        if (description == null || description.isBlank()) {
            throw new BobbyException(Message.MESSAGE_TODO_EMPTY_DESCRIPTION);
        }

        boolean isDone = components.containsKey("done");
        return new TodoCommand(description, isDone);
    }

    private static UnmarkCommand createUnmarkCommand(HashMap<String, String> components)
            throws BobbyException {
        String indexString = components.get("value");
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new BobbyException(Message.MESSAGE_INVALID_TASK_INDEX);
        }
        return new UnmarkCommand(index);
    }

    private static LocalDate parseDate(String dateString) throws BobbyException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new BobbyException(Message.MESSAGE_INVALID_DATE_FORMAT);
        }
    }
}
