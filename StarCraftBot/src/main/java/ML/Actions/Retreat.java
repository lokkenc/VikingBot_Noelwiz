package ML.Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

import java.io.Serializable;
import java.util.Objects;

/**
 * Action that is used for moving away from enemy units. It will move a unit to a position opposite of the closest enemy unit.
 */
public class Retreat extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.RETREAT;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the unit to retreat
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that needs to retreat
     */
    public void doAction(Game game, Unit unit){
        Unit closestUnit = null; // set a variable to hold the lowerUnit id and its hp
        int lowestDistance = Integer.MAX_VALUE;
        for (Unit enemyUnit : game.enemy().getUnits()) { // for every enemy unit
            if(unit.getDistance(enemyUnit) < lowestDistance) {
                closestUnit = enemyUnit;
                lowestDistance = unit.getDistance(enemyUnit);
            }
        }

        if(closestUnit != null) {
            int xTrvl = 0;
            int yTrvl = 0;
            if(closestUnit.getX() < unit.getX()) {
                xTrvl = unit.getX() + 10;
            } else {
                xTrvl = unit.getX() - 10;
            }

            if(closestUnit.getY() < unit.getY()) {
                yTrvl = unit.getY() + 10;
            } else {
                yTrvl = unit.getY() - 10;
            }

            unit.move(new Position(xTrvl, yTrvl));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Retreat retreat = (Retreat) o;
        return type == retreat.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                '}';
    }
}
