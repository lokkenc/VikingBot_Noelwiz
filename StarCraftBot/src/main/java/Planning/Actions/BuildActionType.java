package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class BuildActionType implements ActionType {
    private final static String name = "Build_Type";

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return new BuildAction(s);
    }

    @Override
    public List<Action> allApplicableActions(State state) {
        List<Action> actions = new ArrayList<Action>(3);
        actions.add(new BuildAction("_research")); //a building for upgrade researching
        actions.add(new BuildAction("_pop")); //building (or unit) that adds population
        actions.add(new BuildAction("_train")); //building to train units eg hive, or warp gate
        //actions.add(new BuildAction()); //commented out because it will likely not be parsed, nor applicable yet.
        return actions;
    }
}
