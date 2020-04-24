package Agents;

import Knowledge.ProtossGeneralKnowledge;
import Knowledge.TerrenGeneralKnowledge;
import Knowledge.ZergGeneralKnowledge;
import Planning.CombatUnitStatus;
import Planning.GameStatus;
import bwapi.*;
import bwta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static Planning.GameStatus.*;

public class IntelligenceAgent {
    private static IntelligenceAgent onlyInstance = null;

    private Race myrace;
    private Race enemyRace;
    private Player self;
    private Game game;

    private int baseLoc = 0;
    private ArrayList<Integer> scouts = new ArrayList<Integer>();
    private HashMap<UnitType, Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashMap<UnitType, Integer> enemyUnitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashSet<Position> enemyBuildingMemory = new HashSet<Position>();
    private ArrayList<Chokepoint> watched = new ArrayList<Chokepoint>(3);
    private int scoutTimer;

    private IntelligenceAgent(Player self, Game game) {
        this.self = self;
        this.game = game;
        //myrace = self.getRace();
        //enemyRace = game.enemy().getRace();
        myrace = Race.Protoss;
        enemyRace = Race.Terran;

    }

    public static IntelligenceAgent getInstance(Game game) {
        if(onlyInstance == null) {
            onlyInstance = new IntelligenceAgent(game.self(), game);
        }

        return onlyInstance;
    }

