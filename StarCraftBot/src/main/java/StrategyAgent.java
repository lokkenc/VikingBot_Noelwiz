import bwapi.*;
import bwta.*;

public class StrategyAgent extends DefaultBWListener{

    private BWClient bwClient;
    private Game game;
    private Player self;

    private IntelligenceAgent intel = new IntelligenceAgent();
    private CombatAgent combat = new CombatAgent();
    private EconomyAgent economy = new EconomyAgent();

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
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
    }

    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory(game);

        // If we're not at max population capacity
        if (self.supplyTotal() < 400) {
            //if we're running out of supply and have enough minerals ...
            if ((self.supplyTotal() - self.supplyUsed() <= 4) && (self.minerals() >= 100)) {
                game.drawTextScreen(10, 240, "Need to increase PSI");
                economy.expandPopulationCapacity(self, game);
            }
        }

        // build Forge / Cannons
        if (self.minerals() >= 150) {
            // Check if we have any forges built
            game.drawTextScreen(10, 260, "Forge available");
            if (intel.unitExists(UnitType.Protoss_Forge)) {
                if ((intel.getUnitMemoryValue(UnitType.Protoss_Forge) - intel.getBuildingUnitsOfType(self, UnitType.Protoss_Forge)) > 0) {
                    if (intel.unitExists(UnitType.Protoss_Photon_Cannon)) {
                        game.drawTextScreen(10, 270, String.format("Can build %d more Photon Cannons", (2 * intel.getUnitMemoryValue(UnitType.Protoss_Pylon)) - intel.getUnitMemoryValue(UnitType.Protoss_Photon_Cannon)));
                        if (intel.getUnitMemoryValue(UnitType.Protoss_Photon_Cannon) < (2 * intel.getUnitMemoryValue(UnitType.Protoss_Pylon))) {
                            economy.createBuildingOfType(game, self, UnitType.Protoss_Photon_Cannon);
                        }
                    } else {
                        economy.createBuildingOfType(game, self, UnitType.Protoss_Photon_Cannon);
                    }
                }
            } else {
                economy.createBuildingOfType(game, self, UnitType.Protoss_Forge);
            }
        }

        // build Gateway
        if(self.minerals() >= 150) {
            // Check if there is already a Gateway
            game.drawTextScreen(10, 250, "Gateway Avalaible");
            if(intel.unitExists(UnitType.Protoss_Gateway)) {
                if(intel.getUnitMemoryValue(UnitType.Protoss_Gateway) + intel.getBuildingUnitsOfType(self, UnitType.Protoss_Gateway) < 2) {
                    economy.createBuildingOfType(game, self, UnitType.Protoss_Gateway);
                }
            } else {
                economy.createBuildingOfType(game, self, UnitType.Protoss_Gateway);
            }
        }

        //if it's time to attack the enemy base
        if (intel.getUnitsOfType(self, UnitType.Protoss_Zealot) > 30) {
            combat.attackEnemyBase(self, UnitType.Protoss_Zealot);
        }

        //if it's a worker and we have over 10 then send it as a scout
        if (intel.getUnitsOfType(self, UnitType.Protoss_Probe) >= 8) {
            if(intel.getBaseLoc() < BWTA.getStartLocations().size()) {
                Unit scout = intel.getAvailableWorker(self);
                intel.findEnemyBase(scout, BWTA.getStartLocations().get(intel.getBaseLoc()));
                intel.addScout(scout.getID());
                intel.changeBaseLoc(intel.getBaseLoc() + 1);
            }
        }

        //iterate through my units
        for (Unit myUnit : self.getUnits()) {

            if (myUnit.getType() == UnitType.Protoss_Nexus && myUnit.isUnderAttack()) {
                combat.attackPosition(self, UnitType.Protoss_Zealot, myUnit.getPosition());
            }

            //if there's enough minerals, train a Probe
            if (myUnit.getType() == UnitType.Protoss_Nexus && self.minerals() >= 50 && (intel.getUnitsOfType(self, UnitType.Protoss_Probe) < 12 || intel.unitExists(UnitType.Protoss_Gateway))) {
                if (self.supplyTotal() - self.supplyUsed() > 4 && intel.getUnitsOfType(self, UnitType.Protoss_Probe) < 12) {
                    myUnit.train(UnitType.Protoss_Probe);
                }
            }

            //if there's enough minerals, train a Zealot
            if (myUnit.getType() == UnitType.Protoss_Gateway && self.minerals() >= 100) {
                if	(self.supplyTotal() - self.supplyUsed() > 4) {
                    myUnit.train(UnitType.Protoss_Zealot);
                }
            }

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                if(intel.isScout(myUnit.getID())) {
                    if(!intel.getEnemyBuildingMemory().isEmpty()) {
                        for(Unit unit: self.getUnits()) {
                            if(unit.getType() == UnitType.Protoss_Nexus) {
                                economy.gatherMinerals(game, myUnit, unit);
                            }
                        }
                    }
                } else {
                    economy.gatherMinerals(game, myUnit);
                }
            }
        }
    }

    public static void main(String[] args) {
        new ExampleBot().run();
    }
}
