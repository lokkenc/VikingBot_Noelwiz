package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class TrainActionType implements ActionType {
    private final static String name = "Train_Type";

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return new TrainAction(s);
    }

    @Override
    public List<Action> allApplicableActions(State state) {
        List<Action> actions = new ArrayList<Action>(3);
        actions.add(new TrainAction("_what=worker_amount=1"));
        actions.add(new TrainAction("_what=combatUnit_amount=1"));
        actions.add(new TrainAction());
        return actions;
    }
}
