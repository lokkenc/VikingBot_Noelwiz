import bwapi.*;
import bwta.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExampleBot extends DefaultBWListener {

    private BWClient bwClient;
    private Game game;
    private Player self;

    private HashMap<UnitType, java.lang.Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("Created: " + unit.getType());
    }

    @Override
    public void onStart() {
        game = bwClient.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap(game);
        BWTA.analyze();
        System.out.println("Map data ready");

        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
            System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
//            for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
//                System.out.print(position + ", ");
//            }
            System.out.print(baseLocation.getPosition().toString());
            System.out.println();
        }

    }

    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " +
                            self.supplyUsed() + "/" + self.supplyTotal());

        tabulateUnits ();
        displayUnits ();

        // If we're not at max population capacity
        if (self.supplyTotal() < 400) {
            //if we're running out of supply and have enough minerals ...
            if ((self.supplyTotal() - self.supplyUsed() <= 4) && (self.minerals() >= 100)) {
                game.drawTextScreen(10, 240, "Need to increase PSI");
                expandPopulationCapacity();
            }
        }

        if (self.minerals() >= 150) {
            // Check if we have any forges built
            game.drawTextScreen(10, 250, "Forge available");
            if (unitMemory.containsKey(UnitType.Protoss_Forge)) {
                if ((unitMemory.get(UnitType.Protoss_Forge) - getBuildingUnitsOfType(UnitType.Protoss_Forge)) > 0) {
                    if (unitMemory.containsKey(UnitType.Protoss_Photon_Cannon)) {
                        game.drawTextScreen(10, 260, String.format("Can build %d more Photon Cannons", (2 * unitMemory.get(UnitType.Protoss_Pylon)) - unitMemory.get(UnitType.Protoss_Photon_Cannon)));
                        if (unitMemory.get(UnitType.Protoss_Photon_Cannon) < (2 * unitMemory.get(UnitType.Protoss_Pylon))) {
                            buildPhotonCannon();
                        }
                    } else {
                        buildPhotonCannon();
                    }
                }
            } else {
                buildForge();
            }
        }

        //iterate through my units
        for (Unit myUnit : self.getUnits()) {
            // Draw unit's order pathing
            if (myUnit.isGatheringMinerals() || myUnit.isCarrying()) {
                game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), myUnit.getOrderTargetPosition().getX(),
                        myUnit.getOrderTargetPosition().getY(), bwapi.Color.Blue);
            }
            else if (myUnit.isIdle()) {
                game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), myUnit.getOrderTargetPosition().getX(),
                        myUnit.getOrderTargetPosition().getY(), bwapi.Color.Black);
            }
            else if (myUnit.isConstructing() || myUnit.isBeingConstructed()) {
                game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), myUnit.getOrderTargetPosition().getX(),
                        myUnit.getOrderTargetPosition().getY(), bwapi.Color.Green);
            }
            else {
                game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), myUnit.getOrderTargetPosition().getX(),
                        myUnit.getOrderTargetPosition().getY(), bwapi.Color.White);
            }

            if (myUnit.isAttacking() || myUnit.isUnderAttack()) {
                game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(), myUnit.getOrderTargetPosition().getX(),
                        myUnit.getOrderTargetPosition().getY(), bwapi.Color.Red);
            }

            //if there's enough minerals, train a Probe
            if (myUnit.getType() == UnitType.Protoss_Nexus && self.minerals() >= 51) {
                if (self.supplyTotal() - self.supplyUsed() > 4) {
                    myUnit.train(UnitType.Protoss_Probe);
                }
            }

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                gatherMinerals(myUnit);
            }
        }
    }

    private void tabulateUnits () {
        unitMemory.clear();

        for (Unit unit : self.getUnits()) {
            updateUnitMemory(unit.getType(), 1);
        }
    }

    private void displayUnits () {
        int xPos = 10;
        int yPos = 20;
        int maxUnitsToDisplay = 15;
        int i = 0;
        String unitStringFormat = "%s %d";

        for (UnitType type : unitMemory.keySet()) {
            if (i >= maxUnitsToDisplay) {
                break;
            }

            game.drawTextScreen(xPos, yPos, String.format(unitStringFormat, type.toString(), unitMemory.get(type)));

            yPos += 10;
            i++;
        }
    }

    /*
     * TODO: GameManager
     */
    private void updateUnitMemory (UnitType type, int amount) {
        if (unitMemory.containsKey(type)) {
            unitMemory.put(type, unitMemory.get(type) + amount);
        }
        else {
            unitMemory.put(type, amount);
        }
    }

    /*
     * TODO: WorkerManager
     */
    private void gatherMinerals (Unit worker) {
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

    /*
     * TODO: UnitManager
     */
    private Unit getAvailableWorker () {
        // Find an available worker
        for (Unit unit : self.getUnits()) {
            if (unit.getType().isWorker()) {
                return unit;
            }
        }

        return null;
    }

    /*
     * TODO: UnitManager
     */
    private boolean isUnitInRadius (Position position, int radius, UnitType type) {
        List<Unit> units;
        units = game.getUnitsInRadius(position, radius);

        for (Unit unit : units) {
            if (unit.getType() == type) {
                return true;
            }
        }

        return false;
    }
    /*
     * TODO: BuildManager
     */
    private int getBuildingUnitsOfType (UnitType type) {
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

    /*
     * TODO: BuildManager
     */
    private void expandPopulationCapacity () {
        Unit worker;
        worker = getAvailableWorker();

        if (worker != null) {
            //get a nice place to build a supply depot
            TilePosition buildTile = game.getBuildLocation(UnitType.Protoss_Pylon, self.getStartLocation());
            //and, if found, send the worker to build it (and leave others alone - break;)
            if (buildTile != null) {
                worker.build(UnitType.Protoss_Pylon, buildTile);
            }
        }
    }

    /*
     * TODO: BuildManager
     */
    private Unit getPylon () {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == UnitType.Protoss_Pylon) {
                // Other checks in here?
                return unit;
            }
        }

        return null;
    }
    /*
     * TODO: BuildManager
     */
    private Unit getPylonWithoutType (UnitType type) {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == UnitType.Protoss_Pylon) {
                if (!isUnitInRadius(unit.getPosition(), 16, type)) {
                    return unit;
                }
            }
        }

        return null;
    }

    /*
     * TODO: BuildManager
     */
    private void buildForge () {
       Unit worker, pylon;

       worker = getAvailableWorker();
       pylon = getPylon ();

        if ((worker != null) && pylon != null) {
           TilePosition buildTile = game.getBuildLocation(UnitType.Protoss_Forge, pylon.getTilePosition(), 16);

           if (buildTile != null) {
               worker.build(UnitType.Protoss_Forge, buildTile);
           }
        }
    }

    /*
     * TODO: BuildManager
     */
    private void buildPhotonCannon () {
        Unit worker, pylon;

        worker = getAvailableWorker();
        pylon = getPylonWithoutType(UnitType.Protoss_Photon_Cannon);

        if ((worker != null) && pylon != null) {
            TilePosition buildTile = game.getBuildLocation(UnitType.Protoss_Photon_Cannon, pylon.getTilePosition(), 16);

            if (buildTile != null) {
                worker.build(UnitType.Protoss_Photon_Cannon, buildTile);
            }
        }
    }

    public static void main(String[] args) {
        new ExampleBot().run();
    }
}