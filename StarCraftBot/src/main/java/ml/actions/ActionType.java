package ML.Actions;

public enum ActionType {
    ATTACK("Attack"),
    RETREAT("Retreat"),
    MOVETOWARDS("MoveTowards"),
    GOHOME("GoHome");

    private final String name;

    ActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
