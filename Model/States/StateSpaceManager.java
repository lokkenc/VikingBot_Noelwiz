package States;

import Actions.*;

import java.util.*;

public class StateSpaceManager {
    private List<Action> actionList;
    private Set<State> stateSet;

    public StateSpaceManager() {
        this.actionList = getValidActions();
        this.stateSet = createStates();
    }

    public List<Action> getValidActions() {
        return Arrays.asList(new Attack(), new MoveDown(), new MoveDownLeft(), new MoveDownRight(), new MoveLeft(),
                new MoveRight(), new MoveUp(), new MoveUpLeft(), new MoveUpRight());
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
