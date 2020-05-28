package agents;

import burlap.mdp.core.action.Action;
import bwapi.*;
import bwta.BWTA;
import planning.SharedPriorityQueue;
import planning.StarcraftPlanner;
import planning.actions.BuildAction;
import planning.actions.helpers.ActionParserHelper;
import planning.actions.helpers.ProtossBuildingParserHelper;

import java.util.ArrayList;
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

    /**
     * Called every frame by the listener
     */
    public void update() {
        // use the planner
        // if we can do the action
        Action next = todo.Peek();
        game.drawTextScreen(100,100,"Next in queue:"+next.actionName());
        game.drawTextScreen(100,90,"Current Minerals:"+self.minerals());
        game.drawTextScreen(100,80,"Minerals to Spend minerals:"+intel.getOrderedMineralUse());
        if(canExecute(next) && lastAct != next /* slightly reduce spam*/){
            // tell the planner to tell the environment to tell the bot to do the action
            lastAct = next;
            planner.ExecuteAction();
        } else if(lastAct != null){
            //reset last act after a frame has passed.
            lastAct = null;
        }


        int numCombatUnits = 0;
        UnitType currentUnitType;
        //iterate through our units
        for (Unit myUnit : self.getUnits()) {
            currentUnitType = myUnit.getType();

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (currentUnitType.isWorker() && myUnit.isIdle()) {
                if(intel.isScout(myUnit.getID())) {
                    if(!intel.getEnemyBuildingMemory().isEmpty()) {
                        //get a base to gather minerals at
                        for(Unit unit: self.getUnits()) {
                            if(unit.getType() == UnitType.Protoss_Nexus) {
                                economy.gatherMinerals(myUnit, unit);
                            }
                        }
                    }
                } else {
                    economy.gatherMinerals(myUnit);
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
            intel.setAttacking(false);
        }
    }


    /**
     * called by the listener's onUnitComplete function
     * @param unit the unit completed
     */
    public void useCompletedUnit(Unit unit){
        if(!(unit.canGather() && unit.getType().isBuilding())){
            //todo, check if defending too.
            if(intel.isAttackingEnemy()){
                ArrayList<Unit> newunit = new ArrayList<>(1);
                newunit.add(unit);
                CombatAgent.getInstance(game).controlArmy(game, newunit);
            } else {
                Position gotoposition = BWTA.getNearestChokepoint(unit.getPosition()).getCenter();
                if(BWTA.isConnected(unit.getTilePosition(), gotoposition.toTilePosition())){
                    unit.move(gotoposition);
                }
            }
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

        //the minerals used and about to be used.
        int availMinerals = self.minerals() - intel.getOrderedMineralUse();

        switch (ActionParserHelper.GetActionType(a)){
            //TODO: IMPLEMENT THIS ACTION, RETURN true when possible
            case UPGRADE:
                //TODO: check cost of upgrade
                if(availMinerals > 200 ){
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
                                    if( intel.getUnitCountOfType(self, UnitType.Protoss_Gateway) > 0){
                                        whatUnit = UnitType.Protoss_Zealot;
                                    } //else we can't train this.
                                    break;
                            }

                        }else if(args[i].startsWith("amount=")){
                            currentarg=args[i].split("=")[1];
                            numUnits = Integer.parseInt(currentarg);
                        }
                    }

                    if(whatUnit != UnitType.Unknown){
                        result = numUnits * whatUnit.mineralPrice() <= availMinerals
                        && self.supplyTotal() > self.supplyUsed()+ + whatUnit.supplyRequired();
                    }
                } else {
                    //TODO fix root issue rather than use this.
                    todo.EnQueue(new BuildAction("pop"));
                }
                break;

            case SCOUT:
                result = intel.getTimeSinceLastScout() > 30 && intel.getAvailableWorker() != null;
                break;

            case EXPAND:
                if(availMinerals >= 400 && intel.getUnitCountOfType(self, UnitType.Protoss_Probe) > 0){
                    result = true;
                }
                break;
            case BUILD:
                //we need a worker
                if(intel.getUnitCountOfType(self, UnitType.Protoss_Probe) > 0){
                    String what = a.actionName().split("_")[1];
                    //check cost of building
                    UnitType whatUnit = ProtossBuildingParserHelper.translateBuilding(what);
                    if((what.equals("pop") || what.equals("gas") ) && availMinerals > whatUnit.mineralPrice()){
                        result = true;
                        //TODO: MORE general function to test if there's a space to build on
                    } else if(intel.getUnitCountOfType(self, UnitType.Protoss_Pylon) > 1){
                        if(availMinerals > whatUnit.mineralPrice()){
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

            case GATHER:
                //if we have probes
                if(intel.getUnitCountOfType(self, UnitType.Protoss_Probe) > 0){
                    //check if we have the ability to get gass.
                    if(a.actionName().endsWith("gas")){
                        if(intel.getUnitCountOfType(self, UnitType.Protoss_Assimilator) > 0){
                            result = true;
                        }
                    } else {
                        //ASSUMING that this is gathering minerals
                        result = true;
                    }
                } else {
                    result = false;
                }
                break;
            case UNKNOWN:
                result = false;
                break;
        }

        if (planner.roomInQueue())
            return result;
        else{
            return false;
        }
    }


    /**
     * Tell the economy agent to gather.
     *
     * placed here because the economy has trouble getting units.
     * @param isMinerals is this order to gather minerals (true) or gather gas (false)
     */
    public void executeGatherAction(boolean isMinerals){
        Unit worker = intel.getAvailableWorkerNotGathering(isMinerals);

        if(isMinerals && intel.getBuildingUnitsOfType(self, UnitType.Protoss_Nexus) > 0){
            //economy.gatherMinerals(game, worker);
            Unit base = intel.getUnitsListOfType(UnitType.Protoss_Nexus).get(0);
            economy.gatherMinerals(worker, base);
        } else {
            economy.gatherGas(worker);
        }
    }
}
