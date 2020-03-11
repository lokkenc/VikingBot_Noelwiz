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
        int[][] currentCapacity = (int[][]) state.get("trainingCapacity");

        //TODO: once actions are parsed in the bot, allow the bot to pass a command to train multiple units at once based on remaining capacity
        //if we have the capacity to train workers.
        if(currentCapacity[0][1] > 0){
            actions.add(new TrainAction("_what=worker_amount=1"));
        }

        //if we have the capacity to train combat units
        if(currentCapacity[1][1] > 0 || currentCapacity[2][1] > 0){
            actions.add(new TrainAction("_what=combatUnit_amount=1"));
        }

        //actions.add(new TrainAction()); //commenting this out as it would create a lot of work to interperate in the bot.

        return actions;
    }
}
