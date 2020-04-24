package agents;

import planning.actions.ActionParserHelper;
import planning.SharedPriorityQueue;
import planning.StarcraftPlanner;
import burlap.mdp.core.action.Action;
import bwapi.*;
import bwta.*;

public class StrategyAgent {
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private EconomyAgent economy;
    private StarcraftPlanner planner;
    private SharedPriorityQueue todo;

    public StrategyAgent(Game game, IntelligenceAgent intel) {
        this.game = game;
        this.self = game.self();
        this.intel = intel;
        this.economy = new EconomyAgent(intel);
        planner = new StarcraftPlanner(intel);
        todo = new SharedPriorityQueue(planner);
        planner.Initalize(todo);
    }

    public void update() {
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

            /*
            if (myUnit.getType() == UnitType.Protoss_Nexus && myUnit.isUnderAttack()) {
                combat.attackPosition(self, UnitType.Protoss_Zealot, myUnit.getPosition());
            }
            */

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

    /**
     * Checks if a given action can possibly be executed at the present time
     * @param a the action being taken
     * @return true if action can be taken, false otherwise
     */
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
