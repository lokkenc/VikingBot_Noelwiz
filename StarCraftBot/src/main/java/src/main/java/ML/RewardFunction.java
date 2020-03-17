package src.main.java.ML;

import src.main.java.ML.Actions.Action;
import src.main.java.ML.Actions.ActionType;
import src.main.java.ML.Range.HpRange;
import src.main.java.ML.Range.UnitsRange;
import src.main.java.ML.States.State;

public class RewardFunction {
    /**
     * This function returns the reward to be given based on the action taken and the difference in states
     * @param current The state before the action is taken
     * @param action The action that was performed
     * @param next The state after the action is taken
     * @return A Double that contains the value of the reward
     */
    public static double getRewardValue(State current, Action action, State next) {
        if (action.getType() == ActionType.ATTACK) { // if we are attacking then we can check damage done
            if(current.getNumberOfEnemies().getValue() == 0) {
                return -1;
            } else if(current.getFriendlyHp().getRange() == HpRange.LOW && current.getEnemyHp().getRange() != HpRange.LOW) {
                return -1;
            } else {
                return 1;
            }
        } else if (action.getType() == ActionType.MOVETOWARDS) {
            if (current.getNumberOfEnemies().getValue() > 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if(current.getFriendlyHp().getRange() != HpRange.LOW && current.getNumberOfFriendlies().getValue() >= current.getNumberOfEnemies().getValue() - 2) {
                return -1;
            } else if (current.getNumberOfFriendlies().getValue() >= current.getNumberOfEnemies().getValue() - 2){
                return -1;
            } else {
                return 1;
            }
        }
    }
}