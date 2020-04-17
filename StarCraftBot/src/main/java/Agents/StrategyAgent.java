package Agents;

import Planning.Actions.ActionParserHelper;
import Planning.SharedPriorityQueue;
import Planning.StarcraftPlanner;
import burlap.mdp.core.action.Action;
import bwapi.*;
import bwta.*;

public class StrategyAgent extends DefaultBWListener{

    private BWClient bwClient;
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private CombatAgent combat;
    private EconomyAgent economy;

    private StarcraftPlanner planner;
    private SharedPriorityQueue todo;



    private int ML_Epoch = 14;
    private boolean training = true;
    private int frameCount = 0;

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
    }

    @Override
    public void onStart() {
        game = bwClient.getGame();
        self = game.self();
        intel =  new IntelligenceAgent(self, game);
        combat = new CombatAgent(intel);
        economy = new EconomyAgent(intel);

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap(game);
        BWTA.analyze();
        System.out.println("Map data ready");

        combat.addUnitTypeToModel(combat.getUnitClassification(UnitType.Protoss_Zealot));
        combat.loadModels();
    }

    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory(game);


        //use the planner
        //if we can do the action
        if(canExecute(todo.Peek())){
            //tell the planner to tell the enviorment to tell the bot
            //to do the action.
            planner.ExecuteAction();
        }


        /*
            if(intel.getBaseLoc() < BWTA.getStartLocations().size()) {
                Unit scout = intel.getAvailableWorker(self);
                intel.findEnemyBase(scout, BWTA.getStartLocations().get(intel.getBaseLoc()));
                intel.addScout(scout.getID());
                intel.changeBaseLoc(intel.getBaseLoc() + 1);
            }
        }
        */


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
                if (self.supplyTotal() - self.supplyUsed() > 4) {
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

    @Override
    public void onUnitDestroy(Unit unit) {
        intel.onUnitDestroy(unit);
    }

    @Override
    public void onUnitShow(Unit unit) {
        intel.onUnitShow(unit);

        //could send the scout back once we show the enemy base
    }


    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    public static void main(String[] args) {
        new StrategyAgent().run();
    }


    private boolean canExecute(Action a){
        boolean result = false;


        switch (ActionParserHelper.GetActionType(a)){
            case UPGRADE:
                //TODO: check cost of upgrade + implement
                if(self.minerals() > 200){
                    //TODO: CHANGE THIS TO TRUE WHEN implemented
                    result = false;
                }
                break;
            case TRAIN:
                //TODO: check cost of unit in action
                if(self.minerals() >= 50){
                    result = true;
                }
                break;
            case SCOUT:
                //TODO: implement scout.
                result = false;
                break;
            case EXPAND:
                if(self.minerals() > 400){
                    //TODO: implement expand.
                    result = false;
                }
                break;
            case BUILD:
                //TODO: CHECK BUILDING PRICE of the actions desired building.
                if(self.minerals() > 150){
                    //TODO: implement expand.
                    result = true;
                }
                break;
            case ATTACK:
                //TODO: implement + change this to true
                result = false;
                break;
            case UNKNOWN:
                result = false;
                break;
        }


        if (planner.roomInQueue())
            return result;
        return false;
    }
}
