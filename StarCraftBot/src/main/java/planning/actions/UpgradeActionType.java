package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;

/**
 * Currently does nothing as UpgradeAction needs to be better definied to be useful.
 * Normally would analyze a state and provide the planner with possible actions to take,
 * in this grade upgrades the bot can research.
 */
public class UpgradeActionType implements ActionType {
    /**
     * The String that is the name of this action type.
     */
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
    public ArrayList<Action> allApplicableActions(State state) {
        ArrayList<Action> actions = new ArrayList<Action>();
        //TODO: make Upgrade work.
        //actions.add(new UpgradeAction());
        return actions;
    }
}
