package planning.actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;
import planning.PlanningState;

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
 *
 * WHY DOES THIS LOOK DIFFERENT THAN THE OTHERS?
 * The states don't and shouldn't know the map lay out, and should be
 * map independent, so there's not really any information the bot needs
 * to know other than that the planner wants to expand. The bot can track
 * where the enemy base is to scout, and where it wants to look.
 * So, I just used a single instance of Burlap's simple action which
 * so there's only ever one of these in memory and that works. No need to
 * implement the Action class ourselves.
 */
public class ScoutActionType implements ActionType {
    private final static String name = "ScoutActionType";
    private final static String action_name = "ScoutAction";
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
        PlanningState ps = (PlanningState) state;
        List<Action> actions = new ArrayList<Action>(3);
        int numWorkers = ps.getNumWorkers();
        int armySize = ps.getArmySize();

        //TODO: once the state is parsed, allow the planner to pass a command
        //      to scout multiple units at once based on remaining capacity
        //if we have the capacity to scout with workers.
        if(numWorkers > 0){
            actions.add(new ScoutAction("_what=worker"));
        }

        //if we have the capacity to scout with combat units
        if(armySize > 0){
            actions.add(new ScoutAction("_what=combatUnit"));
        }

        //TODO: support scouting with air units.

        return actions;
    }
}
