package ML.Actions;

import bwapi.Game;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Player;
import bwapi.Position;

import java.io.Serializable;
import java.util.Objects;

/**
 * Action that is used for returning a unit to base. If there are no bases left to the player then this action will
 * do nothing. The AI planning should have this information and thus should never set the skirmish flag to false
 * if there are no bases left.
 */
public class GoHome extends Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType type = ActionType.GOHOME;

    public ActionType getType() { return this.type; }

    /**
     * This function orders the unit to return to base
     * @param game The game that was initialized at the startup of the program
     * @param unit The unit that should return to base
     */
    public void doAction(Game game, Unit unit){

        Race race = unit.getType().getRace(); // need the race to check for a base
        Player self = game.self(); // need the current player to get all of the units
        Position home = null; // position of the home base
        if(race == Race.Protoss) { // check for nexus specifically
            for(Unit friendlyUnits : self.getUnits()) {
                if(friendlyUnits.getType() == UnitType.Protoss_Nexus) {
                    home = friendlyUnits.getPosition();
                }
            }
        } else if(race == Race.Zerg) { // check for a hatchery specifically
            for(Unit friendlyUnits : self.getUnits()) {
                if(friendlyUnits.getType() == UnitType.Zerg_Hatchery) {
                    home = friendlyUnits.getPosition();
                }
            }
        } else if(race == Race.Terran) { // check for a command center specifically
            for(Unit friendlyUnits : self.getUnits()) {
                if(friendlyUnits.getType() == UnitType.Terran_Command_Center) {
                    home = friendlyUnits.getPosition();
                }
            }
        }

        if(home != null) { // if there is no bases left then this action will do nothing
            Position move = new Position(home.getX() - 40, home.getY());
            unit.move(move, true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoHome goHome = (GoHome) o;
        return type == goHome.type;
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
