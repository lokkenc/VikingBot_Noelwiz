package Planning.Actions;

import burlap.mdp.core.action.Action;

public class BuildAction implements Action {
    String actionName;
    private static final String BaseActionName = "BuildAction";
    private static final String[] buildOptions = new String[]{"research", "pop", "train"};

    /**
     * parse a list of option strings to add to the action name
     * and make sure they conform to the expected options.
     * @param options
     *         "research"
     *            - a building that enables research.
     *         "pop"
     *            - population, something that increases the population capacity
     *         "train"
     *            - a building that trains or enables the training for zerg of units
     * @return
     */
    public BuildAction(String options) {
        if(options == null || options.isEmpty()){
            actionName = BaseActionName;
        } else {
            String[] inputStr = options.split("_");
            int i = 0;
            if(inputStr[i] == null || inputStr[i].isEmpty() ||inputStr[i].equals(BaseActionName)){
                i++;
            }

            boolean validArg = false;
            for (int j = 0; j < buildOptions.length; j++){
                if(inputStr[i].equals(buildOptions[j])){
                    validArg = true;
                }
            }


            if(validArg){
                actionName = BaseActionName.concat("_");
                actionName = actionName.concat(inputStr[i]);
            } else {
                actionName = BaseActionName;
            }
        }
    }

    public BuildAction(){
        actionName = BaseActionName;
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new BuildAction(actionName.substring(BaseActionName.length()+1));
    }
}