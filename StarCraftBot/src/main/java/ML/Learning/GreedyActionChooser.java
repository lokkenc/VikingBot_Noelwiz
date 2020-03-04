package ML.Learning;

import ML.Actions.Action;
import ML.QTable;
import ML.States.State;
import ML.States.StateSpaceManager;

import java.util.Random;

public class GreedyActionChooser {

    double epsilon = 0.6;
    Random random = new Random();

    QTable qtable;

    public GreedyActionChooser(QTable qtable) {
        this.qtable = qtable;
    }

    public Action chooseAction(StateSpaceManager spMng, State curState) {
        double rand = random.nextDouble();

        if(rand < epsilon) {
            return spMng.getActionList().get(random.nextInt(8));
        } else {
            return qtable.getMaxAction(curState);
        }
    }
}
