package agents;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

import java.util.*;

public class EconomyAgent {

    IntelligenceAgent intel;

    public EconomyAgent( Game game){
        intel = IntelligenceAgent.getInstance(game);
    }

    /**
     * tell a worker to go gather gas at the closest gas local
     * @param worker a worker, presumed probe.
     */
    protected void gatherGas(Unit worker){
       List<Unit> gasLocals;
       switch (intel.getPlayerRace()){
           case Protoss:
               gasLocals = intel.getUnitsListOfType(UnitType.Protoss_Assimilator);
               break;
           case Terran:
                gasLocals = intel.getUnitsListOfType(UnitType.Terran_Refinery);
                break;
           case Zerg:
               gasLocals = intel.getUnitsListOfType(UnitType.Zerg_Extractor);
               break;
           default:
               gasLocals = new ArrayList<>();
           break;
       }

       if(gasLocals.size() > 0){
           Unit closest = gasLocals.get(0);
           int Shortestdistance = worker.getDistance(closest);

           for(Unit gas : gasLocals){
                if(worker.getDistance(gas) < Shortestdistance){
                    Shortestdistance = worker.getDistance(gas);
                    closest = gas;
                }
           }

           worker.gather(closest);
       } else {
           System.err.println("No Extractors to gather from.");
       }
    }


    /**
     * Sends a worker to get the closest mineral to the specified base
     * @param worker Unit to send to gather minerals
     * @param base Base to gather minerals near
     */
    protected void gatherMinerals (Unit worker, Unit base) {
        Game game = intel.getGame();
        Unit closestMineral = null;

        //find the closest mineral
        for (Unit neutralUnit : game.neutral().getUnits()) {
            if (neutralUnit.getType().isMineralField()) {
                if (closestMineral == null || base.getDistance(neutralUnit) < base.getDistance(closestMineral)) {
                    closestMineral = neutralUnit;
                }
            }
        }

        //if a mineral patch was found, send the worker to gather it
        if (closestMineral != null) {
            worker.gather(closestMineral, false);
        }
    }

    /**
     * Sends a worker to gather minerals that are closest to it
     * @param worker Unit to send to gather minerals
     */
    public void gatherMinerals (Unit worker) {
        assert(worker != null);
        Game game = intel.getGame();
        Unit closestMineral = null;

        //find the closest mineral
        for (Unit neutralUnit : game.neutral().getUnits()) {
            if (neutralUnit.getType().isMineralField()) {
                if(closestMineral == null){
                    closestMineral = neutralUnit;
                }

                if (worker.getDistance(neutralUnit) < worker.getDistance(closestMineral)) {
                    closestMineral = neutralUnit;
                }
            }
        }

        //if a mineral patch was found, send the worker to gather it
        if (closestMineral != null) {
            worker.gather(closestMineral, false);
        }
    }

    /**
     * If player is Zerg morph a larva into an overlord. If player is Protoss build a pylon
     */
    public void expandPopulationCapacity() {
        Player self = intel.getSelf();
        Game game = intel.getGame();
        if(self.getRace() == Race.Zerg) {
            Unit larva = intel.getAvailableUnit(self, UnitType.Zerg_Larva);
            if (larva != null) {
                larva.morph(UnitType.Zerg_Overlord);
            }
        } else {
            Unit probe = intel.getAvailableUnit(self, UnitType.Protoss_Probe);
            if(probe != null) {
                //get a nice place to build a supply depot
                TilePosition buildTile = game.getBuildLocation(UnitType.Protoss_Pylon, self.getStartLocation());
                //and, if found, send the worker to build it (and leave others alone - break;)
                if (buildTile != null) {
                    probe.build(UnitType.Protoss_Pylon, buildTile);
                }
            }
        }
    }

    /**
     * Builds a building of type in a suitable position
     * @param type Building to be created
     */
    public void createBuildingOfType(UnitType type) {
        Game game = intel.getGame();
        Player self = intel.getSelf();
        Unit worker = intel.getAvailableWorker();

        assert type.isBuilding() : "Must Build Buildings.";

        if((worker != null)) {
            TilePosition buildTile;
            if(type != UnitType.Protoss_Assimilator){
                buildTile = game.getBuildLocation(type, self.getStartLocation());
                if(buildTile != null) {
                    worker.build(type, buildTile);
                }
            } else {
                List<Unit> locals = game.getGeysers();

                int mindist = 10000000;
                int currentdist = 0;
                Unit final_location = worker;
                for(Unit loc : locals){
                    currentdist = worker.getDistance(loc);
                    if(currentdist < mindist){
                        final_location = loc;
                        mindist = currentdist;
                    }
                }

                buildTile = game.getBuildLocation(type, final_location.getTilePosition());
                if(buildTile != null) {
                    worker.build(type, buildTile);
                }
            }
        }
    }

    /**
     * Builds a building of type in a suitable position determined by the anchor and maxDistance
     * Calls the variation of this that's anchored off of a TilePosition to minimize code repetition.
     * @param type Building to be created
     * @param anchor Unit that serves as the anchor for the build position
     * @param maxDistance maximum distance that building can be built from the anchor
     */
    public void createBuildingOfTypeWithAnchor(UnitType type, Unit anchor, int maxDistance) {
        createBuildingOfTypeWithAnchor(type, anchor.getTilePosition(), maxDistance);
    }

