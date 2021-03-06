package agents;

import knowledge.ProtossGeneralKnowledge;
import knowledge.TerrenGeneralKnowledge;
import knowledge.ZergGeneralKnowledge;
import planning.CombatUnitStatus;
import planning.GameStatus;
import bwapi.*;
import bwta.*;

import java.util.*;

import static planning.GameStatus.*;

public class IntelligenceAgent {
    private static IntelligenceAgent onlyInstance = null;

    private Race myRace;
    private Race enemyRace;
    private Player self;
    private Game game;

    private int baseLoc = 0;
    private ArrayList<Integer> scouts = new ArrayList<Integer>();
    private HashMap<UnitType, Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashMap<UnitType, Integer> enemyUnitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashSet<Position> enemyBuildingMemory = new HashSet<Position>();
    private ArrayList<Chokepoint> watched = new ArrayList<Chokepoint>(3);
    private int scoutTimer = 0;
    private boolean attacking = false;

    private IntelligenceAgent(Player self, Game game) {
        this.self = self;
        this.game = game;
        myRace = self.getRace();
        enemyRace = game.enemy().getRace();
    }

    /**
     * The intelligence agent is a singleton. This function gets the single
     * instance of the intel agent.
     * @param game the current game being played.
     * @return the only instance of the intel agent.
     */
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
     * @return the current game
     */
    public Game getGame() {
        return game;
    }

    /**
     *
     * @return the HashMap of UnitTypes and number of Units of the respective UnitType.
     */
    public HashMap<UnitType, Integer> getUnitMemory() {
        return unitMemory;
    }

    /**
     * Clears current unitMemory and uses {@link #updateUnitMemory} to add all units owned by the bot to hashmap
     * NOTE: includes units still being trained (probably)
     */
    public void tabulateUnits () {
        unitMemory.clear();
        for (Unit unit : self.getUnits()) {
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
     * This function removes a unit to the scout list
     * @param unitID ID number of the unit to add
     */
    public void removeScout(int unitID) {
        for(int i = scouts.size() - 1; i > -1; i--){
            if(scouts.get(i) == unitID){
                scouts.remove(i);
            }
        }
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
     * Updates hash set of enemy building locations.
     */
    public void updateEnemyBuildingMemory () {
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

            boolean buildingStillThere = false;
            int i = 0;
            List<Unit> enemyUnits = game.enemy().getUnits();
            while(game.isVisible(tileCorrespondingToPos) && i < enemyUnits.size()) {
                if(enemyUnits.get(i).getTargetPosition().equals(new Position(0,0)) && enemyUnits.get(i).getType().isBuilding()) {
                    buildingStillThere = true;
                    break;
                } else {
                    if (enemyUnits.get(i).getType().isBuilding() && enemyUnits.get(i).getOrderTargetPosition().equals(pos)) {
                        buildingStillThere = true;
                        break;
                    }
                }
                i++;
            }

            if (!buildingStillThere && i >= enemyUnits.size() && i != 0) {
                enemyBuildingMemory.remove(pos);
                break;
            }
        }
    }


    /**
     * remove a unit from memory upon destruction. Not currently used.
     * @param unit the unit destroyed
     */
    public void onUnitDestroy(Unit unit) {
        if(unit.isVisible(self) && !unit.getPlayer().equals(self)){
            updateEnemyUnitMemory(unit.getType(), -1);
        }
    }

    /**
     * to be called when a unit is distroyed by the listener. Not currently used.
     * @param unit
     */
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
     * @return The current enemyBuildingMemory
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
     * @param type UnitType to get the count of
     * @return The count of units of type
     */
    public int getUnitCountOfType(UnitType type) {
        int numOfUnits = 0;

        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                numOfUnits++;
            }
        }

        return numOfUnits;
    }

    /**
     * Returns a list of units of type belonging to the bot
     * @param type UnitType to get the list of
     * @return Returns a list of all units of type type
     */
    public List<Unit> getUnitsListOfType(UnitType type){
        return getUnitsListOfType(type, self);
    }


    /**
     * Returns a list of units of type belonging to the specified player
     * @param type UnitType to get the list of
     * @return Returns a list of all units of type type
     */
    public List<Unit> getUnitsListOfType(UnitType type, Player unitOwner){
        List<Unit> unitsList = new ArrayList<Unit>(4);

        for (Unit unit : unitOwner.getUnits()) {
            if (unit.getType() == type) {
                unitsList.add(unit);
            }
        }

        return unitsList;
    }


