import Actions.Action;
import Actions.ActionType;
import States.State;
import bwapi.Unit;

import java.util.ArrayList;

public class RewardFunction {

    /**
     * This function returns the reward to be given based on the action taken and the difference in states
     * @param current The state before the action is taken
     * @param action The action that was performed
     * @param next The state after the action is taken
     * @return A Double that contains the value of the reward
     */
    public static double getRewardValue(State current, Action action, State next) {
//        if(action.getType() == ActionType.ATTACK) { // if we are attacking then we can check damage done
//            // Reward attacking along the lines of
//            int currentEnemyTotalHealth = 0;
//            int nextEnemyTotalHealth = 0;
//
//            for (Unit unit : current.getEnemyUnits().getUnits()) { // get total enemy units health if they were max
//                currentEnemyTotalHealth += unit.getHitPoints();
//            }
//            for (Unit unit : next.getEnemyUnits().getUnits()) { // get total enemy units health if they were max
//                nextEnemyTotalHealth += unit.getHitPoints();
//            }
//
//            // This is the reward function based on damage done vs damage received.
//            if(current.getClosestEnemy() <= 1) {
//                return (current.getUnit().getType().groundWeapon().damageAmount() + current.getUnit().getType().airWeapon().damageAmount())
//                        - (current.getFriendlyHitPoints() - next.getFriendlyHitPoints());
//            } else {
//                return current.getFriendlyHitPoints() - next.getFriendlyHitPoints();
//            }
//        } else {
//            return current.getFriendlyHitPoints() - next.getFriendlyHitPoints();
//        }

        int currentNumberOfEnemies = current.getNumberOfEnemies().getValue();
        int nextNumberOfEnemies = next.getNumberOfEnemies().getValue();
        int currentEnemyHp = current.getEnemyHp().getValue();
        int nextEnemyHp = next.getEnemyHp().getValue();
        int currentFriendlyHp = current.getFriendlyHp().getValue();
        int nextFriendlyHp = next.getFriendlyHp().getValue();

        /* I believe this is the same as what's above but it abstracts away the specific Unit(s) and only uses
         * the difference in hp values between enemies and friendlies.
         */
        if(action.getType() == ActionType.ATTACK) {
            if(currentNumberOfEnemies > 0) {
                return (currentEnemyHp - nextEnemyHp) - (currentFriendlyHp - nextFriendlyHp);
            } else {
                return currentFriendlyHp - nextFriendlyHp;
            }
        } else {
            return currentFriendlyHp - nextFriendlyHp;
        }
    }
}