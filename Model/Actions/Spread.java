package Actions;

import Actions.ActionType;
import java.util.*;

public class Spread extends Action {
    private ActionType type = SPREAD;

    public ActionType getType() {
        return this.type;
    }

    /**
     * This function orders units to spread out
     * @param game The game initialized at the start of the program
     * @param units The group of units that makes up the commandable squad
     */
    public void doAction(Game game, Units units){

        Position center = findCenter(getPosEndPoints(units));
        for(Unit unit: units) {
            int x = unit.getX();
            int y = unit.getY();
            if(x > center.getX()) { // if we are to the right move a bit further to the right
                x += 8;
            } else if(x < center.getX()) { // same for the left
                x -= 8;
            }

            if(y > center.getY()) { // if we are above move a bit further up
                y += 8;
            } else if(y < center.getY()) { // same for down
                y -= 8;
            }

            // it is possible that a unit is located at the center point and thus will not move at all.
            // this is ok and is not handled here but if that behavior needs to be changed add and else
            // to both the x and y checks above and add the behavior you would like to happen
            unit.move(new Position(x, y));
        }

    }

    /**
     * This function returns the min and max X and Y values of a group of units
     * @param units The group of units to search for the min and max values
     * @return int[] which holds the min/max values of x and y for all units
     */
    private int[] getPosEndPoints(Units units) {
        //Find min and max position values to get a center point
        int[] retArr = new int[4];
        retArr[0] = retArr[2] = Integer.MAX_VALUE;
        retArr[1] = retArr[3] = Integer.MIN_VALUE;

        for(Unit unit: units) { // all the units in our sqaud
            // Check for x min/max
            if(unit.getX() < retArr[0]) {
                retArr[0] = unit.getX();
            } else if(unit.getX() > retArr[1]) {
                retArr[1] = unit.getX();
            }

            // Check for y min/max
            if(unit.getY() < retArr[2]) {
                retArr[2] = unit.getY();
            } else if(Unit.getY() > retArr[3]) {
                retArr[3] = unit.getY();
            }
        }
        return retArr;
    }

    /**
     * This function finds the position at the center of all of the units
     * @param minMax integer array that holds the min/max values of x and y for all units
     * @return Position which is the center point of all the units
     */
    private Position findCenter(int[] minMax) {
        int xCoord= minMax[1] - ((minMax[1] - minMax[0]) / 2);
        int yCoord = minMax[3] - ((minMax[3] - minMax[2]) / 2);
        return new Position(xCoord, yCoord);
    }
}