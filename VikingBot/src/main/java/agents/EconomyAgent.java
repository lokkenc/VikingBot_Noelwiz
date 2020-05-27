package agents;

import bwapi.*;

import java.util.List;

public class EconomyAgent {

    IntelligenceAgent intel;

    public EconomyAgent( Game game){
        intel = IntelligenceAgent.getInstance(game);
    }

    /**
     * tell a worker to go gather gas at the closest gas local
     *
     * NOTE: this assumes protoss right now, will not work for zerg.
     * @param game the current game
     * @param worker a worker, presumed probe.
     */
    protected void gatherGas(Game game, Unit worker){
       List<Unit> gasLocals = intel.getUnitsListOfType(UnitType.Protoss_Assimilator);
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
     * @param game Game value created on game start
     * @param worker Unit to send to gather minerals
     * @param base Base to gather minerals near
     */
    protected void gatherMinerals (Game game, Unit worker, Unit base) {
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
     * @param game Game value created on game start
     * @param worker Unit to send to gather minerals
     */
    public void gatherMinerals (Game game, Unit worker) {
        assert(worker != null);
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
     * @param self Player assigned to the bot
     * @param game Game value created on game start
     */
    public void expandPopulationCapacity(Player self, Game game) {
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
     * @param game Game value created on game start
     * @param self Player assigned to the bot
     * @param type Building to be created
     */
    public void createBuildingOfType(Game game, Player self, UnitType type) {
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
     * @param game Game value created on game start
     * @param self Player assigned to the bot
     * @param type Building to be created
     * @param anchor Unit that serves as the anchor for the build position
     * @param maxDistance maximum distance that building can be built from the anchor
     */
    public void createBuildingOfTypeWithAnchor(Game game, Player self, UnitType type, Unit anchor, int maxDistance) {
        Unit worker = intel.getAvailableWorker();

        assert type.isBuilding() : "Must Build Buildings.";

        if ((worker != null)) {
            TilePosition buildTile = game.getBuildLocation(type, anchor.getTilePosition(), maxDistance);

            if (buildTile != null) {
                worker.build(type, buildTile);
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

        if(  intel.getSelf().minerals() - intel.getOrderedMineralUse()  < 200){
            System.err.println("Not enough minerals.");
            return;
        }


        if(Gateways != null && Gateways.size() != 0){
            Unit minTrainingGateway = Gateways.get(0);

            int minTrainingQueueLength = 4;

            for (Unit gateway: Gateways) {
                int currentsize = gateway.getTrainingQueue().size();

                if (currentsize < minTrainingQueueLength) {
                    minTrainingGateway = gateway;
                    minTrainingQueueLength = currentsize;
                }
            }

            minTrainingGateway.train(UnitType.Protoss_Zealot);
        }
    }
}
