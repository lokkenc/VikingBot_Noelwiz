package src.main.java;

import bwapi.*;
import bwta.*;

import java.util.HashSet;

public class CombatAgent {

    IntelligenceAgent intel = new IntelligenceAgent();
    Player self;

    public CombatAgent( IntelligenceAgent itagent){
        intel = itagent;
    }

    /**
     * Gives all units of type type the command to attack position targetPos
     * @param self Player assigned to the bot
     * @param type UnitType to send to attack
     * @param targetPos Position to attack
     */
    public void attackPosition (Player self, UnitType type, Position targetPos) {
        if (intel.unitExists(type)) {
            for (Unit unit : self.getUnits()) {
                if (unit.getType() == type) {
                    unit.attack(targetPos, false);
                }
            }
        }
    }

    /**
     * Sends units of type type to attack known enemy bases
     * @param self Player assigned to the bot
     * @param type UnitType that should be sent to attack
     */
    public void attackEnemyBase (Player self, UnitType type) {
        HashSet<Position> enemyBuildingMemory = intel.getEnemyBuildingMemory();
        for (Unit myUnit: self.getUnits()) {
            if(myUnit.getType() == type) {
                if(enemyBuildingMemory.iterator().hasNext()) {
                    myUnit.attack(enemyBuildingMemory.iterator().next());
                } else {
                    myUnit.attack(BWTA.getStartLocations().get(BWTA.getStartLocations().size()).getPosition());
                }
            }
        }
    }

    public void attackClosestEnemy (Unit unit) {

    }
}
