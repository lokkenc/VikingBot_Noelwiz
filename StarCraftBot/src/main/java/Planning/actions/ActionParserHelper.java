package planning.actions;

import burlap.mdp.core.action.Action;

/**
 * This class is made to identify the type of action
 * easily.
 *
 * Why this exists: So that the code to parse the type
 * of action isn't repeated frequently.
 */
public class ActionParserHelper {

    /**
     * Enum for what type of action a given action is
     *
     * Why this exists: So that the concrete Action class
     * can be parsed easily.
     */
    public enum ActionEnum{
        UPGRADE, TRAIN, SCOUT, EXPAND, BUILD, ATTACK, UNKNOWN
    }

    /**
     * Gets an enumerator representing the action type from the Actions
     * module.
     *
     * Why this exists: To simplify action decoding for use in the bot.
     *
     * How this works: Looks at the action's name up to the first
     * '_' and then matches that to the types.
     *
     * @param theAction any Action object. Expected to be
     *                  one of the ones from the Actions module.
     * @return an ActionEnum indicating the action type. Defaults to
     * UNKNOWN if the action does not match any others.
     */
    public static ActionEnum GetActionType(Action theAction){
        assert theAction != null : "null actions not allowed";
        String actionname = theAction.actionName();
        String[] parts = actionname.split("_");

        //defaults to UNKNOWN
        ActionEnum result = ActionEnum.UNKNOWN;

        //Because if there is no _, parts[0] is null
        String actiontypename;
        if(parts[0] == null || parts[0].isEmpty()){
            System.err.println("error: no string in action name");
            actiontypename= "bad_argument";
        } else {
            actiontypename = parts[0];
        }

        switch(actiontypename){
            case "AttackAction":
                result = ActionEnum.ATTACK;
                break;
            case "BuildAction":
                result = ActionEnum.BUILD;
                break;
            case "ScoutAction":
                result = ActionEnum.SCOUT;
                break;
            case "TrainAction":
                result = ActionEnum.TRAIN;
                break;
            case "UpgradeAction":
                result = ActionEnum.UPGRADE;
                break;
            case "ExpandAction":
                result = ActionEnum.EXPAND;
            default:
                //RESULT defualts to UNKNONW.
                break;

        }
        return result;
    }
}
