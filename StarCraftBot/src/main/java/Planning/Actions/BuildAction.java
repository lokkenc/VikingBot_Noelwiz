package Planning.Actions;

import burlap.mdp.core.action.Action;

public class BuildAction implements Action {
    private String actionName;
    private static final String BaseActionName = "BuildAction";

    public BuildAction(String s) {
        actionName = BaseActionName.concat(s);
    }

    public BuildAction(){
        actionName = BaseActionName;
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new BuildAction(actionName.substring(BaseActionName.length()));
    }
}
