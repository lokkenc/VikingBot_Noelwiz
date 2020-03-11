package Planning.Actions;

import burlap.mdp.core.action.Action;

public class UpgradeAction implements Action {
    private String actionName;
    private static final String BaseActionName = "UpgradeAction";

    public UpgradeAction(){
        actionName = BaseActionName;
    }

    /**
     *
     * @param options
     *          "what=%s", TO BE DETERMINED
     */
    public UpgradeAction(String options){
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
        return new UpgradeAction(actionName.substring(BaseActionName.length()));
    }
}
