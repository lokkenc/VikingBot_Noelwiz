package Actions;

import bwapi.Game;
import bwapi.Unit;
import model.Units;

import java.util.ArrayList;

public class Attack extends Action {
    private ActionType type = ActionType.ATTACK;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the units to attack the enemy with the lowest health in range
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that makes up the commandable squad
     */
    public void doAction(Game game, Unit unit){

        Unit lowestUnit = null; // set a variable to hold the lowerUnit id and its hp
        int lowestHP = Integer.MAX_VALUE;
        for (Unit enemyUnit : game.enemy().getUnits()) { // for every enemy unit
            if(unit.isInWeaponRange(enemyUnit) && enemyUnit.getHitPoints() < lowestHP) { // if it is in range and has less HP
                // set it as the new lowest enemyUnit
                lowestUnit = enemyUnit;
                lowestHP = enemyUnit.getHitPoints();
            }
        }

        if(lowestUnit != null) { // if we found something to attack, attack it
            unit.attack(lowestUnit);
        }
    }
}