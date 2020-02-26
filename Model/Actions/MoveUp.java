package Actions;

import Actions.ActionType;

public class MoveUp extends Action {
    private ActionType type = MOVEUP;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to move straight up
     * @param game The game initialized at the start of the program
     * @param units The group of units that makes up the commandable squad
     */
    public void doAction(Game game, Units units){

        Position movePos;
        for(Unit unit: units) { // for every unit add 8 to the units current y to move up
            movePos = new Position(unit.getX(), unit.getY() + 8)
            if(unit.hasPath(movePos)) { // check if it can move there
                unit.move(movePos);
            }
        }
    }