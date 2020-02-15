package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for the expand action for AI Planning.
 *
 * Planner use: consider the option of taking
 * this action.
 *
 * Bot Response: Interpret it as
 * trying to expand, choose a near by safe expansion, and go
 * build a hive or protoss thingy there.
 *
 * Future Considerations:
 */
public class ExpandActionType implements ActionType {
    private final static String name = "Expand_Type";
    private final static String action_name = "Expand_Action";
    private static SimpleAction ExpandAction = new SimpleAction(action_name);

    public String typeName() {
        return name;
    }

    /**
     * "Returns an Action whose parameters are specified by the given String representation
     * (if the ActionType manages multiple parameterizations)"
     * @param s a string representing action parameters.
     * @return a single action parametized by s.
     * In practice, this is the simple expand action as this is unprameterized.
     */
    public Action associatedAction(String s) {
        return ExpandAction;
    }


    /**
     * A function that returns the possible actions of type expanding.
     * @param state a possible game state, used to determine what actions are possible.
     * @return a list of actions possible to be taken of type expanding.
     *         Currently this will always be the simple expand action
     *
     *         In the future, it might be advisable to take a state,
     *         predict minerals, and decide weather it's reasonably possible to
     *         expand somehow.
     *         NOTE: this can't be done using current game info because it's used
     *         for future planning.
     */
    public List<Action> allApplicableActions(State state) {
        ArrayList<Action> list = new ArrayList<Action>(1);
        list.add(ExpandAction);
        return list;
    }
}
