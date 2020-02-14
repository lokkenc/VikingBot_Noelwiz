

public class RewardFunction {
    public static final double DEFAULT_REWARD = 100d;

    public static double getValue(State state, State next, Action action) {
        double reward;

        if(action instanceof Attack) {
            // How we will reward attacking
        }
        else if(action instanceof Explore) {
            // How we will reward exploring
        }
        else if(action instanceof Idle) {
            // How we will reward idling
        }
        else {
            reward = -DEFAULT_REWARD;
        }

        return reward;
    }
}