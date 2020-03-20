package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class UpgradeActionType implements ActionType {
    private final static String name = "Upgrade_Type";
    private final static String action_name = "ScoutAction";
    private static SimpleAction scoutAction = new SimpleAction(action_name);

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return new UpgradeAction(s);
    }

    @Override
    public ArrayList<Action> allApplicableActions(State state) {
        ArrayList<Action> actions = new ArrayList<Action>();
        actions.add(scoutAction);
        return actions;
    }
}
