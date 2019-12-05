import bwapi.*;
import bwta.*;

import java.util.*;

public class ZergBot extends DefaultBWListener {

    private BWClient bwClient;
    private Game game;
    private Player self;

    private HashMap<UnitType, java.lang.Integer> unitMemory = new HashMap<UnitType, java.lang.Integer>();
    private HashSet<Position> enemyBuildingMemory = new HashSet<Position>();
    private int baseLoc = 0;
    private ArrayList<Integer> scouts = new ArrayList<Integer>();
    //private ArrayList<OverlordAgent> overlords = new ArrayList<OverlordAgent>();

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {

        System.out.println("Created: " + unit.getType());
        if(unit.getType() == UnitType.Zerg_Overlord){

        }
    }

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
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");
        tabulateUnits ();
        updateEnemyBuildingMemory();
        displayUnits ();


        if (self.supplyTotal() < 200) {
            //if we're running out of supply and have enough minerals ...
            if ((self.supplyTotal() - self.supplyUsed() <= 4) && (self.minerals() >= 100)) {
                game.drawTextScreen(10, 240, "Need to increase PSI");
                expandPopulationCapacity();
            }
        }

        //if it's a worker and we have over 8 then send it as a scout
        if (getUnitsOfType(UnitType.Zerg_Drone) >= 8) {
            if (baseLoc < BWTA.getStartLocations().size()) {
                Unit scout = getAvailableWorker();
                findEnemyBase(scout, BWTA.getStartLocations().get(baseLoc));
                scouts.add(scout.getID());
                baseLoc++;
            }
        }

        List<Chokepoint> chokepoints = new ArrayList<Chokepoint>(5);
        List<Unit> overlordsToMove = new ArrayList<Unit>();
        //Error without this: unmodiffyable collection.remove, collections.java:1058
        chokepoints.addAll(BWTA.getChokepoints());
        for(Unit currentUnit : getUnitsListOfType(UnitType.Zerg_Overlord)){
            if(!currentUnit.isMoving()) {
                boolean atChoke = false;

                /*TODO: check if the overlord is not at a choke point because it is hiding in an
                overlord blob for it's own saftey
                atChoke = atChoke || some test of if it's around a point where we decided to hide overlords;
                * */

                //check if the unit is at a choke point
                for(int i = 0 ; i < chokepoints.size() && !atChoke; i++){
                    Chokepoint currentPoint = chokepoints.get(i);
                    if(currentPoint.getCenter().getApproxDistance(currentUnit.getPosition()) < 2){
                        atChoke = true;
                        chokepoints.remove(currentPoint);
                        i--; //so no choke points are skipped due to removal of this
                    }
                }
                if(!atChoke){
                    overlordsToMove.add(currentUnit);
                }
            }
        }

        for (Unit currentOverlord: overlordsToMove){
            //if it's not at a choke point and there's more than zero unguarded chokepoints
            //TODO: if the few choke points near the base are observed, send off to a corner of the map
            if(chokepoints.size() > 0) {
                //find the closest unoccupied chokepoint
                Position destination = chokepoints.get(0).getCenter();
                Position currentPosition;
                int distance = currentOverlord.getPosition().getApproxDistance(destination);
                for(int i = 1; i < chokepoints.size(); i++){
                    currentPosition = chokepoints.get(i).getCenter();
                    if(distance > currentOverlord.getPosition().getApproxDistance(currentPosition)){
                        distance = currentOverlord.getPosition().getApproxDistance(currentPosition);
                        destination = currentPosition;
                    }
                }
                currentOverlord.move(destination);
            }
        }

