package ML.Learning;

import ML.Actions.Action;
import ML.QTable;
import ML.States.State;
import ML.States.StateSpaceManager;

import java.util.Random;

/**
 * Selects either the best Action for a specified State or a randomly sampled Action.
 */
public class GreedyActionChooser {
    private static final double epsilon = 0.1;
    private final Random random;

    /**
     * Initialize the GreedyActionChooser with a QTable.
     */
    public GreedyActionChooser() {
        random = new Random();
    }

    /**
     * Samples a random real value between [0, 1] and either picks the best action for the current state from the QTable
     * or a random action if the sampled value is within the range [0, epsilon).
     * @param spMng the StateSpaceManager that is used select a valid action at random.
     * @param curState the current State to get the optimal Action from the QTable.
     * @return returns an Action that is either randomly selected or the optimal Action for the current State.
     */
    public Action chooseAction(StateSpaceManager spMng, State curState, QTable qtable) {
        double rand = random.nextDouble();

        if(rand < epsilon) {
            return spMng.getActionList().get(random.nextInt(4));
        } else {
            return qtable.getMaxAction(curState);
        }
    }
}
