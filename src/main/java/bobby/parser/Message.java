package bobby.parser;

/**
 * Container for all messages used by the program.
 */
public class Message {
    public static final String MESSAGE_INTRO = """
            Hello! I'm Bobby.
            What can I do for you?""";
    public static final String MESSAGE_EXIT = """
            Bye! Hope to see you again soon!""";

    public static final String MESSAGE_LIST_FORMAT = """
            Here are the tasks in your list:
            %1$s""";
    public static final String MESSAGE_LIST_EMPTY = """
            You don't have any tasks!""";

    public static final String MESSAGE_MARK_FORMAT = """
            Marked this task as done:
              %1$s""";
    public static final String MESSAGE_UNMARK_FORMAT = """
            Marked this task as not done:
              %1$s""";

    public static final String MESSAGE_ADD_FORMAT = """
            Added this task:
              %1$s
            Now you have %2$d task(s) in the list.""";
    public static final String MESSAGE_DELETE_FORMAT = """
            Deleted this task:
              %1$s
            Now you have %2$d task(s) in the list.""";

    public static final String MESSAGE_TODO_EMPTY_DESCRIPTION = """
            The description of a todo cannot be empty! :(""";

    public static final String MESSAGE_DEADLINE_EMPTY_DESCRIPTION = """
            The description of a deadline cannot be empty! :(""";
    public static final String MESSAGE_DEADLINE_MISSING_BY = """
            I couldn't find the deadline! :(
            (Hint: Use the /by parameter)""";

    public static final String MESSAGE_DOAFTER_EMPTY_DESCRIPTION = """
            The description of a do-after task cannot be empty! :(""";
    public static final String MESSAGE_DOAFTER_MISSING_AFTER = """
            I couldn't find the date after which the task should be done! :(
            (Hint: Use the /after parameter)""";

    public static final String MESSAGE_EVENT_EMPTY_DESCRIPTION = """
            The description of an event cannot be empty! :(""";
    public static final String MESSAGE_EVENT_MISSING_FROM = """
            I couldn't find the start time! :(
            (Hint: Use the /from parameter)""";
    public static final String MESSAGE_EVENT_MISSING_TO = """
            I couldn't find the end time! :(
            (Hint: Use the /to parameter)""";

    public static final String MESSAGE_FIND_FORMAT = """
            Here are the matching tasks in your list:
            %1$s""";
    public static final String MESSAGE_FIND_NO_MATCH = """
            You don't have any matching tasks!""";
    public static final String MESSAGE_FIND_EMPTY_KEYWORD = """
            The keyword for finding tasks cannot be empty! :(""";

    public static final String MESSAGE_EMPTY_INPUT = """
            Did you say something? :(
            (Hint: Input cannot be blank)""";
    public static final String MESSAGE_EMPTY_PARAMETER_NAME = """
            I couldn't find a parameter name :(
            (Hint: Don't leave a space after the '/')""";

    public static final String MESSAGE_INVALID_COMMAND = """
            I don't know what that means :(
            (Hint: Use one of the recognised commands)""";
    public static final String MESSAGE_INVALID_TASK_INDEX = """
            I couldn't find that task :(
            (Hint: Check that the task number is correct)""";
    public static final String MESSAGE_INVALID_DATE_FORMAT = """
            I don't understand this date format :(
            (Hint: Use the yyyy-mm-dd format)""";

    public static final String MESSAGE_STORAGE_FILE_CREATION_ERROR = """
            I couldn't create the storage file! :(""";
    public static final String MESSAGE_STORAGE_FILE_LOAD_ERROR = """
            I couldn't load from the storage file! :(""";
    public static final String MESSAGE_STORAGE_FILE_SAVE_ERROR = """
            I couldn't save to the storage file! :(""";
}
