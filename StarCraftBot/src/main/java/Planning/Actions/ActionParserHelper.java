package Planning.Actions;

import burlap.mdp.core.action.Action;

public class ActionParserHelper {

    /**
     * Enum for what the action is
     */
    public enum ActionEnum{
        UPGRADE, TRAIN, SCOUT, EXPAND, BUILD, ATTACK, UNKNOWN
    }

    public static ActionEnum GetActionType(Action theAction){
        String actionname = theAction.actionName();
        assert theAction != null : "null actions not allowed";
        String[] parts = actionname.split("_");
        ActionEnum result = ActionEnum.UNKNOWN;

        //I think I learned that if there is no _, parts[0] is null
        String actiontypename;
        if(parts[0] == null || parts[0].isEmpty()){
            System.err.println("error: no string in action name");
            actiontypename= "bad_argument";
        } else {
            actiontypename = parts[0];
        }

        //System.out.println(actiontypename);

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
