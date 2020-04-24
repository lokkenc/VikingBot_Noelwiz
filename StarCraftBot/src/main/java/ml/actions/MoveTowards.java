package ml.actions;

import agents.IntelligenceAgent;
import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/**
 * Action used to MoveTowards the closest enemy unit
 */
public class MoveTowards extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.MOVETOWARDS;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the unit to move towards the closest enemy
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that needs to approach the enemy
     */
    public void doAction(Game game, Unit unit) {

        Unit closestUnit = null; // set a variable to hold the lowerUnit id and its hp
        int lowestDistance = Integer.MAX_VALUE;
        for (Unit enemyUnit : game.enemy().getUnits()) { // for every enemy unit
            if(unit.getDistance(enemyUnit) < lowestDistance) {
                closestUnit = enemyUnit;
                lowestDistance = unit.getDistance(enemyUnit);
            }
        }

        if(closestUnit != null) { // if there is a unit in vision
            unit.move(new Position(closestUnit.getX(), closestUnit.getY()));
        } else { // if there is not a unit in vision use the knowledge we have of their bases
           IntelligenceAgent intel =  IntelligenceAgent.getInstance(game);
           HashSet<Position> enemyBuildings = intel.getEnemyBuildingMemory();
           Iterator<Position> enemyBuildingsItr = enemyBuildings.iterator();
           if(enemyBuildingsItr.hasNext()) { // if there is no knowledge of a base then nothing will happen still
               unit.move(enemyBuildingsItr.next());
           }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveTowards MoveTo = (MoveTowards) o;
        return type == MoveTo.type;
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