    /**
     * Builds a building of type in a suitable position determined by the anchor and maxDistance
     * Calls the variation of this that's anchored off of a TilePosition to minimize code repetition.
     * @param type Building to be created
     * @param anchor Unit that serves as the anchor for the build position
     * @param maxDistance maximum distance that building can be built from the anchor
     */
    public void createBuildingOfTypeWithAnchor(UnitType type, TilePosition anchor, int maxDistance) {
        Game game = intel.getGame();
        Unit worker = intel.getAvailableWorker();

        assert type.isBuilding() : "Must Build Buildings.";

        if ((worker != null)) {
            TilePosition buildTile = game.getBuildLocation(type, anchor, maxDistance);

            if (buildTile != null && game.canBuildHere(buildTile, type,worker,true)) {
                worker.move(buildTile.toPosition(),true);
                worker.build(type, buildTile);
            } else {
                System.out.println("cannot find suitable build location.");
            }
        }
    }

    //Only works for protoss for now
    public void trainWorker() {
        for (Unit nexus: intel.getUnitsListOfType(UnitType.Protoss_Nexus)) {
            List<UnitType> trainingQueue = nexus.getTrainingQueue();
            if (trainingQueue.size() < 5) {
                nexus.train(UnitType.Protoss_Probe);
            }
        }
    }

    /**
     * Trains a combat unit. Finds the gateway with the smallest queue and trains there
     */
    public void trainCombatUnit() {
        List<Unit> Gateways = intel.getUnitsListOfType(UnitType.Protoss_Gateway);
        assert Gateways.size() > 0: "must have a gateway to train.";

        if(intel.getSelf().minerals() - intel.getOrderedMineralUse() < UnitType.Protoss_Zealot.mineralPrice()){
            System.err.println("Not enough minerals.");
            return;
        }


        if(Gateways != null && Gateways.size() != 0){
            boolean foundTrainer = false;
            Unit hasRoom = null;

            for(Unit gw: Gateways){
                if(!foundTrainer && gw.getTrainingQueue().size() < 1){
                    foundTrainer = true;
                    gw.train(UnitType.Protoss_Zealot);
                } else if(gw.getTrainingQueue().size() < 5){
                    hasRoom = gw;
                }
            }

            if(!foundTrainer && hasRoom != null){
                hasRoom.train(UnitType.Protoss_Zealot);
            } else {
                System.err.println("No gateway with room to train unit.");
            }
        }
    }

    /**
     * get a tile position for the next expansion.
     * used to scout so it can be built, and by the expandToNewBase function
     * @return
     */
    public TilePosition getNextExpansionLocation(){
        Game game = intel.getGame();
        TilePosition self_local = intel.getSelf().getStartLocation();

        Set<BaseLocation> canidateExpansions = new LinkedHashSet<>(10);
        canidateExpansions.addAll( BWTA.getBaseLocations());

        List<BaseLocation> toremove = new ArrayList<BaseLocation>(4);

        //prepare to remove locations occupied by us
        for(Unit b : intel.getUnitsListOfType(UnitType.Protoss_Nexus)){
            toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
        }

        //prepare to remove locations occupied by our opponent
        switch (intel.getEnemyRace()){
            case Zerg:
                //TODO: if possible, optimize this. Maybe a function to just give a list
                //      of unit types rather than three loops.
                for(Unit b : intel.getUnitsListOfType(UnitType.Zerg_Hive, game.enemy())){
                    toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
                }
                for(Unit b : intel.getUnitsListOfType(UnitType.Zerg_Lair, game.enemy())){
                    toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
                }
                for(Unit b : intel.getUnitsListOfType(UnitType.Zerg_Hatchery, game.enemy())){
                    toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
                }
                break;
            case Terran:
                for(Unit b : intel.getUnitsListOfType(UnitType.Terran_Command_Center, game.enemy())){
                    toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
                }
                break;
            case Protoss:
                for(Unit b : intel.getUnitsListOfType(UnitType.Protoss_Nexus, game.enemy())){
                    toremove.add(BWTA.getNearestBaseLocation(b.getTilePosition()));
                }
                break;
            case Unknown:
                break;
        }
        //prepare to remove start locations incase we haven't scouted.
        toremove.addAll(BWTA.getStartLocations());

        //remove islands
        for(BaseLocation bl : canidateExpansions){
            if(bl.isIsland()){
                toremove.add(bl);
            }
        }

        //remove the occupied or otherwise invalid locations.
        //can still check if a position is a island and remove that too.
        canidateExpansions.removeAll(toremove);


        if(canidateExpansions.isEmpty()){
            System.err.println("Warning: no remaining valid expansions.");
        }

        Iterator<BaseLocation> l = canidateExpansions.iterator();
        TilePosition nextExpandLoc = l.next().getTilePosition();
        while (l.hasNext()){
            TilePosition current = l.next().getTilePosition();

            if(BWTA.getGroundDistance(current, self_local) < BWTA.getGroundDistance(nextExpandLoc, self_local)){
                nextExpandLoc = current;
            }
        }
        return nextExpandLoc;
    }

    /**
     * 1) finds the closest unoccupied base location
     *      using getNextExpandLocation()
     * 2) orders a base built there
     */
    public void expandToNewBase() {
        TilePosition closest = getNextExpansionLocation();

        switch (intel.getPlayerRace()){
            case Protoss:
                this.createBuildingOfTypeWithAnchor(UnitType.Protoss_Nexus, closest, 50);
                break;
            case Zerg:
                this.createBuildingOfTypeWithAnchor(UnitType.Zerg_Hatchery, closest, 50);
                break;
            //case Terran
        }
    }
}
