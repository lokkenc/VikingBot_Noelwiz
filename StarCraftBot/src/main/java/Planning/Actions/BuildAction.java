package Planning.Actions;

import burlap.mdp.core.action.Action;

public class BuildAction implements Action {
    private String actionName;
    private static final String BaseActionName = "BuildAction";

    //TODO: enforce creation of new BuildActions with an _ for aguments
    public BuildAction(String s) {
        if(s == null || s.isEmpty()){
            actionName = BaseActionName;
        } else {
            actionName = BaseActionName.concat(s);
        }
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
