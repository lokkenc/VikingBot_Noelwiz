package planning.actions;

import burlap.mdp.core.action.Action;

public class GatherAction implements Action {
    private static final String BaseActionName = "Gather";
    private String ActionName;

    /**
     * Returns the action name for this grounded action.
     *
     * @return the action name for this grounded action.
     */
    @Override
    public String actionName() {
        return null;
    }

    /**
     * Returns a copy of this grounded action.
     *
     * @return a copy of this grounded action.
     */
    @Override
    public Action copy() {
        return null;
    }
}
