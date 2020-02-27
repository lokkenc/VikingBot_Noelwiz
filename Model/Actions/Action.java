package Actions;

import bwapi.Game;
import model.Units;

public abstract class Action {
    private ActionType type;

    public abstract ActionType getType();

    public abstract void doAction(Game game, Units units);
}
