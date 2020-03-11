package src.main.java.ML.Actions;

import bwapi.Game;
import bwapi.Unit;

import java.io.Serializable;

public abstract class Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type;

    public abstract ActionType getType();

    public abstract void doAction(Game game, Unit unit);
}
