package src.main.java.ML.Actions;

import bwapi.Game;
import bwapi.Unit;

import java.io.Serializable;
import java.util.Objects;

public class Attack extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.ATTACK;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the units to attack the enemy with the lowest health in range
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

        unit.attack(closestUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attack attack = (Attack) o;
        return type == attack.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}