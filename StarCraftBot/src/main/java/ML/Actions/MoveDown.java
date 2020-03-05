package ML.Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class MoveDown extends Action implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.MOVEDOWN;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to move straight down
     * @param game  The game initialized at the start of the program
     * @param unit The unit that needs a command
     */
    public void doAction(Game game, Unit unit) {

        Position movePos;
        movePos = new Position(unit.getX(), unit.getY() + 8);
        if (unit.hasPath(movePos)) { // check if it can move there
            unit.move(movePos);
        }
    }
}