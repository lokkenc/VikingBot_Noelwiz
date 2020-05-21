package agents;

import burlap.mdp.core.action.Action;
import bwapi.*;
import planning.SharedPriorityQueue;
import planning.StarcraftPlanner;
import planning.actions.ActionParserHelper;

import java.util.List;

public class StrategyAgent {
    private Game game;
    private Player self;

    //units given
    private int retreatThreshold = 0;

    private IntelligenceAgent intel;
    private EconomyAgent economy;
    private CombatAgent combat;
    private StarcraftPlanner planner;
    private SharedPriorityQueue todo;
    private Action lastAct = null;

    public StrategyAgent(Game game) {
        this.game = game;
        this.self = game.self();

        this.intel = IntelligenceAgent.getInstance(game);
        this.economy = new EconomyAgent(game);
        this.combat = CombatAgent.getInstance(game);

        planner = new StarcraftPlanner(intel, this);
        todo = new SharedPriorityQueue(planner);
        planner.Initalize(todo);
    }

    public void update() {
        // use the planner
        // if we can do the action
        if(canExecute(todo.Peek())){
            // tell the planner to tell the enviorment to tell the bot to do the action
            lastAct = todo.Peek();
            planner.ExecuteAction();
        } else if(lastAct != null){
            lastAct = null;
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

        int numCombatUnits = 0;
        UnitType currentUnitType;
        //iterate through my units
        for (Unit myUnit : self.getUnits()) {
            currentUnitType = myUnit.getType();

            /*
            if (myUnit.getType() == UnitType.Protoss_Nexus && myUnit.isUnderAttack()) {
                combat.attackPosition(self, UnitType.Protoss_Zealot, myUnit.getPosition());
            }
            */

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (currentUnitType.isWorker() && myUnit.isIdle()) {
                if(intel.isScout(myUnit.getID())) {
                    if(!intel.getEnemyBuildingMemory().isEmpty()) {
                        //get a base to gather minerals at
                        for(Unit unit: self.getUnits()) {
                            if(unit.getType() == UnitType.Protoss_Nexus) {
                                economy.gatherMinerals(game, myUnit, unit);
                            }
                        }
                    }
                } else {
                    economy.gatherMinerals(game, myUnit);
                }
                //count number of combat units to decide if to retreat
            } else if (!myUnit.getType().isBuilding() && myUnit.getType().canAttack()){
                numCombatUnits++;
            }
        }

        //retreat the army.
        //TODO: figure out when to take controll again, and if we can..
        if(numCombatUnits < retreatThreshold){
            CombatAgent.getInstance(game).setSkirmish(true);
            //RETREEAT
            intel.setAttacking(false);
        }
    }

    /**
     * Hand over an army to the ML
     * @param army list of units for the ML to use
     */
    public void attackEnemy(List<Unit> army){
        if(army != null){
            combat.setSkirmish(false);
            //retreat when we only have 1/8 units remaining
            retreatThreshold = army.size()/8;

            //attacK
            intel.setAttacking(true);
            combat.controlArmy(game, army);

            if(self.getRace() == Race.Protoss){
                combat.attackEnemyBase(self, UnitType.Protoss_Zealot);
            } else if (self.getRace() == Race.Zerg) {
                combat.attackEnemyBase(self, UnitType.Zerg_Zergling);
            }

        } else {
            System.err.println("attackEnemy passed empty army");
        }
    }

    public void scoutEnemy(Unit scout) {
        if(scout != null) {
            combat.sendScout(scout);
        } else {
            System.err.println("scoutEnemy passed null scout");
        }
    }

    /**
     * Checks if a given action can possibly be executed at the present time
     * @param a the action being taken
     * @return true if action can be taken, false otherwise
     */
    private boolean canExecute(Action a){
        boolean result = false;
        int availMinerals = self.minerals() - intel.getOrderedMineralUse();

        switch (ActionParserHelper.GetActionType(a)){
            case UPGRADE:
                //TODO: check cost of upgrade + implement
                if(availMinerals > 200 ){
                    //TODO: CHANGE THIS TO TRUE WHEN implemented
                    result = false;
                }
                break;
            case TRAIN:
                int numUnits  = 1;

                if(self.supplyTotal() > self.supplyUsed()){
                    //UnitType.Protoss_Nexus.mineralPrice();
                    UnitType whatUnit = UnitType.Unknown;

                    String args[] = a.actionName().split("_");
                    for(int i = 0; i < args.length; i++){
                        String currentarg;
                        if(args[i].startsWith("what=")){
                            currentarg = args[i].split("=")[1];
                            switch (currentarg){
                                case "worker":
                                    whatUnit = UnitType.Protoss_Probe;
                                    break;
                                case "combatUnit":
                                    if( intel.getUnitsOfType(self, UnitType.Protoss_Gateway) > 0){
                                        whatUnit = UnitType.Protoss_Zealot;
                                    } //else we can't train this.
                                    break;
                            }

                        }else if(args[i].startsWith("amount=")){
                            currentarg=args[i].split("=")[1];
                            numUnits = Integer.parseInt(currentarg);
                        }
                    }

                    //TODO: check cost of unit in action
                    if(whatUnit != UnitType.Unknown){
                        result = numUnits * whatUnit.mineralPrice() <= self.minerals()
                        && self.supplyTotal() > self.supplyUsed()+ + whatUnit.supplyRequired();
                    }
                }
                break;
            case SCOUT:
                if(intel.getTimeSinceLastScout() > 90 * 30 && intel.getAvailableWorker(self) != null) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case EXPAND:
                if(self.minerals() > 400){
                    //TODO: implement expand.
                    result = false;
                }
                break;
            case BUILD:
                //we need a worker
                if(intel.getUnitsOfType(self, UnitType.Protoss_Probe) > 0){
                    String what = a.actionName().split("_")[1];
                    //check cost of building
                    if(what.equals("pop") && availMinerals > 100){
                        result = true;
                        //TODO: MORE general function to test if there's a space to biold on

                    } else if(intel.getUnitsOfType(self, UnitType.Protoss_Pylon) > 1){
                        if(what.equals("train") && availMinerals > 150){
                            result = true;
                        }
                    }
                }
                break;
            case ATTACK:
                if(intel.getUnitsListOfType(UnitType.Protoss_Zealot).size() > 0) {
                    result = true;
                }
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
