package listener;

public enum ListenerType {
    NORMAL("Normal"),
    TRAINING("Training");

    private final String name;

    ListenerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
