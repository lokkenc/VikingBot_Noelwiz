package States;

import Actions.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateSpaceManager {
    private List<Action> actionList;
    private Set<State> stateSet;

    public StateSpaceManager() {
        this.actionList = getValidActions();
        this.stateSet = createStates();
    }

    public List<Action> getValidActions() {
        List<Action> actions = new ArrayList<Action>();

        // Add all implemented actions here

        return actions;
    }

    public Set<State> createStates() {
        Set<State> states = new HashSet<State>();

        // Create all the states here

        return states;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public Set<State> getStateSet() {
        return stateSet;
    }
}
