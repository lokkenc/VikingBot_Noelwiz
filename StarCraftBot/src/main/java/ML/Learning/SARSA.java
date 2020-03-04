package ML.Learning;

import ML.Actions.Action;
import ML.QTable;
import ML.States.State;
import ML.States.StateAction;
import ML.States.StateSpaceManager;
import bwapi.Game;
import bwapi.UnitType;

public class SARSA {

    double LEARNING_FACTOR = .5;
    double DISCOUNT_FACTOR = .6;
    double INITIAL_QVAL = Double.NEGATIVE_INFINITY;

    QTable qTable;
    StateSpaceManager spManager;
    UnitType type;
    GreedyActionChooser greedy;
    StateAction current;
    StateAction next;

    public SARSA(UnitType type) {
        this.type = type;
        spManager = new StateSpaceManager();
        qTable = new QTable(spManager.getStateSet(), spManager.getActionList());
        greedy = new GreedyActionChooser(qTable);
    }

    public void setCurrentStateAction(State state, Game game) {
        Action action = greedy.chooseAction(spManager, state);
        current.setState(state);
        current.setAction(action);
        action.doAction(game, state.getUnit());
    }

    public void setNextStateAction(State state, Game game) {
        Action action = greedy.chooseAction(spManager, state);
        next.setState(state);
        next.setAction(action);
    }

    public void UpdateQTable(State state) {
        Action action = greedy.chooseAction(spManager, state);

    }

}
