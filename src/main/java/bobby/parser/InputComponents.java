package bobby.parser;

import java.util.HashMap;

/**
 * Helper class to store the components of user input after parsing.
 * Contains the command type, value and parameters.
 */
class InputComponents {
    private String commandType;
    private String value;

    /** Map of parameter names (without '/' prefix) to their values. */
    private final HashMap<String, String> parameters;

    public InputComponents() {
        parameters = new HashMap<>();
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Checks if a parameter with the given name exists in the parameters map.
     *
     * @param parameterName Name of the parameter (without '/' prefix).
     * @return True if the parameter exists, false otherwise.
     */
    public boolean containsParameter(String parameterName) {
        return parameters.containsKey(parameterName);
    }

    /**
     * Gets the value of a parameter with the given name.
     *
     * @param parameterName Name of the parameter (without '/' prefix).
     * @return The value of the parameter, or null if it does not exist.
     */
    public String getParameter(String parameterName) {
        return parameters.get(parameterName);
    }

    /**
     * Adds a parameter with the given name and value to the parameters map.
     *
     * @param parameterName Name of the parameter (without '/' prefix).
     * @param parameterValue Value of the parameter.
     */
    public void addParameter(String parameterName, String parameterValue) {
        parameters.put(parameterName, parameterValue);
    }
}
