package listener;

/**
 * Defines various ListenerTypes to distinguish different versions of GameListener.
 */
public enum ListenerType {
    NORMAL("Normal"),
    TRAINING("Training");

    private final String name;

    /**
     * Initialize a new ListenerType given it's name.
     * @param name the name of the ListenerType as a String.
     */
    ListenerType(String name) {
        this.name = name;
    }

    /**
     * Return the ListenerType's name as a String.
     * @return The name of the ListenerType as a String.
     */
    public String getName() {
        return name;
    }
}