    /**
     * @return the player
     */
    public Player getSelf() {
        return self;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Clears current unitMemory and uses {@link #updateUnitMemory} to add all trained units to hashmap
     * @param self Player assigned to the bot
     */
    public void tabulateUnits (Player self) {
        unitMemory.clear();
        for (Unit unit : self.getUnits()) {
            if (!unit.isCompleted()) {
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


    public void onUnitDestroy(Unit unit) {
        if(unit.isVisible(self) && !unit.getPlayer().equals(self)){
            updateEnemyUnitMemory(unit.getType(), -1);
        }
    }

    public void onUnitShow(Unit unit){
        if(unit.isVisible(self) && !unit.getPlayer().equals(self)){
            updateEnemyUnitMemory(unit.getType(), 1);
        }
    }


    /**
     * Change count of UnitType by the amount
     * @param type Unit to check memory for
     * @param amount Total amount of units
     */
    private void updateEnemyUnitMemory (UnitType type, int amount) {
        if (enemyUnitMemory.containsKey(type)) {
            enemyUnitMemory.put(type, enemyUnitMemory.get(type) + amount);
        }
        else {
            enemyUnitMemory.put(type, amount);
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
     * @param type UnitType to get the list of
     * @return Returns a list of all units of type type
     */
    public List<Unit> getUnitsListOfType(UnitType type){
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

    /**
     * Get the number of workers for the AI planner
     * @return int, # workers the player owns.
     */
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

    /**
     * Gets number of workers gathering minerals
     * @return Number of workers gathering minerals
     */
    private int getNumWorkersGatheringMinerals() {
        int numWorkers = 0;
        for (Unit unit : self.getUnits()) {
            if (unit.isGatheringMinerals()) {
                numWorkers++;
            }
        }
        return numWorkers;
    }

    /**
     * Gets number of workers gathering gas
     * @return Number of workers gathering gas
     */
    private int getNumWorkersGatheringGas() {
        int numWorkers = 0;
        for (Unit unit : self.getUnits()) {
            if (unit.isGatheringGas()) {
                numWorkers++;
            }
        }
        return numWorkers;
    }

    /**
     * Gets the mineral production rate
     * @return float, the current mineral production rate
     */
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

    /**
     * Gets the gas production rate
     * @return float, the current gas production rate
     */
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

    /**
     * Gets the number of friendly bases
     * @return number of bases controlled by the player
     */
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

    /**
     * Marks the current frame count as the last scout time
     */
    public void setScoutTimer() {
        scoutTimer = game.getFrameCount();
    }

    /**
     * Gets the number of frames since last scout was sent
     * @return the number of frames since a scout was last sent
     */
    public int getTimeSinceLastScout() {
        return game.getFrameCount() - scoutTimer;
    }

    /**
     * Gets a list of friendly combat unit statuses
     * @return An ArrayList containing the {@link CombatUnitStatus} of each combat unit
     */
    public ArrayList<CombatUnitStatus> getCombatUnitStatuses() {
        ArrayList<CombatUnitStatus> list = new ArrayList<CombatUnitStatus>();
        for (UnitType unit: unitMemory.keySet()) {
            if (unit.canAttack() && !(unit.isWorker())) {
                CombatUnitStatus status = new CombatUnitStatus(unit, unitMemory.get(unit));
                list.add(status);
            }
        }
        return list;
    }


    /**
     * Get the number of enemy workers
     * @return the number of known workers controlled by the enemy
     */
    public int getNumEnemyWorkers() {
        int numworkers = 0;

        switch (enemyRace) {
            case Terran:
                if (enemyUnitMemory.containsKey(UnitType.Terran_SCV))
                    numworkers = enemyUnitMemory.get(UnitType.Terran_SCV);
                break;
            case Zerg:
                if (enemyUnitMemory.containsKey(UnitType.Zerg_Drone))
                    numworkers = enemyUnitMemory.get(UnitType.Zerg_Drone);
                break;
            case Protoss:
                if (enemyUnitMemory.containsKey(UnitType.Protoss_Probe))
                    numworkers = enemyUnitMemory.get(UnitType.Protoss_Probe);
                break;
        }

        return numworkers;
    }

    /**
     * Get number of enemy bases
     * @return the number of bases controlled by the enemy player
     */
    public int getNumEnemyBases() {
        int numBases = 1;

        switch (enemyRace) {
            case Terran:
                if (enemyUnitMemory.containsKey(UnitType.Terran_Command_Center))
                    numBases = unitMemory.get(UnitType.Terran_Command_Center);
                break;
            case Zerg:
                if (enemyUnitMemory.containsKey(UnitType.Zerg_Hatchery))
                    numBases = unitMemory.get(UnitType.Zerg_Hatchery);
                break;
            case Protoss:
                if (enemyUnitMemory.containsKey(UnitType.Protoss_Nexus))
                    numBases = unitMemory.get(UnitType.Protoss_Nexus);
                break;
        }

        return numBases;
    }

    /**
     * Get the most common combat unit of the players army
     * @return The UnitType of the most common combat unit controlled by the player
     */
    public UnitType getMostCommonCombatUnit() {
        UnitType mostCommonType = null;
        int amount = 0;
        for (UnitType unit: unitMemory.keySet()) {
            if (unit.canAttack() && !(unit.isWorker())) {
                if (unitMemory.get(unit) > amount) {
                    mostCommonType = unit;
                    amount = unitMemory.get(unit);
                }
            }
        }
        return mostCommonType;
    }

    /**
     * Checks if the players is attacking the enemy base
     * @return true if the players is attacking the enemy base, false otherwise
     */
    public Boolean attackingEnemyBase() {
        ArrayList<Unit> enemyBases = new ArrayList<>();
        UnitType baseType;
        switch (enemyRace) {
            case Terran:
                baseType = UnitType.Terran_Command_Center;
                break;
            case Zerg:
                baseType = UnitType.Zerg_Hatchery;
                break;
            default:
                baseType = UnitType.Protoss_Nexus;
                break;
        }
        //Getting list of enemy bases
        for (Unit unit: game.enemy().getUnits()) {
            if (unit.getType() == baseType) {
                enemyBases.add(unit);
            }
        }
        //For each enemy base, check if we have units that are attacking it
        for (Unit enemyBase: enemyBases) {
            for (Unit unit: self.getUnits()) {
                if (unit.isAttacking() && unit.getRegion() == enemyBase.getRegion()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the players bases is being attacked
     * @return true if the player's base is being attacked, false otherwise
     */
    public Boolean beingAttacked() {
        ArrayList<Unit> bases = new ArrayList<>();
        UnitType baseType;
        switch (enemyRace) {
            case Terran:
                baseType = UnitType.Terran_Command_Center;
                break;
            case Zerg:
                baseType = UnitType.Zerg_Hatchery;
                break;
            default:
                baseType = UnitType.Protoss_Nexus;
                break;
        }
        //Get list of our bases
        for (Unit unit: self.getUnits()) {
            if (unit.getType() == baseType) {
                bases.add(unit);
            }
        }
        //For each base, check if we are being attacked there
        for (Unit base: bases) {
            for (Unit unit: game.enemy().getUnits()) {
                if (unit.isAttacking() && unit.getRegion() == base.getRegion()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the player's race
     */
    public Race getPlayerRace() {
        return myrace;
    }

    /**
     * @return the enemy's race
     */
    public Race getEnemyRace() {
        return enemyRace;
    }

    /**
     * Get the current {@link GameStatus}
     * @return return a {@link GameStatus} describing the current game
     */
    public GameStatus getGameStatus() {
        GameStatus gameStatus;
        int earlyGameThreshold = 12600;
        int midGameThreshold = 25200;
        if (game.getFrameCount() < earlyGameThreshold) {
            gameStatus = EARLY;
        } else if (game.getFrameCount() < midGameThreshold) {
            gameStatus = MID;
        } else {
            gameStatus = LATE;
        }
        return gameStatus;
    }

    /**
     * @return A two-dimensional array of the form [workers[used, avail] , ground[used, avail], combat air[used, avail],
     *                                             support air[used, avail]]
     */
    public int[][] getTrainingCapacity() {
        int trainingCapacity[][] = {{0,3}, {0,3}, {0,3}, {0,3}};
        return trainingCapacity;
    }

    /* * *Supporting Methods for getTrainingCapacity() * * */


    /* private int[] getWorkerCapacity() {

    }

    private int[] getGroundUnitCapacity() {

    }

    private int[] getCombatAirCapacity() {

    }

    private int[] getSupportAirCapacity() {

    } */
}
