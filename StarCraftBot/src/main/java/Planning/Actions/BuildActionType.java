package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;

import java.util.List;

public class BuildActionType implements ActionType {
    private final static String name = "Build_Type";

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return null;
    }

    @Override
    public List<Action> allApplicableActions(State state) {
        return null;
    }
}
