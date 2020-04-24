package planning;

import planning.actions.ActionParserHelper;
import planning.actions.BuildAction;
import planning.actions.TrainAction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import java.util.Random;

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
                reward -= 500;
                break;
            case BUILD:
                //Get information of what is being built?
                Random rand = new Random();
                String toBuild = ((BuildAction) a).getUnitToBuild();
                if (toBuild.equals("pop")) {
                    reward += 50;
                    if ((int) s.get("numWorkers") == populationCap)
                        reward += 75;
                    if (rand.nextInt(10) == 2)
                        reward += 50;
                } else if (toBuild.equals("train")) {
                    reward += 50;
                    if ((int) s.get("numWorkers") > targetNumWorkers) {
                        reward += 75;
                    } if (rand.nextInt(10) == 2)
                        reward += 50;
                } else {
                    reward -= 50;
                }


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
                String toTrain = ((TrainAction) a).getUnitToTrain();
                if (toTrain.equals("worker")) {
                    if ((int) s.get("numWorkers") < targetNumWorkers)
                        reward += 50;
                    reward += 50;
                } else if (toTrain.equals("combatUnit")) {
                    if ((int) s.get("numWorkers") >= targetNumWorkers) {
                        reward += 150;
                    }
                }
                break;
            case UPGRADE:
                reward -= 100;
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
}
