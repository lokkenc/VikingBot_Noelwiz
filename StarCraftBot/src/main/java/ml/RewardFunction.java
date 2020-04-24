package ml;

import ml.actions.Action;
import ml.actions.ActionType;
import ml.model.UnitClassification;
import ml.range.HpRange;
import ml.state.State;
import exception.IncorrectActionTypeException;

public class RewardFunction {
    private static final double defaultReward = 100.0;
    private static final double rewardScalar = 1.0;
    private static final double skirmishScalar = 1.0;
    private static final double attackScalar = 1.0;
    private static final double goHomeScalar = 1.0;
    private static final double moveScalar = 1.0;
    private static final double retreatScalar = 1.0;

    /**
     * This function returns the reward to be given based on the action taken and the difference in states
     * @param current The state before the action is taken
     * @param action The action that was performed
     * @return A Double that contains the value of the reward
     */
    public static double getRewardValue(State current, Action action) {
        if (action.getType() == ActionType.ATTACK) { // if we are attacking then we can check damage done
            if(!current.getSkirmish()) { // if the AI Planner told us to stop fighting we should be returning to home
                return -1;
            } else {
                if (current.getNumberOfEnemies().getValue() == 0) {
                    // if there are no enemies in range than attacking will do nothing so we don't want to do this
                    return -1;
                } else if (current.getFriendlyHp().getRange() == HpRange.LOW && current.getEnemyHp().getRange() != HpRange.LOW) {
                    // if we are low and the enemy is not we should not be attacking
                    return -1;
                } else {
                    // any other case should be ok to attack
                    return 1;
                }
            }
        } else if (action.getType() == ActionType.MOVETOWARDS) {
            if(!current.getSkirmish()) { // if the AI Planner told us to stop fighting we should be returning to home
                return -1;
            } else {
                if (current.getNumberOfEnemies().getValue() > 0) {
                    // if there are enemies already in range we do not need to move towards anything
                    return -1;
                } else {
                    // otherwise we need to get some enemies in range of an attack
                    return 1;
                }
            }
        } else if (action.getType() == ActionType.RETREAT) {
            if(!current.getSkirmish()) { // if the AI Planner told us to stop fighting we should be returning to home
                return -1;
            } else {
                if (current.getNumberOfFriendlies().getValue() + 1 >= current.getNumberOfEnemies().getValue() - 2) {
                    // if our army has an even amount of troops or we have more than we should not be retreating.
                    return -1;
                } else {
                    if (current.getFriendlyHp().getRange() != HpRange.LOW) {
                        // if our force has at least 2 less units than the enemy force but our hp is fine then don't retreat
                        return -1;
                    } else {
                        // if we are low and have less units than the enemy force we need to retreat
                        return 1;
                    }
                }
            }
        } else { // If we are told to stop fighting
            if(current.getSkirmish()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * This function returns the reward to be given based on the action taken and the difference in states
     * @param current The state before the action is taken
     * @param action The action that was performed
     * @param next The state after the action is taken
     * @return A Double that contains the value of the reward
     */
    public static double getRewardValue(State current, Action action, State next) {
        double reward = 0;

        // Information on unit location
        int currentClosestEnemyRange = current.getClosestEnemy().getValue();
        int nextClosestEnemyRange = next.getClosestEnemy().getValue();

        // Information on total units and change in total units
        int currentTotalNumberOfEnemies = current.getNumberOfEnemies().getValue();
        int nextTotalNumberOfEnemies = next.getNumberOfEnemies().getValue();
        int totalNumberOfEnemiesDiff = nextTotalNumberOfEnemies - currentTotalNumberOfEnemies;
        int currentTotalNumberOfFriendlies = current.getNumberOfFriendlies().getValue();
        int nextTotalNumberOfFriendlies = next.getNumberOfFriendlies().getValue();
        int totalNumberOfFriendliesDiff = nextTotalNumberOfFriendlies - currentTotalNumberOfFriendlies;

        // Information on total HP and change in total HP
        double currentTotalEnemyHp = current.getEnemyHp().getValue();
        double currentTotalFriendlyHp = current.getFriendlyHp().getValue();
        double nextTotalEnemyHp = next.getEnemyHp().getValue();
        double nextTotalFriendlyHp = next.getFriendlyHp().getValue();
        double totalEnemyHpDiff = nextTotalEnemyHp - currentTotalEnemyHp;
        double totalFriendlyHpDiff = nextTotalFriendlyHp - currentTotalFriendlyHp;
        double normalizedHpFactor = currentTotalEnemyHp / currentTotalFriendlyHp;

        if(action.getType() == ActionType.ATTACK) {
            // If skirmish is false, the only valid action is GoHome
            if(!current.getSkirmish()) {
                reward = -(defaultReward * rewardScalar * skirmishScalar);
            }
            else {
                // Check if there are enemies nearby
                if (currentTotalNumberOfEnemies == 0) {
                    reward = -(defaultReward * rewardScalar * attackScalar);
                }
                else {
                    // Calculate the reward based on the differences in enemy and friendly HP
//                    reward = -(totalEnemyHpDiff / 10.0);
//                    System.out.println("Attack Reward : " + reward);
//                    reward *= attackScalar;
                    reward = defaultReward * rewardScalar * attackScalar;
                }
            }
        }
        else if(action.getType() == ActionType.GOHOME) {
            // If skirmish is true, all actions except GoHome are valid
            if(current.getSkirmish()) {
                reward = -(defaultReward * rewardScalar * skirmishScalar);
            }
            else {
                reward = (defaultReward * rewardScalar * goHomeScalar);
            }
        }
        else if(action.getType() == ActionType.MOVETOWARDS) {
            // If skirmish is false, the only valid action is GoHome
            if(!current.getSkirmish()) {
                reward = -(defaultReward * rewardScalar * skirmishScalar);
            }
            else {
                if(currentTotalNumberOfEnemies > 0) {
                    reward = -(defaultReward * rewardScalar * moveScalar);
                } else {
                    reward = (defaultReward * rewardScalar * moveScalar);
                }
            }
        }
        else if(action.getType() == ActionType.RETREAT) {
            // If skirmish is false, the only valid action is GoHome
            if(!current.getSkirmish()) {
                reward = -(defaultReward * rewardScalar * skirmishScalar);
            }
            else {
                // Check if there are enemies closer than 50% of attack range
                if (currentClosestEnemyRange <= 50.00 && current.getUnitClass() != UnitClassification.MELEE) {
                    reward = (defaultReward * rewardScalar * retreatScalar);
                }
                else {
                    reward = -(defaultReward * rewardScalar * retreatScalar);
                }
            }
        }
        else {
            throw new IncorrectActionTypeException("ActionType is unrecognized: " + action.getType().getName());
        }

        return reward;
    }
}