package src.main.java.ML.Actions;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

import java.io.Serializable;
import java.util.Objects;

public class Retreat extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.RETREAT;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the unit to retreat
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that makes up the commandable squad
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

        unit.move(new Position(-1 * closestUnit.getX(), -1 * closestUnit.getY()));
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
}
