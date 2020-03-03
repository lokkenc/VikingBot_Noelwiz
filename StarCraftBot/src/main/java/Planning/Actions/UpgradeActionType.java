package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.List;

public class UpgradeActionType implements ActionType {
    private final static String name = "Upgrade_Type";

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return new UpgradeAction(s);
    }

    @Override
    public List<Action> allApplicableActions(State state) {
        return null;
    }
}
