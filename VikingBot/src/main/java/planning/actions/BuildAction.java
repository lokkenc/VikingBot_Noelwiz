package planning.actions;

import burlap.mdp.core.action.Action;

/**
 * An Action that involves building a structure for the bot to take. Used
 * in AI Planning and starcraft integration.
 *
 * Why this exists: So Burlap can plan with the option of building structures.
 *
 * Current options:
 * This class only has one option, what to build, so it's just the unit name,
 * not what=unitname.
 *      *        "research"
 *      *            - a building that enables research.
 *      *         "pop"
 *      *            - population, something that increases the population capacity
 *      *         "train"
 *      *            - a building that trains or enables the training for zerg of units
 *
 * How to add an option:
 *      1) If more options are needed the current what to build should be made to "what=unitname"
 *      2) add validation of that option to the constructors
 *      3) add the option to the build action type
 *      + add tests and documentation
 *
 * How to add a new building:
 *      1) go to the buildOptions array
 *      2) add the string that identifys that building.
 *      3) add that to the BuildActionType as well.
 */
public class BuildAction implements Action {
    /**
     * The name of the action.
     */
    String actionName;
    /**
     * The unique name at the start of every Attack Action that tells the bot
     * an action is an attack and not anything else.
     */
    private static final String BaseActionName = "BuildAction";
    /**
     * Current list of building types.
     */
    private static final String[] buildOptions = new String[]{"research", "pop", "train", "gas"};
    private String unitToBuild;

    /**
     * parse a list of option strings to add to the action name
     * and make sure they conform to the expected options.
     *
     * How it works: parses the string
     *
     * @param options A string consisting, containing the name of the general type of building from the
     *                options listed bellow.
     *         "research"
     *            - a building that enables research. Currently a Cybernetics core for protoss
     *         "pop"
     *            - population, something that increases the population capacity
     *         "train"
     *            - a building that trains or enables the training for zerg of units
     *         "gas"
     *            - a building that is required for gas mining
     * @return a new build actions with the given options. Any invalid options are dropped.
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
                    unitToBuild = buildOptions[j];
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

    /**
     * Creates an AttackAction just called it's base name with no other information.
     */
    public BuildAction(){
        actionName = BaseActionName;
    }

    //doc string should be inherited from bwapi
    @Override
    public String actionName() {
        return actionName;
    }

    //doc string should be inherited from bwapi
    @Override
    public Action copy() {
        return new BuildAction(actionName.substring(BaseActionName.length()+1));
    }

    /**
     * Currently unused (to my knowledge) because would require casting an
     * action to BuildAction.
     * @return a string, hopefully the unit's name stored in the action.
     */
    public String getUnitToBuild() {
        return unitToBuild;
    }
}