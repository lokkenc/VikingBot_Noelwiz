package src.main.java.ML.Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

import java.io.Serializable;
import java.util.Objects;

public class MoveLeft extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.MOVELEFT;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to move straight left
     *
     * @param game The game initialized at the start of the program
     * @param unit The unit that needs a command
     */
    public void doAction(Game game, Unit unit) {

        Position movePos;
        movePos = new Position(unit.getX() - 8, unit.getY());
        if (unit.hasPath(movePos)) { // check if it can move there
            unit.move(movePos);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveLeft moveLeft = (MoveLeft) o;
        return type == moveLeft.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}