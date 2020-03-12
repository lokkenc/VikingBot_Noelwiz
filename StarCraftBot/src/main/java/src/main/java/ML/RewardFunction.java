package src.main.java.ML;

import src.main.java.ML.Actions.Action;
import src.main.java.ML.Actions.ActionType;
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
            // This is the reward function based on damage done vs damage received.
//            if (current.getNumberOfEnemies().getValue() >= 1) {
//                return (current.getUnit().getType().groundWeapon().damageAmount() + current.getUnit().getType().airWeapon().damageAmount())
//                        - (current.getFriendlyHp().getValue() - next.getFriendlyHp().getValue());
//            } else {
//                return current.getFriendlyHp().getValue() - next.getFriendlyHp().getValue();
//            }
            double allyHpDiff = (current.getFriendlyHp().getValue() - next.getFriendlyHp().getValue());
            double enemyHpDiff = (current.getEnemyHp().getValue() - next.getEnemyHp().getValue());

            return enemyHpDiff - allyHpDiff;
        } else {
            return current.getFriendlyHp().getValue() - next.getFriendlyHp().getValue() - 50;
        }
    }
}