        for (Unit myUnit : self.getUnits()) {
            drawUnitPathing(myUnit);

            //if there's enough minerals, morph larva into drone
            if (myUnit.getType() == UnitType.Zerg_Larva && self.minerals() >= 50 && getUnitsOfType(UnitType.Zerg_Drone) < 12) {
                myUnit.morph(UnitType.Zerg_Drone);
            }

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                boolean isScout = false;
                for(int IDs: scouts) {
                    if (myUnit.getID() == IDs) {
                        isScout = true;
                    }
                }

                if(isScout) {
                    if(!enemyBuildingMemory.isEmpty()) {
                        for(Unit unit: self.getUnits()) {
                            if(unit.getType() == UnitType.Zerg_Hatchery) {
                                gatherMinerals(myUnit, unit);
                            }
                        }
                    }
                } else {
                    gatherMinerals(myUnit);
                }
            }
        }
    }

    private void findEnemyBase (Unit scout, BaseLocation basePos) {
        if (basePos.equals(self.getStartLocation()))
            return;
        scout.attack(basePos.getPosition());
    }

    private int getUnitsOfType (UnitType type) {
        int numOfUnits = 0;
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                numOfUnits++;
            }
        }
        return numOfUnits;
    }

    private List<Unit> getUnitsListOfType(UnitType type){
        List<Unit> unitsList = new ArrayList<Unit>(4);
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == type) {
                unitsList.add(unit);
            }
        }
        return unitsList;
    }

    private List<Chokepoint> getUnwatchedPoints(){
        List<Chokepoint> cps = new ArrayList<Chokepoint>();

        return  cps;
    }

    private void drawUnitPathing (Unit unit) {
        if (unit.getOrderTargetPosition().getX() == 0 && unit.getOrderTargetPosition().getY() == 0) {
            return;
        }

        // Draw unit's order pathing
        if (unit.isGatheringMinerals() || unit.isCarrying()) {
            game.drawLineMap(unit.getPosition().getX(), unit.getPosition().getY(), unit.getOrderTargetPosition().getX(),
                    unit.getOrderTargetPosition().getY(), Color.Blue);
        }
        else if (unit.isIdle()) {
            game.drawLineMap(unit.getPosition().getX(), unit.getPosition().getY(), unit.getOrderTargetPosition().getX(),
                    unit.getOrderTargetPosition().getY(), Color.Black);
        }
        else if (unit.isConstructing() || unit.isBeingConstructed()) {
            game.drawLineMap(unit.getPosition().getX(), unit.getPosition().getY(), unit.getOrderTargetPosition().getX(),
                    unit.getOrderTargetPosition().getY(), Color.Green);
        }
        else {
            game.drawLineMap(unit.getPosition().getX(), unit.getPosition().getY(), unit.getOrderTargetPosition().getX(),
                    unit.getOrderTargetPosition().getY(), Color.White);
        }

        if (unit.isAttacking() || unit.isUnderAttack()) {
            game.drawLineMap(unit.getPosition().getX(), unit.getPosition().getY(), unit.getOrderTargetPosition().getX(),
                    unit.getOrderTargetPosition().getY(), Color.Red);
        }
    }

    private void tabulateUnits () {
        unitMemory.clear();

        for (Unit unit : self.getUnits()) {
            if (unit.isTraining()) {
                continue;
            }

            updateUnitMemory(unit.getType(), 1);
        }
    }

    private void updateEnemyBuildingMemory () {
        // update the hashset of enemy building positions
        for (Unit enemyUnit: game.enemy().getUnits()) {
            if (enemyUnit.getType().isBuilding()) {
                if(!enemyBuildingMemory.contains(enemyUnit.getPosition())) {
                    enemyBuildingMemory.add(enemyUnit.getPosition());
                }
            }
        }

        //remove any destroyed buildings from the memory
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

    private void updateUnitMemory (UnitType type, int amount) {
        if (unitMemory.containsKey(type)) {
            unitMemory.put(type, unitMemory.get(type) + amount);
        }
        else {
            unitMemory.put(type, amount);
        }
    }

    private Unit getAvailableLarva() {
        for (Unit unit : self.getUnits()) {
            if (unit.getType() == UnitType.Zerg_Larva) {
                return unit;
            }
        }
        return null;
    }

    private Unit getAvailableWorker() {
        // Find an available worker
        for (Unit unit : self.getUnits()) {
            if (unit.getType().isWorker() && !scouts.contains(unit.getID())) {
                return unit;
            }
        }
        return null;
    }

    private void expandPopulationCapacity() {
        Unit larva = getAvailableLarva();
        if (larva != null) {
            larva.morph(UnitType.Zerg_Overlord);

            //Additional checks to instead make another Hatchery...?
        }
    }

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

    private void gatherMinerals (Unit worker, Unit base) {
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

    public static void main(String[] args) {
        new ZergBot().run();
    }
}
