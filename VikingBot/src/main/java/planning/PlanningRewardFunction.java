package planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import bwapi.UnitType;
import planning.actions.BuildAction;
import planning.actions.TrainAction;
import planning.actions.helpers.ActionParserHelper;

import java.util.ArrayList;

public class PlanningRewardFunction implements RewardFunction {
    private GameStatus gameStatus;

    public PlanningRewardFunction(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Given the current game state, and action, and a resultant state, returns the reward value of that action
     * @param s A State describing the current {@link PlanningState}
     * @param a The Action being taken
     * @param sprime The Game State after Action a is taken
     * @return the reward value should that action be taken
     */
    public double reward(State s, Action a, State sprime) {
        switch (gameStatus) {
            case EARLY:
                return earlyGameReward(s, a, sprime);
            case MID:
                return midGameReward(s, a, sprime);
            default:
                return lateGameReward(s, a, sprime);
        }
    }

    /**
     * Returns the reward for the given action if the {@link GameStatus} is in the early game
     * @param s A State describing the current {@link PlanningState}
     * @param a The Action being taken
     * @param sprime The Game State after Action a is taken
     * @return the reward value should that action be taken
     * @see #reward(State, Action, State)
     */
    /*private double earlyGameReward(State s, Action a, State sprime) {
        int targetNumWorkers = 12 * (int) s.get("numBases");
        int targetMineralProduction = 500; //500 minerals per minute
        int targetGasProduction = 250; //250 units per minute
        int targetArmySize = 50;
        int maxTimeSinceLastScout = 3600;
        int incentiveMultiplier = 100;
        int populationCap = 9; //POSSIBLE VARIABLE IN STATE ???
        double reward = 0.0;
        ActionParserHelper aph = new ActionParserHelper();
        switch(aph.GetActionType(a)) {
            case ATTACK :
                if ((Boolean) sprime.get("attackingEnemyBase") == true) {
                    reward -= 500;
                } else { //We are attacking intruders
                    //reward += 100;
                }
                break;
            case BUILD:
                //Get information of what is being built?
                Random rand = new Random();
                reward += 50;
                if ((int) s.get("numWorkers") == populationCap)
                    reward += 75;
                if (rand.nextInt(10) == 2)
                    reward += 50;

                //PLACEHOLDER REWARD
                break;
            case EXPAND:
                if ((int) s.get("numBases") < 2) {
                    //Give reward if preconditions for expanding are met
                    if ((int) s.get("numWorkers") >= targetNumWorkers && (int) s.get("mineralProductionRate") >= targetMineralProduction
                            && (int) s.get("gasProductionRate") >= targetGasProduction) {
                        reward += 500;
                    }
                    else {
                        reward -= 500;
                    }
                } else {
                    reward -= 500;
                } break;
            case SCOUT :
                //Checks the last time scouted and gives reward based on that
                if ((int) s.get("timeSinceLastScout") > maxTimeSinceLastScout) {
                    reward += (int) s.get("timeSinceLastScout") / 100;
                } else {
                    reward -= (int) s.get("timeSinceLastScout") / 100;
                }
                break;
            case TRAIN:
                //TEMPORARY REWARD
                if ((int) s.get("numWorkers") < targetNumWorkers)
                    reward += 50;
                reward += 50;



                int[][] trainingCapacity = (int[][]) s.get("trainingCapacity");
                //Checking worker status
                if ((int) sprime.get("numWorkers") > (int) s.get("numWorkers")) {
                    //Disincentivize queueing units that cannot be trained
                    if (trainingCapacity[0][1] == 0)
                        reward -= 100;
                    //Given or taking reward based on if the action is training worker
                    if ((int) s.get("numWorkers") < targetNumWorkers)
                        reward += targetNumWorkers - (int) s.get("numWorkers") * incentiveMultiplier;
                    else
                        reward -= 100;
                }

                //Getting size of current army
                ArrayList<CombatUnitStatus> combatList = (ArrayList<CombatUnitStatus>) s.get("combatUnitStatuses");
                int curArmy = 0;
                for (CombatUnitStatus combatUnitStatus: combatList) {
                    curArmy += combatUnitStatus.getAmount();
                }
                //Getting size of army after action
                combatList = (ArrayList<CombatUnitStatus>) sprime.get("combatUnitStatuses");
                int sprimeArmy = 0;
                for (CombatUnitStatus combatUnitStatus: combatList) {
                    sprimeArmy += combatUnitStatus.getAmount();
                }

                //Evaluating reward based on army
                if (sprimeArmy > curArmy) {
                    if (curArmy < targetArmySize) {
                        reward += targetArmySize - curArmy;
                    } else {
                        reward -= 100;
                    }
                }
                break;
            case UPGRADE:
                reward -= 100;
                break;
        }
        return reward;
    }*/

    private double earlyGameReward(State s, Action a, State sprime) {
        //casting the state once.
        PlanningState ps = (PlanningState) s;
        //state parts taken out
        int populationCap = ps.getPopulationCapacity();

        //tarets
        int targetNumWorkers = 12 * (int) s.get("numBases");
        int targetMineralProduction = 500; //500 minerals per minute
        int targetGasProduction = 250; //250 units per minute
        int targetMilitaryTrainingCapacity = 4;
        //8 because the planner will consistantly get to 8 units
        int targetArmySize = 8;
        int maxTimeSinceLastScout = 3600;

        //planing stuff
        int incentiveMultiplier = 100;
        double reward = 0.0;

        ActionParserHelper aph = new ActionParserHelper();
        switch(aph.GetActionType(a)) {
            case ATTACK :
                String target = "";
                String actionarg[] = a.actionName().split("_");

                for(String arg : actionarg){
                    String argumentparts[] = arg.split("=");
                    switch (argumentparts[0]){
                        case "what":
                            target = argumentparts[1];
                            break;
                        //case "unit":
                        default:
                            //with what units
                            break;
                    }
                }

                if(ps.getBeingAttacked() && target.equals("defend")){
                    reward += 1000;
                } else if ((Boolean) sprime.get("attackingEnemyBase") == false && this.calcArmySize(s) >= targetArmySize){
                    reward += 750;
                } else {
                    reward -= 500;
                }
                break;

            case BUILD:
                //Get information of what is being built?
                String toBuild = ((BuildAction) a).getUnitToBuild();
                if (toBuild.equals("pop")) {
                    int popreamaining = populationCap - (int) s.get("populationUsed");

                    if( popreamaining/populationCap < 0.25){
                        reward += 15;
                    }

                    if(popreamaining <= 4){
                        reward += 85;
                    }

                    if ((int) s.get("numWorkers") == populationCap) {
                        reward += 75;
                    } else {
                        reward += 25;
                    }

                } else if (toBuild.equals("train")) {
                    int[][] capacity = ps.getTrainingCapacity();
                    if (capacity[1][0] + capacity[1][1] <= targetMilitaryTrainingCapacity){
                        reward += 50;
                    }

                    if ((int) s.get("numWorkers") < targetNumWorkers) {
                        reward -= 25;
                    }

                } else if(toBuild.equals("gas") && ps.getGasProductionRate() == 0) {
                    //will eventually build a gas
                    if(ps.getUnitMemory().getOrDefault(UnitType.Protoss_Assimilator,0) < 1){
                        reward += 50;
                    } else {
                        reward -= 25;
                    }

                } else if(toBuild.equals("research")){
                    if(ps.getUnitMemory().getOrDefault(UnitType.Protoss_Cybernetics_Core,0) < 1){
                        reward += 30;
                    }else {
                        reward -= 200;
                    }
                } else {
                    reward -= 50;
                }
                break;

            case EXPAND:
                targetNumWorkers = 8;
                if ((int) s.get("numBases") < 2) {
                    //Give reward if preconditions for expanding are met
                    if ( ps.getNumWorkers() >= targetNumWorkers && ps.getMineralProductionRate() >= targetMineralProduction
                            && (int) s.get("gasProductionRate") >= targetGasProduction) {
                        reward += 250;
                    } else if((int) s.get("numWorkers") >= targetNumWorkers - 3){
                        reward += 100;
                    }
                    else {
                        reward -= 500;
                    }
                } else {
                    reward -= 500;
                }
                break;

            case SCOUT :
                //Checks the last time scouted and gives reward based on that
                if ((int) s.get("timeSinceLastScout") > maxTimeSinceLastScout) {
                    reward += (int) s.get("timeSinceLastScout") / 100;
                } else {
                    reward -= (int) s.get("timeSinceLastScout") / 100;
                }
                break;

            case TRAIN:
                //TEMPORARY REWARD
                String toTrain = ((TrainAction) a).getUnitToTrain();
                if (toTrain.equals("worker")) {
                    if (ps.getNumWorkers() < targetNumWorkers)
                        reward += 75;
                    reward += 50;
                } else if (toTrain.equals("combatUnit")) {
                    if ((int) s.get("numWorkers") >= targetNumWorkers) {
                        reward += 150;
                    }

                    if(ps.getArmySize() < targetArmySize){
                        reward += 150;
                    }
                }
                break;

            case UPGRADE:
                reward -= 100;
                break;

            case GATHER:
                //assuming is either gas or minerals
                if(a.actionName().endsWith("gas")){
                    if( (int) s.get("gasProductionRate") > 0){
                        reward -= 100;
                    }else {
                        reward += 40;
                    }
                }else {
                    if( (int) s.get("mineralProductionRate") <= 0){
                        reward += 10;
                    } else {
                        reward -= 100;
                    }

                }
                break;
        }
        return reward;
    }


    /**
     * Returns the reward for the given action if the {@link GameStatus} is in the mid game
     * @param s A State describing the current {@link PlanningState}
     * @param a The Action being taken
     * @param sprime The Game State after Action a is taken
     * @return the reward value should that action be taken
     * @see #reward(State, Action, State)
     */
    private double midGameReward(State s, Action a, State sprime) {
        double reward = 0.0;
        return reward;
    }

    /**
     * Returns the reward for the given action if the {@link GameStatus} is in the late game
     * @param s A State describing the current {@link PlanningState}
     * @param a The Action being taken
     * @param sprime The Game State after Action a is taken
     * @return the reward value should that action be taken
     * @see #reward(State, Action, State)
     */
    private double lateGameReward(State s, Action a, State sprime) {
        double reward = 0.0;
        return reward;
    }

    /**
     * Sets the game status (Early, mid, or late)
     * @param gameStatus a {@link GameStatus} that represents the current game status
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }


    /**
     * retrieves the army size from a given state.
     * @param s a state
     * @return # combat units
     */
    private int calcArmySize(State s){
        ArrayList<CombatUnitStatus> combatUnitStatuses = (ArrayList<CombatUnitStatus>) s.get("combatUnitStatuses");
        int armysize = 0;
        for(CombatUnitStatus cbs: combatUnitStatuses){
            //intel makes sure these are combat units
            armysize += cbs.getAmount();
        }
        return armysize;
    }
}
