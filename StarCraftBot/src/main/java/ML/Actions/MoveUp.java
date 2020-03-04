package ML.Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class MoveUp extends Action {
    private ActionType type = ActionType.MOVEUP;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to move straight up
     *
     * @param game  The game initialized at the start of the program
     * @param unit The unit that needs a command
     */
    public void doAction(Game game, Unit unit) {

        Position movePos;
        movePos = new Position(unit.getX(), unit.getY() - 8);
        if (unit.hasPath(movePos)) { // check if it can move there
            unit.move(movePos);
        }
    }
}