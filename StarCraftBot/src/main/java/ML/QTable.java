package ml;

import ml.actions.Action;
import ml.state.State;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps (State, Action) pairs to double values representing the q-value for executing the Action in the specific State.
 */
public class QTable extends HashMap<State, Map<Action, Double>> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * This constructor takes a Collection of ML.States and a Collection of ML.Actions and adds them to the
     * (State, (Action, Double)) HashMap.
     * @param states All possible states that will be included in the HashMap.
     * @param actions All possible actions that can occur.
     */
    public QTable(Collection<State> states, Collection<Action> actions) {
        super(states.size());

        for(State state : states) {
            Map<Action, Double> actionDoubleMap = new HashMap<>();

            for(Action action : actions) {
                actionDoubleMap.put(action, -1000.0);
            }

            this.put(state, actionDoubleMap);
        }
    }

    /**
     * Gets the (Action, double) pair for a given state.
     * @param state key to index the QTable.
     * @return Returns (Action, double) pair for the given state.
     */
    public Map<Action, Double> get(State state) {
        return super.get(state);
    }

    /**
     * This function takes a state and returns the action associated with the max value in the HashMap.
     * @param state The state used to search for the best (Action, Value) combination.
     * @return Returns the action relating to the max value in the HashMap.
     */
    public Action getMaxAction(State state) {
        Map<Action, Double> actionDoubleMap = this.get(state);
        Action action = null;
        double max = Double.NEGATIVE_INFINITY;

        for(Action act : actionDoubleMap.keySet()) {
            if(actionDoubleMap.get(act) > max) {
                max = actionDoubleMap.get(act);
                action = act;
            }
        }

        return action;
    }

    /**
     * This function takes a state and returns the max value in the HashMap.
     * @param state The state used to search for the best (Action, Value) combination.
     * @return The max value in the (Action, Value) pair in the HashMap
     */
    public double getMaxValue(State state) {
        Map<Action, Double> actionDoubleMap = this.get(state);
        double max = Double.NEGATIVE_INFINITY;

        for(Action act : actionDoubleMap.keySet()) {
            if(actionDoubleMap.get(act) > max) {
                max = actionDoubleMap.get(act);
            }
        }

        return max;
    }
}
