package Planning.Actions;

import burlap.mdp.core.action.Action;

public class BuildAction implements Action {
    private String actionName;
    private static final String BaseActionName = "BuildAction";

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return null;
    }
}
