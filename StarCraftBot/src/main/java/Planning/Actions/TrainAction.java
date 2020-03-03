package Planning.Actions;

import burlap.mdp.core.action.Action;

public class TrainAction implements Action {
    private String actionName;
    private static final String BaseActionName = "TrainAction";

    //TODO: ALLOW for options to train worker, or a specific unit by name
    //possibly could replace using the names with a unit catigoriazation we make
    //that the bot then interperates and chooses

    /**
     *
     * @param s stirng of options seperated by '_'
     *          "what=%s", unit type (worker or combatUnit)
     *          "amount=%i", the amount to train
     */
    public TrainAction(String s){
        actionName = BaseActionName.concat(s);
    }

    /**
     * Create a train action with no arguments. ... consider not allowing.
     */
    public TrainAction(){
        actionName = BaseActionName;
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new TrainAction(actionName.substring(BaseActionName.length()));
    }
}