    public int getOrderedMineralUse(){
        int mineralsUsed = 0;
        List<Unit> nexi = getUnitsListOfType(UnitType.Protoss_Nexus);
        for(Unit nexus : nexi){
            if(nexus.getTrainingQueue().size() > 0){
                for(UnitType unit: nexus.getTrainingQueue()){
                    mineralsUsed+=unit.mineralPrice();
                }
            }
        }

        List<Unit> gateways = getUnitsListOfType(UnitType.Protoss_Gateway);
        for(Unit gateway : gateways){
            if(gateway.getTrainingQueue().size() > 0){
                for(UnitType unit: gateway.getTrainingQueue()){
                    mineralsUsed+=unit.mineralPrice();
                }
            }
        }


        List<Unit> probes = getUnitsListOfType(UnitType.Protoss_Probe);
        for(Unit probe : probes){
            if(probe.isConstructing()){
                mineralsUsed += probe.getBuildType().mineralPrice();
            }
        }

        return mineralsUsed;
    }

    /**
     * True if unit of type is within radius of position
     * @param position Position of the center point
     * @param radius Value of the Radius
     * @param type UnitType to search the radius for
     * @return Returns true if unit of type is within the radius of position
     */
    public boolean isUnitInRadius (Position position, int radius, UnitType type) {
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
     * Get the current combat units that exist to attack with.
     * @param unitOwner the Player who we want a list of combat units from
     * @return a List of fighting Units.
     */
    public List<Unit> getCombatUnits(Player unitOwner){
        UnitFilter filter = new UnitFilter() {
            @Override
            public boolean test(Unit unit) {
                return !(unit.isBeingConstructed() || unit.getType().isWorker() || unit.getType().isBuilding())
                        && unit.canAttack();
            }
        };

        List<Unit> military = new LinkedList<Unit>();

        for (Unit unit : unitOwner.getUnits()) {
            if(filter.test(unit)){
                military.add(unit);
            }
        }

        return military;
    }



    /**
     * Get the current buildings units that exist and are owned by specified player..
     * @param buildingOwner the Player who we want a list of combat units from
     * @return a List of buildings.
     */
    public List<Unit> getBuildingsOwnedBy(Player buildingOwner){
        UnitFilter filter = new UnitFilter() {
            @Override
            public boolean test(Unit unit) {
                return (unit.getType().isBuilding());
            }
        };

        List<Unit> buildings = new LinkedList<Unit>();

        for (Unit unit : buildingOwner.getUnits()) {
            if(filter.test(unit)){
                buildings.add(unit);
            }

        }

        return buildings;
    }

    /**
     * Returns a unit of type target that has no units of type type in the specified radius
     * @param unitOwner Player assigned to the bot
     * @param target UnitType to use as the anchor for the radius
     * @param type UnitType to check radius for
     * @param radius Size of radius from the target unit
     * @return A unit of type target with no units of type type in the radius
     */
    public Unit getUnitWithoutType (Player unitOwner, UnitType target, UnitType type, int radius) {
        for (Unit unit : unitOwner.getUnits()) {
            if (unit.getType() == target) {
                if (!isUnitInRadius(unit.getPosition(), radius, type)) {
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
     * Returns a worker that is not a scout, owned by the player, and idle.
     * May return a unit that is constructing if no other non-scouting units
     * are available.
     * @return Available worker unit
     */
    public Unit getAvailableWorker() {
        // Find an available worker
        Unit AvailbleWorker = null;

        List<Unit> workers = getUnitsListOfType(UnitType.Protoss_Probe);
        if(workers.isEmpty()){
            System.out.println("WARNING: no workers owned by bot.");
        } else {
            for (Unit unit : workers) {
                //prioratize non-building workers
                if(unit.isConstructing()){
                    AvailbleWorker = unit;
                } else {
                    if (!scouts.contains(unit.getID())) {
                        return unit;
                    }
                }
            }
        }
        return AvailbleWorker;
    }

    /**
     * Returns a worker that is not a scout
     * @param notGatheringMinerals true, will exclude a unit that is not gathering minerals, else will exclude
     *                        one not gathering gas
     * @return Available worker unit
     */
    public Unit getAvailableWorkerNotGathering(boolean notGatheringMinerals) {
        // Find an available worker
        Unit chosen = null;
        Unit backup = null;
        List<Unit> workers;
        switch (self.getRace()){
            case Protoss:
                workers = getUnitsListOfType(UnitType.Protoss_Probe);
            case Zerg:
                workers = getUnitsListOfType(UnitType.Zerg_Drone);
            case Terran:
                workers = getUnitsListOfType(UnitType.Terran_SCV);
            default:
                workers = new ArrayList<>();
        }


        Unit current;
        for(int i = 0; i < workers.size() && chosen==null; i++){
            current = workers.get(i);

            //incase all workers are gathering gas or working
            if(current.isConstructing()){
                backup = current;
            }

            if(notGatheringMinerals){
                if(!current.isConstructing() && !current.isGatheringMinerals()){
                    chosen = current;
                }
            } else {
                if(!current.isConstructing() && !current.isGatheringGas()){
                    chosen = current;
                }
            }
        }


        if(chosen == null){
            if( backup == null && workers.size()>0){
                chosen = workers.get(0);
            } else {
                chosen = backup;
            }
        }

        return chosen;
    }

    /**
     * Get a unit of a specific type owned by a specific player
     * @param unitOwner Player who owns the unit.
     * @param type UnitType to search for
     * @return Unit of UnitType type
     */
    public Unit getUnit(Player unitOwner, UnitType type) {
        for (Unit unit : unitOwner.getUnits()) {
            if (unit.getType() == type) {
                // Other checks in here?
                return unit;
            }
        }

        return null;
    }

    /**
     * Returns the total amount of buildings of type.
     * NOTE: will include buildings being constructed.
     * @param type UnitType to get the count of
     * @return Total count of building unit of type type
     */
    public int getBuildingUnitsOfType(UnitType type) {
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

    public Position nextBaseLocationToCheck() {
       Iterator<TilePosition> startLoc = game.getStartLocations().iterator();
       for(int i = 0; i < baseLoc; i++) {
           startLoc.next();
       }

       TilePosition startLocTP = startLoc.next();
       if(game.isVisible(startLocTP)) {
           baseLoc++;
           return startLoc.next().toPosition();
       } else {
           return startLocTP.toPosition();
       }
    }

    // * * * Information retrieval for AI Planning * * *//

    /**
     * Get the number of workers for the AI planner
     * @return int, # workers the player owns.
     */
    public int getNumWorkers(){
        int numworkers = 0;

        switch (myRace) {
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
        switch(myRace) {
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
                if(unitMemory!= null){
                    numBases = unitMemory.get(UnitType.Protoss_Nexus);
                    numWorkers = new int[numBases];
                    for (int i = 0; i < numBases; i++) {
                        numWorkers[i] = getNumWorkersGatheringMinerals() / numBases;
                    }
                    ProtossGeneralKnowledge pgk= new ProtossGeneralKnowledge();
                    productionRate= pgk.AverageMineralProductionRate(numWorkers);
                }
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
        switch(myRace) {
            case Terran:
                numBases = unitMemory.get(UnitType.Terran_Command_Center);
                numWorkers = new int[numBases];
                for (int i = 0; i < numBases; i++) {
                    numWorkers[i] = getNumWorkersGatheringGas() / numBases ;
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
                    //;
                    numWorkers[i] = getNumWorkersGatheringGas() / unitMemory.getOrDefault(UnitType.Protoss_Assimilator, 1);
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

        switch (myRace) {
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
            if (unit.canAttack() && !(unit.isWorker()) /*&& !unit.isBuilding() */) {
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
     * let the intel agent tell ask if we're attacking.
     * @param setTo boolean, weather or not we are taking an attack action.
     */
    protected void setAttacking(boolean setTo){
        this.attacking = setTo;
    }

    /**
     * Checks if the players is attacking the enemy base
     * @return true if the players is attacking the enemy base, false otherwise
     */
    public Boolean isAttackingEnemy() {
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
        return false || attacking;
    }



    /**
     * Checks if the players buildings are being attacked
     * @return true if the player's base is being attacked, false otherwise
     */
    public Boolean beingAttacked() {
        for(Unit building : getBuildingsOwnedBy(self)){
            if(building.isUnderAttack()){
                return true;
            }
        }
        return false;
    }

    /**
     * @return the player's race
     */
    public Race getPlayerRace() {
        return myRace;
    }

    /**
     * @return the enemy's race
     */
    public Race getEnemyRace() {
        if(enemyRace == Race.Unknown || enemyRace == Race.Random){
            //hopefully if we scout, this will update the enemy's race.
            enemyRace = game.enemy().getRace();
        }
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
        int workerCapacity = 0;
        int workerUsed = 0;
        for (Unit base : this.getUnitsListOfType(UnitType.Protoss_Nexus)){
            if(base.isCompleted())
            {
                if(base.getTrainingQueue().isEmpty()){
                    workerCapacity++;
                }else {
                    workerUsed++;
                }
            }
        }

        int combatCapacity = 0;
        int combatUsed = 0;
        for (Unit gateway : this.getUnitsListOfType(UnitType.Protoss_Gateway)){
            if(gateway.isCompleted()){
                if(gateway.getTrainingQueue().isEmpty()){
                    combatCapacity++;
                }else {
                    combatUsed++;
                }
            }
        }

        int trainingCapacity[][] = {{workerUsed,workerCapacity}, {combatUsed,combatCapacity}, {0,0}, {0,0}};


        return trainingCapacity;
    }

    /**
     *
     * @return the maximum number of units that can currently exist.
     */
    public int getPopulationCapacity() {

        return (((getBuildingUnitsOfType(UnitType.Protoss_Pylon) + getUnitCountOfType(UnitType.Protoss_Pylon)) * 8)
                + 9 * ((getBuildingUnitsOfType(UnitType.Protoss_Nexus) + getUnitCountOfType(UnitType.Protoss_Nexus))));
    }

    /**
     *
     * @return the total number of units that currently exist.
     */
    public int getPopulationUsed() {
        return self.supplyUsed();
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
