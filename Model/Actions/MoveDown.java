package Actions;

import Actions.ActionType;

public class MoveDown extends Action {
    private ActionType type = MOVEDOWN;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to move straight down
     * @param game The game initialized at the start of the program
     * @param units The group of units that makes up the commandable squad
     */
    public void doAction(Game game, Units units){

        Position movePos;
        for(Unit unit: units) { // for every unit subtract 8 from the units current y to move down
            movePos = new Position(unit.getX(), unit.getY() - 8)
            if(unit.hasPath(movePos)) { // check if it can move there
                unit.move(movePos);
            }
        }
    }