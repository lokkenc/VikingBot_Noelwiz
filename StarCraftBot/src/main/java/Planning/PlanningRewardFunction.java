package Planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import java.util.ArrayList;

public class PlanningRewardFunction implements RewardFunction {
    private GameStatus gameStatus;

    public PlanningRewardFunction(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

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

    private double earlyGameReward(State s, Action a, State sprime) {
        int targetNumWorkers = 12 * (int) s.get("numBases");
        int targetMineralProduction = 500; //500 minerals per minute
        int targetGasProduction = 250; //250 units per minute
        int targetArmySize = 50;
        int maxTimeSinceLastScout = 3600;
        double reward = 0.0;
        //Action is an attack
        if (a.actionName().equals("AttackAction")) {

        }
        //Action is building a structure
        else if (a.actionName().equals("BuildAction")) {
            
        }
        //Action is expanding (building a new Hatchery/Nexus)
        else if (a.actionName().equals("ExpandAction")) {
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
            }
        }
        //Action is scouting
        else if (a.actionName().equals("ScoutAction")) {
            //Checks the last time scouted and gives reward based on that
            if ((int) s.get("timeSinceLastScout") > maxTimeSinceLastScout) {
                reward += (int) s.get("timeSinceLastScout") / 100;
            } else {
                reward -= (int) s.get("timeSinceLastScout") / 100;
            }

        }
        //Action is training a new unit
        else if (a.actionName().equals("TrainAction")) {
            int[][] trainingCapacity = (int[][]) s.get("trainingCapacity");
            //Checking worker status
            if ((int) sprime.get("numWorkers") > (int) s.get("numWorkers")) {
                //Disincentivize queueing units that cannot be trained
                if (trainingCapacity[0][1] == 0)
                    reward -= 100;
                //Given or taking reward based on if the action is training worker
                 if ((int) s.get("numWorkers") < targetNumWorkers)
                    reward += targetNumWorkers - (int) s.get("numWorkers");
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


        }
        //Action is upgrading a unit
        else if (a.actionName().equals("UpgradeAction")) {

        }
        return reward;
    }
    private double midGameReward(State s, Action a, State sprime) {
        double reward = 0.0;
        return reward;
    }
    private double lateGameReward(State s, Action a, State sprime) {
        double reward = 0.0;
        return reward;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
