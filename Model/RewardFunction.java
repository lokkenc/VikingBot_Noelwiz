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
               /* reward = ((current.getEnemyHitPoints() - next.getEnemyHitPoints()) * UnitTypeDamageOnAttack) -
                         (current.getFriendlyHitPoints() - next.getFriendlyHitPoints()); */ // maybe this equation?
                break;
            case MOVEDOWNLEFT:
            case MOVEDOWNRIGHT:
            case MOVEUP:
            case MOVEUPLEFT:
            case MOVEUPRIGHT:
            case MOVEDOWN:
            case MOVERIGHT:
            case MOVELEFT:
                // Reward Movement
                break;
            case SPREAD:
                break;
            default:
                // Default reward?
        }

        return reward;
    }
}