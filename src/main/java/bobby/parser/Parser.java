package bobby.parser;

import java.util.HashMap;

import bobby.exception.BobbyException;

public class Parser {
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
     * @return A HashMap with keys "command", "value" and parameter names
     */
    public static HashMap<String, String> parse(String line) throws BobbyException {
        HashMap<String, String> components = new HashMap<>();

        if (line == null || line.isBlank()) {
            throw new BobbyException("Did you say something?\n"
                    + "(Hint: Input cannot be blank)");
        }

        // Split by one or more whitespace characters followed by '/'
        String[] sections = line.split("\\s+/");

        String commandValueSection = sections[0];
        String[] commandValueTokens = commandValueSection.split("\\s+", 2);

        if (commandValueTokens.length == 0) {
            throw new BobbyException("I don't know what that means :(\n"
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
                throw new BobbyException("I couldn't find a parameter name :(\n"
                        + "(Hint: Don't leave a space after the '/')");
            }

            String parameterName = parts[0];
            String parameterValue = parts.length < 2 ? "" : parts[1];
            components.put(parameterName, parameterValue);
        }

        return components;
    }
}
