package Actions;

import Actions.ActionType;

public abstract class Action {
    private ActionType type;

    public ActionType getType() {
        return this.type;
    }
}
