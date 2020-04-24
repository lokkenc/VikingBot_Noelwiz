package ml.actions;

import bwapi.Game;
import bwapi.Unit;

import java.io.Serializable;
import java.util.Objects;

/**
 * Action that is used for Attacking. Attacks currently are based on Lowest Unit in attack range.
 */
public class Attack extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.ATTACK;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the units to attack the enemy with the lowest health in range
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that should attack
     */
    public void doAction(Game game, Unit unit){

        Unit attackUnit = null; // set a variable to hold the lowerUnit id and its hp
        int lowestHP = Integer.MAX_VALUE;
        for (Unit enemyUnit : game.enemy().getUnits()) { // for every enemy unit
            if(unit.isInWeaponRange(enemyUnit) && enemyUnit.getHitPoints() < lowestHP) {
                attackUnit = enemyUnit;
                lowestHP = unit.getHitPoints();
            }
        }

        if(attackUnit != null) { // if there is a unit in range
            unit.attack(attackUnit);
        }
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


    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                '}';
    }
}