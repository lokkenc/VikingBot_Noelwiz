package Planning.Actions;

import burlap.mdp.core.action.Action;

public class TrainAction implements Action {
    private String actionName;
    private static final String BaseActionName = "BuildAction";

    public TrainAction(String s){

    }

    public TrainAction(){
        actionName = BaseActionName;
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return null;
    }
}
