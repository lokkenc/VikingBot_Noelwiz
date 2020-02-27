package Planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

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
        int targetNumWorkers = 12;
        int targetArmySize = 50;
        double reward = 0.0;
        //Action is an attack
        if (a.actionName().equals("AttackAction")) {

        }
        else if (a.actionName().equals("BuildAction")) {

        }
        else if (a.actionName().equals("ExpandAction")) {

        }
        else if (a.actionName().equals("ScoutAction")) {
            /* CHECK LAST SCOUT TIME AND GIVE REWARD BASED ON THAT */
        }
        else if (a.actionName().equals("TrainAction")) {
            if ((int) s.get("numWorkers") < targetNumWorkers) {
                if ((int) sprime.get("numWorkers") > (int) s.get("numWorkers"))
                    reward += targetNumWorkers - (int) s.get("numWorkers");
                else
                    reward -= targetNumWorkers - (int) s.get("numWorkers");
            }

            /* CHECK FOR ARMY STATUS */
        }
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
