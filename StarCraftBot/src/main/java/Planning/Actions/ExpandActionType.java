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
 * WHY DOES THIS LOOK DIFFERENT THAN THE OTHERS?
 * The states don't and shouldn't know the map lay out, and should be
 * map independent, so there's not really any information the bot needs
 * to know other than that the planner wants to expand.
 * So, I just used a single instance of Burlap's simple action which
 * so there's only ever one of these in memory and that works. No need to
 * implement the Action class ourselves.
 */
public class ExpandActionType implements ActionType {
    private final static String name = "ExpandType";
    private final static String action_name = "ExpandAction";
    private final static SimpleAction ExpandAction = new SimpleAction(action_name);

    /**
     * Get this action type's name.
     * @return the simple Action for expanding.
     */
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
     */
    public List<Action> allApplicableActions(State state) {
        ArrayList<Action> list = new ArrayList<Action>(1);
        list.add(ExpandAction);
        return list;
    }
}
