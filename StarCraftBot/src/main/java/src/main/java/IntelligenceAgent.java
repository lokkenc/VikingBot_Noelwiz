package src.main.java;

import Knowledge.ProtossGeneralKnowledge;
import Knowledge.TerrenGeneralKnowledge;
import Knowledge.ZergGeneralKnowledge;
import bwapi.*;
import bwta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class IntelligenceAgent {
    private Race myrace;
    private Race enemyRace;
    Player self;

    private int baseLoc = 0;
    private ArrayList<Integer> scouts = new ArrayList<Integer>();
    private HashMap<UnitType, Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashSet<Position> enemyBuildingMemory = new HashSet<Position>();
    private ArrayList<Chokepoint> watched = new ArrayList<Chokepoint>(3);

    public IntelligenceAgent(Player self) {
        this.self = self;
    }

    /**
     * Clears current unitMemory and uses {@link #updateUnitMemory} to add all trained units to hashmap
     * @param self Player assigned to the bot
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
     * @param type Unit to check memory for
     * @param amount Total amount of units
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
     * Checks if player has units of type type
     * @param type UnitType to check for
     * @return True if unit of type type exists
     */
    public boolean unitExists(UnitType type) {
        if(unitMemory.containsKey(type)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Used to get the value mapped by the key of type
     * @param type Unit type to search the list for
     * @return value mapped by the key of type
     */
    public int getUnitMemoryValue(UnitType type) {
        return unitMemory.get(type);
    }

    /**
     * Returns true if unit with unitID is scout false otherwise
     * @param unitID Unit that should be checked
     * @return True if unit is scout
     */
    public boolean isScout(int unitID) {
        if(scouts.contains(unitID)) {
            return true;
        } else{
            return false;
        }
    }

    /**
     * This function adds a unit to the scout list
     * @param unitID ID number of the unit to add
     */
    public void addScout(int unitID) {
        scouts.add(unitID);
    }

    /**
     * Changes BaseLoc variable to provided value
     * @param newBaseLoc new value of BaseLoc
     */
    public void changeBaseLoc(int newBaseLoc) {
        baseLoc = newBaseLoc;
    }

    /**
     *
     * @return the current value of baseLoc
     */
    public int getBaseLoc() {
        return baseLoc;
    }

    /**
     * Updates hashset of enemy building locations
     * @param game Game value assigned at game start
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

                if (!buildingStillThere) {
                    enemyBuildingMemory.remove(pos);
                    break;
                }
            }
        }
    }

    /**
     * Sends a scout to attack possible enemy base positions
     * @param scout Unit that is a scout
     * @param basePos Possible location of a base to scout
     */
    public void findEnemyBase (Unit scout, BaseLocation basePos) {
        scout.attack(basePos.getPosition());
    }

    /**
     * Returns the current enemyBuildingMemory
     * @return returns the current enemyBuildingMemory
     */
    public HashSet<Position> getEnemyBuildingMemory() {
        return enemyBuildingMemory;
    }

    /**
     * Finds and adds chokePoints to an arrayList
     * @param p Position to watch
     */
    public void addWatchedPoint(Position p) {
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
     * @param self Player assigned to the bot
     * @param type UnitType to get the count of
     * @return returns the count of units of type
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
     * @param self Player assigned to the bot
     * @param type UnitType to get the list of
     * @return Returns a list of all units of type type
     */
    public List<Unit> getUnitsListOfType(Player self, UnitType type){
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
     * @param game Game value assigned at game start
     * @param position Position of the center point
     * @param radius Value of the Radius
     * @param type UnitType to search the radius for
     * @return Returns true if unit of type is within the radius of position
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
     * @param game Game value assigned at game start
     * @param self Player assigned to the bot
     * @param target UnitType to use as the anchor for the radius
     * @param type UnitType to check radius for
     * @param radius Size of radius from the target unit
     * @return returns a unit of type target with no units of type type in the radius
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
     * @param self Player assigned to the bot
     * @param type UnitType to search for availability
     * @return Available unit of type type
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
     * @param self Player assigned to the bot
     * @return Available worker unit
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
     * Returns a Unit
     * @param self Player assigned to the bot
     * @param type UnitType to search for
     * @return Unit of type type
     */
    public Unit getUnit(Player self, UnitType type) {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                // Other checks in here?
                return unit;
            }
        }

        return null;
    }

    /**
     * Returns the total amount of buildings of type
     * @param self Player assigned to the bot
     * @param type UnitType to get the count of
     * @return Total count of building unit of type type
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

    // * * * Information retrieval for AI Planning * * *//
    public int getNumWorkers(){
        int numworkers = 0;

        switch (myrace) {
            case Terran:
                numworkers = unitMemory.get(UnitType.Terran_SCV);
                break;
            case Zerg:
                numworkers = unitMemory.get(UnitType.Zerg_Drone);
                break;
            case Protoss:
                numworkers = unitMemory.get(UnitType.Protoss_Probe);
                break;
        }

        return numworkers;
    }

    private int getNumWorkersGatheringMinerals() {
        int numWorkers = 0;
        for (Unit unit : self.getUnits()) {
            if (unit.isGatheringMinerals()) {
                numWorkers++;
            }
        }
        return numWorkers;
    }

    private int getNumWorkersGatheringGas() {
        int numWorkers = 0;
        for (Unit unit : self.getUnits()) {
            if (unit.isGatheringGas()) {
                numWorkers++;
            }
        }
        return numWorkers;
    }

    public float getMineralProductionRate() {
        float productionRate = 0f;
        int numBases;
        int[] numWorkers;
        switch(myrace) {
            case Terran:
                numBases = unitMemory.get(UnitType.Terran_Command_Center);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringMinerals() / numBases;
                }
                TerrenGeneralKnowledge tgk= new TerrenGeneralKnowledge();
                productionRate= tgk.AverageMineralProductionRate(numWorkers);
                break;
            case Zerg:
                numBases = unitMemory.get(UnitType.Zerg_Hatchery);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringMinerals() / numBases;
                }
                ZergGeneralKnowledge zgk= new ZergGeneralKnowledge();
                productionRate= zgk.AverageMineralProductionRate(numWorkers);
                break;
            case Protoss:
                numBases = unitMemory.get(UnitType.Protoss_Nexus);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringMinerals() / numBases;
                }
                ProtossGeneralKnowledge pgk= new ProtossGeneralKnowledge();
                productionRate= pgk.AverageMineralProductionRate(numWorkers);
                break;
        }
        return productionRate;
    }

    public float getGasProductionRate() {
        float productionRate = 0f;
        int numBases;
        int[] numWorkers;
        switch(myrace) {
            case Terran:
                numBases = unitMemory.get(UnitType.Terran_Command_Center);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringGas() / numBases;
                }
                TerrenGeneralKnowledge tgk= new TerrenGeneralKnowledge();
                productionRate= tgk.AverageMineralProductionRate(numWorkers);
                break;
            case Zerg:
                numBases = unitMemory.get(UnitType.Zerg_Hatchery);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringGas() / numBases;
                }
                ZergGeneralKnowledge zgk= new ZergGeneralKnowledge();
                productionRate= zgk.AverageMineralProductionRate(numWorkers);
                break;
            case Protoss:
                numBases = unitMemory.get(UnitType.Protoss_Nexus);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringGas() / numBases;
                }
                ProtossGeneralKnowledge pgk= new ProtossGeneralKnowledge();
                productionRate= pgk.AverageMineralProductionRate(numWorkers);
                break;
        }
        return productionRate;
    }

    public int getNumBases() {
        int numBases = 0;

        switch (myrace) {
            case Terran:
                numBases = unitMemory.get(UnitType.Terran_Command_Center);
                break;
            case Zerg:
                numBases = unitMemory.get(UnitType.Zerg_Hatchery);
                break;
            case Protoss:
                numBases = unitMemory.get(UnitType.Protoss_Nexus);
                break;
        }

        return numBases;
    }

    public int getTimeSinceLastScout() {
        return 0;
    }
}
