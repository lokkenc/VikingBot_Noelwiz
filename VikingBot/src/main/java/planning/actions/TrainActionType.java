package planning.actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class implements the ActionType abstract class from burlap
 * and is used to tell the planner what actions are possible to take at a givens
 * state
 *
 * How to incorperate new options: Probably would have to restructure the
 * AllApplicableActions function to construct all reasonable possible option
 * combinations
 *
 * How to allow training of new units:
 *  copy the second if statement, swap in the new unit's name and you should be good
 *
 * How the training capacaty array is structures:
 * [ workers[used, avail] , ground[used, avail], combat air[used, avail], support air[used, avail]]
 * So, currentCapacity[0][1] is the current  capacaity to train new workers (the [0]) that is currently
 * available (the [1]).
 * In other words, currentCapacity[index of unit type that somewhat corresponds to a protoss building][used or available]
 */
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

        //TODO: once the state is parsed, allow the planner to pass a command
        //      to train multiple units at once based on remaining capacity
        //if we have the capacity to train workers.
        if(currentCapacity[0][1] > 0){
            actions.add(new TrainAction("_what=worker_amount=1"));
        }

        //if we have the capacity to train combat units
        if(currentCapacity[1][1] > 0 || currentCapacity[2][1] > 0){
            actions.add(new TrainAction("_what=combatUnit_amount=1"));
        }

        //TODO: support training support units.

        //TODO: support training air units.

        return actions;
    }
}
