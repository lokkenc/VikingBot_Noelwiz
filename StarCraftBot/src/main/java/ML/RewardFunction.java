package ML;

import ML.Actions.Action;
import ML.Actions.ActionType;
import ML.Range.HpRange;
import ML.States.State;

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
                // if there are no enemies in range than attacking will do nothing so we don't want to do this
                return -1;
            } else if(current.getFriendlyHp().getRange() == HpRange.LOW && current.getEnemyHp().getRange() != HpRange.LOW) {
                // if we are low and the enemy is not we should not be attacking
                return -1;
            } else {
                // any other case should be ok to attack
                return 1;
            }
        } else if (action.getType() == ActionType.MOVETOWARDS) {
            if (current.getNumberOfEnemies().getValue() > 0) {
                // if there are enemies already in range we do not need to move towards anything
                return -1;
            } else {
                // otherwise we need to get some enemies in range of an attack
                return 1;
            }
        } else {
            if (current.getNumberOfFriendlies().getValue() + 1 >= current.getNumberOfEnemies().getValue() - 2) {
                // if our army has an even amount of troops or we have more than we should not be retreating.
                return -1;
            } else {
                if(current.getFriendlyHp().getRange() != HpRange.LOW) {
                    // if our force has at least 2 less units than the enemy force but our hp is fine then don't retreat
                    return -1;
                } else {
                    // if we are low and have less units than the enemy force we need to retreat
                    return 1;
                }
            }
        }
    }
}