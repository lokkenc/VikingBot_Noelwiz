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
 * Why This Exists: To provide the ai planer with possible attacking actions.
 *
 * How to incorporate new options: in AllApplicableActions, add one or more building
 * actions with the possible arguments and old ones.I recommend doing this similar to
 * how AttackActiontype currently does
 *
 * How to add new buildings: After adding them to the BuildAction,
 * just create and add a new build action with that building as the argument
 * and add it to the actions list.
 */
public class BuildActionType implements ActionType {
    /**
     * The String that is the name of this action type.
     */
    private final static String name = "Build_Type";

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public Action associatedAction(String s) {
        return new BuildAction(s);
    }

    /**
     * Given a state, enumerate all actions that can be taken by the bot.
     * An important note for this and all similar functions is that the actual game of starcraft
     * may not match this state because this is used by the planner to explore options.
     *
     * why it exists: The BWAPI needs a way to know what action it can take in a given plausable state.
     * This function enumarates all of them based on the state given.
     *
     * how it works: lists all 3 types of buildings that the action can accepts currently.
     *
     * @param state A BWAPI state, assumed to be specifically a PlanningState.
     * @return A List of attack actions that may be taken.
     */
    @Override
    public List<Action> allApplicableActions(State state) {
        List<Action> actions = new ArrayList<Action>(3);
        actions.add(new BuildAction("_research")); //a building for upgrade researching
        actions.add(new BuildAction("_pop")); //building (or unit) that adds population
        actions.add(new BuildAction("_train")); //building to train units eg hive, or warp gate
        //actions.add(new BuildAction()); //commented out because it will likely not be parsed, nor applicable yet.
        return actions;
    }
}
