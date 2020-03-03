package Planning.Actions;

import burlap.mdp.core.action.Action;
import bwapi.UnitType;

import java.util.Arrays;

public class AttackAction implements Action {
    private String actionName;
    private static final String BaseActionName = "AttackAction";

    public AttackAction(){
        actionName = BaseActionName;
    }

    public AttackAction(String[] options){
        if(options.length > 0){
            StringBuilder namebuilder = addOptions(options);
            actionName = namebuilder.toString() ;
        } else {
            actionName = BaseActionName;
        }
    }

    /**
     *
     * @param unit a unit type to attack with, probable from the state.
     * @param options
     *   "group=%s"
     *      - "all" = all combat units
     *      - "selected" = the given unit type
     *      - "bot" = delegate that to the ml or something, idk.
     *   "amount=%s" //probably could be removed
     *      - "all" = attack with all selected units
     *      - "half" = attack with half of the selected units
     *   "what=%s"
     *      - "harass" = target workers
     *      - "base" = target a base/expansion
     *      - "army" = target enemy army
     *      - "defend" = defend our base
     */
    public AttackAction(UnitType unit, String[] options){

        if(options.length > 0 && !unit.isBuilding() && unit.canAttack()){
            String[] completeOptions = Arrays.copyOf(options,options.length+1);
            completeOptions[completeOptions.length-1] = "unit="+unit.name();
            StringBuilder namebuilder = addOptions(options);
            actionName = namebuilder.toString();

        } else {
            actionName = BaseActionName;
        }
    }

    public AttackAction(String str) {
        String[] options = str.split("_");

        if (options.length > 1) {
            options = Arrays.copyOfRange(options,1,options.length);

            new AttackAction(options);
        } else{
            new AttackAction();
        }
    }

    /**
     * parse a list of option strings to add to the action name
     * and make sure they conform to the expected options.
     * @param options
     *         "group=%s"
     *            - "all" = all combat units
     *            - "selected" = the given unit type
     *            - "bot" = delegate that to the ml or something, idk.
     *         "amount=%s"
     *            - "all" = attack with all selected units
     *            - "half" = attack with half of the selected units
     *         "what=%s"
     *            - "harass" = target workers
     *            - "base" = target a base/expansion
     *            - "army" = target enemy army
     *            - "defend" = defend our base
     *         "unit=%s"
     *            - one of the unit type names
     *            TODO: CHECK IF THE HASH CODE IS EQUIVELENT TO THE NAME,
     *            AND IF SO, SWAP IT IN WHERE RELEVENT
     *            UnitType h = UnitType.valueOf(unit.name());
     *            UnitType i = UnitType.values()[unit.hashCode()];
     * @return
     */
    private static StringBuilder addOptions(String[] options){
        assert options.length > 0 : "Expected options.";

        StringBuilder namebuilder = new StringBuilder(BaseActionName);

        for(int i = 1; i < options.length; i ++){
            if(!options[i].isEmpty()){

                //confirm that the options are filled out properly.
                //Todo: consider taking this out if it effects prformance too much
                if(options[i].startsWith("group=")){
                    if( !(options[i].endsWith("all") || options[i].endsWith("selected")
                            || options[i].endsWith("bot")) ){
                        System.err.println("Error: improper argument for attack's group option.");
                    }

                }else if (options[i].startsWith("amount=")){

                    if( !(options[i].endsWith("all") || options[i].endsWith("half"))){
                        System.err.println("Error: improper argument for attack's amount option.");
                    }

                }else if (options[i].startsWith("what=")){
                    if( !(options[i].endsWith("army") || options[i].endsWith("base")
                            || options[i].endsWith("harass") || options[i].endsWith("defend"))  ){
                        System.err.println("Error: improper argument for attack's what option.");
                    }
                }

                namebuilder.append('_');
                namebuilder.append(options[i]);
            }
        }
        return  namebuilder;
    }


    public String actionName() {
        return actionName;
    }


    public Action copy() {
        AttackAction newaction;

        if(this.actionName.length() > BaseActionName.length()){
            String[] options = this.actionName.split("_");
            options = Arrays.copyOfRange(options,1,options.length);

            newaction = new AttackAction(options);
        } else {
            newaction = new AttackAction();
        }

        return newaction;
    }
}
