import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

public class FirstTwo extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;
    
    boolean pylonPlaced = false;
    boolean stopProbing = false;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }

    }

    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");

        //iterate through my units
        for (Unit myUnit : self.getUnits()) {
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");

            //if there's enough minerals, train a Probe
            if (myUnit.getType() == UnitType.Protoss_Nexus && self.minerals() >= 50 && !stopProbing) {
                myUnit.train(UnitType.Protoss_Probe);
                if(self.allUnitCount() > 7 && pylonPlaced != true) { //if there is 8 units stop building
                	stopProbing = true;
                }
            }
            
            if(myUnit.getType() == UnitType.Protoss_Probe && self.minerals() >= 400 && !pylonPlaced) { //if there are 8 units time to build a pylon
            	TilePosition buildPos = myUnit.getTilePosition();
            	if(game.canBuildHere(new TilePosition(buildPos.getX() + 3, buildPos.getY() + 3), UnitType.Protoss_Pylon)) {
            		myUnit.build(UnitType.Protoss_Pylon, new TilePosition(buildPos.getX() + 3, buildPos.getY() + 3));
            	} else {
            		myUnit.build(UnitType.Protoss_Pylon, new TilePosition(buildPos.getX() - 3, buildPos.getY() - 3));
            	}
            	pylonPlaced = true;
            	stopProbing = false;
            }
            
            //if it's a worker and it's idle
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                Unit closestMineral = null;
                
                //find the closest mineral
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the worker to gather it
                if (closestMineral != null) {
                    myUnit.gather(closestMineral, false);
                }
            }
        }

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
    }

    public static void main(String[] args) {
        new FirstTwo().run();
    }
}