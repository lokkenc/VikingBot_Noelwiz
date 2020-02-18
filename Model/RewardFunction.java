import Actions.Action;

public class RewardFunction {
    public static final double DEFAULT_REWARD = 100.0;

    public static double getRewardValue(State current, State next, Action action) {
        double reward = DEFAULT_REWARD;

        switch (action.getType()) {
            case ATTACK:
                // Reward attacking along the lines of
                /*
                 * DEFAULT_REWARD * ((EnemiesDefeated / 10) - (AlliesDefeated / 20) - (AllyToEnemyHpDiff / 30))
                 */
                break;
            case EXPLORE:
                // Reward exploring the map
                break;
            case FLEE:
                // Reward tactical retreats
                break;
            case IDLE:
                // Reward idling
                break;
            default:
                // Default reward?
        }

        return reward;
    }
}