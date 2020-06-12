package agents;

import burlap.mdp.core.action.Action;
import bwapi.*;
import bwta.BWTA;
import planning.SharedPriorityQueue;
import planning.StarcraftPlanner;
import planning.actions.BuildAction;
import planning.actions.helpers.ActionParser;
import planning.actions.helpers.ProtossBuildingParser;

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


    //expansion hack
    private canExpand expandVisionState = canExpand.NeedScouting;
    private int scoutID = -1;
    private enum canExpand{NeedScouting, ScouttingExpand, ExpandScouted};


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

        int numCombatUnits = 0;
        UnitType currentUnitType;
        //iterate through our units
        for (Unit myUnit : self.getUnits()) {
            currentUnitType = myUnit.getType();

            //if it's a worker and it's idle, send it to the closest mineral patch
            if (currentUnitType.isWorker() && myUnit.isIdle() && !intel.isScout(myUnit.getID())) {
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
            } else if (!currentUnitType.isBuilding() && currentUnitType.canAttack() && !currentUnitType.isWorker()){
                numCombatUnits++;
            }
        }


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


        game.drawTextScreen(100,70,"expansion scout state:"+expandVisionState.name());
        TilePosition nextBaseLoc = economy.getNextExpansionLocation();
        switch (expandVisionState){
            case NeedScouting:
                game.drawCircleMap(nextBaseLoc.toPosition(), 100 ,Color.Red, true);
                Unit peekingWorker = intel.getAvailableWorker();
                scoutID = peekingWorker.getID();
                intel.addScout(scoutID);
                peekingWorker.getOrder();
                if(peekingWorker.isGatheringGas()){
                    Unit resourcenode = peekingWorker.getTarget();
                    peekingWorker.move(nextBaseLoc.toPosition(),true);
                    peekingWorker.gather(resourcenode,true);
                } else {
                    //else just move it back here
                    peekingWorker.move(nextBaseLoc.toPosition(),false);
                }
                expandVisionState = canExpand.ScouttingExpand;
                break;
            case ScouttingExpand:
                game.drawCircleMap(nextBaseLoc.toPosition(), 100 ,Color.Yellow, true);
                if(game.isVisible(nextBaseLoc)){
                    expandVisionState = canExpand.ExpandScouted;
                }

                peekingWorker = game.getUnit(scoutID);
                Position nearestchoke = BWTA.getNearestChokepoint(nextBaseLoc).getCenter();
                if(peekingWorker.isIdle() || peekingWorker.isGatheringGas() || peekingWorker.isGatheringMinerals()){
                    peekingWorker.move( nextBaseLoc.toPosition());
                    /*
                    //if the dist to the chokepoint is more less than dist to base loc, go to base.
                    if(BWTA.getGroundDistance(peekingWorker.getTilePosition(), nearestchoke.toTilePosition())
                            <= BWTA.getGroundDistance(nearestchoke.toTilePosition(), nextBaseLoc) ){
                        peekingWorker.move( nextBaseLoc.toPosition());
                    } else {
                        //else go to chokepoint first
                        peekingWorker.move( nearestchoke);
                    }
                    */
                }
                break;
            case ExpandScouted:
                game.drawCircleMap(nextBaseLoc.toPosition(), 100 ,Color.Green);
                if(game.getUnit(scoutID).isIdle()){
                    intel.removeScout(scoutID);
                }

                if(!game.isBuildable(nextBaseLoc)){
                    expandVisionState = canExpand.NeedScouting;
                }
                break;
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
        if(!(unit.getType().isWorker() && unit.getType().isBuilding()) ){
            //todo, check if defending too.
            if(intel.isAttackingEnemy()){
                ArrayList<Unit> newunit = new ArrayList<>(1);
                newunit.add(unit);
                CombatAgent.getInstance(game).controlArmy(game, newunit);
            } else {
                Position gotoposition = BWTA.getNearestChokepoint(unit.getPosition()).getCenter();
                if(gotoposition == null){
                    System.err.println("No chokepoint to station unit at.");
                }else if(game.hasPath(unit.getPosition(), gotoposition)){
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

        switch (ActionParser.GetActionType(a)){
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
                                    if( intel.getUnitCountOfType(UnitType.Protoss_Gateway) > 0){
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
                if(availMinerals >= 400 && intel.getUnitCountOfType(UnitType.Protoss_Probe) > 0
                        && expandVisionState == canExpand.ExpandScouted){
                    result = true;
                }
                break;
            case BUILD:
                //we need a worker
                if(intel.getUnitCountOfType(UnitType.Protoss_Probe) > 0){
                    String what = a.actionName().split("_")[1];
                    //check cost of building
                    UnitType whatUnit = ProtossBuildingParser.translateBuilding(what);
                    if((what.equals("pop") || what.equals("gas") ) && availMinerals > whatUnit.mineralPrice()){
                        result = true;
                        //TODO: MORE general function to test if there's a space to build on
                    } else if(intel.getUnitCountOfType(UnitType.Protoss_Pylon) >= 1){
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
                if(intel.getUnitCountOfType(UnitType.Protoss_Probe) > 0){
                    //check if we have the ability to get gass.
                    if(a.actionName().endsWith("gas")){
                        if(intel.getUnitCountOfType(UnitType.Protoss_Assimilator) > 0){
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
        //Unit worker = intel.getAvailableWorkerNotGathering(isMinerals);
        Unit worker = intel.getAvailableWorker();

        if(isMinerals && intel.getBuildingUnitsOfType(UnitType.Protoss_Nexus) > 0){
            //economy.gatherMinerals(game, worker);
            Unit base = intel.getUnitsListOfType(UnitType.Protoss_Nexus).get(0);
            economy.gatherMinerals(worker, base);
        } else {
            economy.gatherGas(worker);
        }
    }
}
