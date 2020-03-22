package ML;

import ML.Actions.Action;
import ML.States.State;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class QTable extends HashMap<State, Map<Action, Double>> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * This constructor takes a Collection of ML.States and a Collection of ML.Actions and adds them to the ML.States.State -> (Action, Double) HashMap.
     * @param states All possible states that will be included in the HashMap.
     * @param actions All possible actions that can occur.
     */
    public QTable(Collection<State> states, Collection<Action> actions) {
        super(states.size());

        for(State state : states) {
            Map<Action, Double> actionDoubleMap = new HashMap<Action, Double>();

            for(Action action : actions) {
                actionDoubleMap.put(action, 0.0);
            }

            this.put(state, actionDoubleMap);
        }
    }

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
