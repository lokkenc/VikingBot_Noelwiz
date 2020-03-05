package ML.Actions;

import bwapi.Game;
import bwapi.Unit;

public class Attack extends Action implements java.io.Serializable {
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
}