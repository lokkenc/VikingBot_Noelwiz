package Planning.Actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for the Scout action for AI Planning.
 *
 * Planner use: consider the option of scouting the
 * enemy base.
 *
 * Bot Response: Choose a unit and send it to scout the enemy base.
 *
 * Future considerations: if a more detailed representation of our units is added
 * then we could specify a unit type.
 */
public class ScoutActionType implements ActionType {
    private final static String name = "Scout_type";
    private final static String action_name = "Scout_Action";
    private static SimpleAction ScoutAction = new SimpleAction(action_name);

    public String typeName() {
        return name;
    }


    /**
     * "Returns an Action whose parameters are specified by the given String representation
     * (if the ActionType manages multiple parameterizations)"
     * @param s a string representing action parameters.
     * @return a single action parametized by s.
     * In practice, this is the simple scout action as this is unprameterized for now.
     */
    public Action associatedAction(String s) {
        return ScoutAction;
    }


    /**
     * A function that returns the possible actions of type expanding.
     * @param state a possible game state, used to determine what actions are possible.
     * @return a list of actions possible to be taken of type scout.
     *         Currently this will always be the simple scout action
     *
     *         In the future, we could list a few units if they are
     *         added to the states, probably probes and maybe some
     *         smart choices
     */
    public List<Action> allApplicableActions(State state) {
        ArrayList<Action> list = new ArrayList<Action>(1);
        list.add(ScoutAction);
        return list;
    }
}
