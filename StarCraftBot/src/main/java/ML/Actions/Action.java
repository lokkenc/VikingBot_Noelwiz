package ML.Actions;

import bwapi.Game;
import bwapi.Unit;

public abstract class Action implements java.io.Serializable {
    private ActionType type;

    public abstract ActionType getType();

    public abstract void doAction(Game game, Unit unit);
}
