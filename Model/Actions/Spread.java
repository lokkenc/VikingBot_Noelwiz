package Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;
import model.Units;

import java.util.ArrayList;

public class Spread extends Action {
    private ActionType type = ActionType.SPREAD;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to spread out
     * @param game The game initialized at the start of the program
     * @param units The group of units that makes up the commandable squad
     */
    public void doAction(Game game, Units units){

        ArrayList<Unit> allUnits = units.getUnits();
        Unit center = units.getCentralUnit();

        for(Unit unit: allUnits) {
            int x = unit.getX();
            int y = unit.getY();
            if(x > center.getX()) { // if we are to the right move a bit further to the right
                x += 8;
            } else if(x < center.getX()) { // same for the left
                x -= 8;
            }

            if(y > center.getY()) { // if we are below move a bit further down
                y += 8;
            } else if(y < center.getY()) { // same for up
                y -= 8;
            }

            // it is possible that a unit is located at the center point and thus will not move at all.
            // this is ok and is not handled here but if that behavior needs to be changed add and else
            // to both the x and y checks above and add the behavior you would like to happen
            unit.move(new Position(x, y));
        }
    }
}