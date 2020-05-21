package planning.actions;

import burlap.mdp.core.action.Action;

public class GatherAction implements Action {
        private static final String BaseActionName = "GatherAction";
    protected static final String[] targets = {"minerals","gas"};
    private String ActionName;

    /***
     * Constructor for a gather action
     * @param what
     */
    protected GatherAction(String what){
        if (what.startsWith(BaseActionName)){
            //copying this action
            if(what.endsWith(targets[0]) || what.endsWith( targets[1])){
                this.ActionName = what; //warning, could lead to funkyness.
            }else {
                System.err.println("Warning, improper string given to Gather Action, defaulting to minerals," +
                        "presumed to be from copy().");
                this.ActionName = this.BaseActionName+"_"+targets[0];
            }
        } else {
            if(what.equals(targets[0]) || what.equals( targets[1])){
                //valid
                this.ActionName = this.BaseActionName+"_"+what;
            } else {
                System.err.println("Warning, improper string given to Gather Action, defaulting to minerals");
                this.ActionName = this.BaseActionName+"_"+targets[0];
            }
        }
    }



    /**
     * Returns the action name for this grounded action.
     *
     * @return the action name for this grounded action.
     */
    @Override
    public String actionName() {
        return ActionName;
    }

    /**
     * Returns a copy of this grounded action.
     *
     * @return a copy of this grounded action.
     */
    @Override
    public Action copy() {
        return new GatherAction(this.actionName());
    }
}
