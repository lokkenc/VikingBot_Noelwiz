package planning.actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class GatherActionType implements ActionType {
    private static final String ActionTypeName = "GatherActionType";

    /**
     * The unique name of this {@link ActionType}
     *
     * @return unique {@link String} name of this {@link ActionType}
     */
    @Override
    public String typeName() {
        return ActionTypeName;
    }

    /**
     * Returns an {@link Action} whose parameters are specified by the given {@link String} representation (if
     * the {@link ActionType} manages multiple parameterizations)
     *
     * @param strRep the {@link String} representation of the {@link Action} parameters, if any, or null if there are no parameters.
     * @return the corresponding {@link Action}
     */
    @Override
    public Action associatedAction(String strRep) {
        return null;
    }

    /**
     * Returns all possible actions of this type that can be applied in the provided {@link State}.
     *
     * @param s the {@link State} in which all applicable  actions of this {@link ActionType} object should be returned.
     * @return a list of all applicable {@link Action}s of this {@link ActionType} object in in the given {@link State}
     */
    @Override
    public List<Action> allApplicableActions(State s) {
        List<Action> allowedActions = new ArrayList<Action>(2);
        //if we have at least one worker

        //if we own an extractor, add gather gas

        //always able to gather minerals, so add to list
        //maybe not when map is out of minerals but that would be an extreme edge
        //case not represented in the state.
        allowedActions.add(new GatherAction(GatherAction.targets[0]));

        return allowedActions;
    }
}
