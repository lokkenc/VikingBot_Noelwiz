package Planning.Actions;

import burlap.mdp.core.action.Action;

public class UpgradeAction implements Action {
    private String actionName;
    private static final String BaseActionName = "UpgradeAction";

    public UpgradeAction(){
        actionName = BaseActionName;
    }

    public UpgradeAction(String options){
        actionName = BaseActionName.concat(options);
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new UpgradeAction(actionName.substring(BaseActionName.length()));
    }
}
