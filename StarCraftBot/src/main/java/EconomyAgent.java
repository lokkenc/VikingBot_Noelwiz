import bwapi.*;
import bwta.*;


public class EconomyAgent {

    IntelligenceAgent intel = new IntelligenceAgent();

    /**
     * Sends a worker to get the closest mineral to the specified base
     * @param game
     * @param worker
     * @param base
     */
    private void gatherMinerals (Game game, Unit worker, Unit base) {
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
     * @param game
     * @param worker
     */
    public void gatherMinerals (Game game, Unit worker) {
        Unit closestMineral = null;

        //find the closest mineral
        for (Unit neutralUnit : game.neutral().getUnits()) {
            if (neutralUnit.getType().isMineralField()) {
                if (closestMineral == null || worker.getDistance(neutralUnit) < worker.getDistance(closestMineral)) {
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
     * @param self
     * @param game
     */
    public void expandPopulationCapacity(Player self, Game game) {
        if(self.getRace() == Race.Zerg) {
            Unit larva = intel.getAvailableUnit(self, UnitType.Zerg_Larva);
            if (larva != null) {
                larva.morph(UnitType.Zerg_Overlord);

                //Additional checks to instead make another Hatchery...?
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
     * @param game
     * @param self
     * @param type
     */
    private void createBuildingOfType(Game game, Player self, UnitType type) {
        Unit worker = intel.getAvailableWorker(self);

        if((worker != null)) {
            TilePosition buildTile = game.getBuildLocation(type, self.getStartLocation());
            if(buildTile != null) {
                worker.build(type, buildTile);
            }
        }
    }

    /**
     * Builds a building of type in a suitable position determined by the anchor and maxDistance
     * @param game
     * @param self
     * @param type
     * @param anchor
     * @param maxDistance
     */
    private void createBuildingOfTypeWithAnchor(Game game, Player self, UnitType type, Unit anchor, int maxDistance) {
        Unit worker = intel.getAvailableWorker(self);

        if ((worker != null)) {
            TilePosition buildTile = game.getBuildLocation(type, anchor.getTilePosition(), maxDistance);

            if (buildTile != null) {
                worker.build(UnitType.Protoss_Forge, buildTile);
            }
        }
    }

    /**
     * Returns the count of Buildings that are of type type
     * @param self
     * @param type
     * @return
     */
    private int getBuildingUnitsOfType (Player self, UnitType type) {
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
