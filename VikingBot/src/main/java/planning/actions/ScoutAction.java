package planning.actions;

import burlap.mdp.core.action.Action;

/**
 * This class is meant to work similar to the Upgrade Action.
 */
public class ScoutAction implements Action {
    private String actionName;
    private static final String BaseActionName = "ScoutAction";

    /**
     * Name of the unit to be used for scouting. defaults to the empty string if no valid unit was given.
     */
    private String unitToScout = "";

    public ScoutAction(){
        actionName = BaseActionName;
    }

    /**
     * create a new scout action.
     * @param options A string representing options to specify to the bot what to upgrade.
     *          "what=%s", TO BE DETERMINED
     */
    public ScoutAction(String options){
        if(options == null || options.isEmpty()) {
            actionName = BaseActionName.concat(options);
        } else {
            String[] inputOptionsList = options.split("_");
            boolean validOption = false;
            StringBuilder name = new StringBuilder(BaseActionName);

            int i = 0;
            if(inputOptionsList[i] == null || inputOptionsList[i].isEmpty()
                    ||inputOptionsList[i].equals(BaseActionName)){
                i++;
            }

            // the following can be enclosed in a for loop if more arugments are added later
            //NOTE: see TrainAction for example, but would need to also check other possible valid options
            String[] currentOptionComponents = inputOptionsList[i].split("=");

            if(currentOptionComponents[0].equals("what") && currentOptionComponents.length == 2
                    && !(currentOptionComponents[1] == null || currentOptionComponents[1].isEmpty()) ){
                validOption = true;
            }

            if (validOption){
                name.append('_');
                name.append(inputOptionsList[i]);
                unitToScout = inputOptionsList[i];
            }
            //said for loop would end here

            this.actionName = name.toString();
        }

    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new ScoutAction(actionName.substring(BaseActionName.length()));
    }

    public String getUnitToScout() {
        return unitToScout;
    }
}
