import bwapi.*;
import bwta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class IntelligenceAgent {

    private int baseLoc = 0;
    private ArrayList<Integer> scouts = new ArrayList<Integer>();
    private HashMap<UnitType, Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashSet<Position> enemyBuildingMemory = new HashSet<Position>();
    private ArrayList<Chokepoint> watched = new ArrayList<Chokepoint>(3);

    /**
     * Clears current unitMemory and uses {@link #updateUnitMemory} to add all trained units to hashmap
     * @param self
     */
    public void tabulateUnits (Player self) {
        unitMemory.clear();
        for (Unit unit : self.getUnits()) {
            if (unit.isTraining()) {
                continue;
            }

            updateUnitMemory(unit.getType(), 1);
        }
    }

    /**
     * Used by {@link #tabulateUnits} to increment count of UnitType by the amount
     * @param type
     * @param amount
     */
    private void updateUnitMemory (UnitType type, int amount) {
        if (unitMemory.containsKey(type)) {
            unitMemory.put(type, unitMemory.get(type) + amount);
        }
        else {
            unitMemory.put(type, amount);
        }
    }

    /**
     * Returns true if unit with unitID is scout false otherwise
     * @param unitID
     * @return
     */
    public boolean isScout(int unitID) {
        if(scouts.contains(unitID)) {
            return true;
        } else{
            return false;
        }
    }

    /**
     * Updates hashset of enemy building locations
     * @param game
     */
    public void updateEnemyBuildingMemory (Game game) {
        // update the hashset of enemy building positions
        for (Unit enemyUnit: game.enemy().getUnits()) {
            if (enemyUnit.getType().isBuilding()) {
                if(!enemyBuildingMemory.contains(enemyUnit.getPosition())) {
                    enemyBuildingMemory.add(enemyUnit.getPosition());
                }
            }
        }

        //remove any destroyed buildings from the hashset
        for (Position pos: enemyBuildingMemory) {
            TilePosition tileCorrespondingToPos = new TilePosition(pos.getX()/32, pos.getY()/32);

            if(game.isVisible(tileCorrespondingToPos)) {
                boolean buildingStillThere = false;
                for (Unit enemyUnit: game.enemy().getUnits()) {
                    if(enemyUnit.getType().isBuilding() && enemyUnit.getOrderTargetPosition().equals(pos)) {
                        buildingStillThere = true;
                        break;
                    }
                }

                if (buildingStillThere == false) {
                    enemyBuildingMemory.remove(pos);
                    break;
                }
            }
        }
    }

    /**
     * Sends a scout to attack possible enemy base positions
     * @param scout
     * @param basePos
     */
    public void findEnemyBase (Unit scout, BaseLocation basePos) {
        scout.attack(basePos.getPosition());
    }

    /**
     * Finds and adds chokePoints to an arrayList
     * @param p
     */
    public void addWatchedPoint(Position p){
        List<Chokepoint> unwatched = new ArrayList<Chokepoint>();
        unwatched.addAll(BWTA.getChokepoints());
        if(watched.size() > 0){
            unwatched.removeAll(watched);
        }

        //find the closest unoccupied chokepoint
        int closestIndex = 0;
        Position destination = unwatched.get(0).getCenter();
        Position currentPosition;
        int distance = p.getApproxDistance(destination);
        for(int i = 1; i < unwatched.size(); i++){
            currentPosition = unwatched.get(i).getCenter();
            if(distance > p.getApproxDistance(currentPosition)){
                distance = p.getApproxDistance(currentPosition);
                destination = currentPosition;
                closestIndex = i;
            }
        }

        watched.add(unwatched.get(closestIndex));
    }

    /**
     * Returns the total count of units of type
     * @param self
     * @param type
     * @return
     */
    public int getUnitsOfType (Player self, UnitType type) {
        int numOfUnits = 0;

        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                numOfUnits++;
            }
        }

        return numOfUnits;
    }

    /**
     * Returns a list of units of type
     * @param self
     * @param type
     * @return
     */
    private List<Unit> getUnitsListOfType(Player self, UnitType type){
        List<Unit> unitsList = new ArrayList<Unit>(4);

        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                unitsList.add(unit);
            }
        }

        return unitsList;
    }

    /**
     * True if unit of type is within radius of position
     * @param game
     * @param position
     * @param radius
     * @param type
     * @return
     */
    public boolean isUnitInRadius (Game game, Position position, int radius, UnitType type) {
        List<Unit> units;
        units = game.getUnitsInRadius(position, radius);

        for (Unit unit : units) {
            if (unit.getType() == type) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a unit of type target that has no units of type type in the specified radius
     * @param game
     * @param self
     * @param target
     * @param type
     * @param radius
     * @return
     */
    public Unit getUnitWithoutType (Game game, Player self, UnitType target, UnitType type, int radius) {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == target) {
                if (!isUnitInRadius(game, unit.getPosition(), radius, type)) {
                    return unit;
                }
            }
        }

        return null;
    }

    /**
     * Returns an available unit of type
     * @param self
     * @param type
     * @return
     */
    public Unit getAvailableUnit(Player self, UnitType type) {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type && !isScout(unit.getID())) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Returns a worker that is not a scout
     * @param self
     * @return
     */
    public Unit getAvailableWorker(Player self) {
        // Find an available worker
        for (Unit unit : self.getUnits()) {
            if (unit.getType().isWorker() && !scouts.contains(unit.getID())) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Returns a pylon
     * @param self
     * @return
     */
    public Unit getPylon(Player self) {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == UnitType.Protoss_Pylon) {
                // Other checks in here?
                return unit;
            }
        }

        return null;
    }

    /**
     * Returns the total amount of buildings of type
     * @param self
     * @param type
     * @return
     */
    public int getBuildingUnitsOfType(Player self, UnitType type) {
        int numberOfBuildingUnits = 0;

        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                if (unit.isBeingConstructed()) {
                    numberOfBuildingUnits++;
                }
            }
        }

        return numberOfBuildingUnits;
    }
}
