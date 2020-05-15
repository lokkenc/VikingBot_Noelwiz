package planning.actions;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;

import java.util.List;

public class GatherActionType implements ActionType {
    /**
     * The unique name of this {@link ActionType}
     *
     * @return unique {@link String} name of this {@link ActionType}
     */
    @Override
    public String typeName() {
        return null;
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
        return null;
    }
}
