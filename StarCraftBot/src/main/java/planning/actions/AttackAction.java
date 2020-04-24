package planning.actions;

import burlap.mdp.core.action.Action;

/**
 * Planning action meaning that the bot should send some or all of
 * it's army to fight somewhere. Details specified in the action name.
 *
 * Why does this exist: To work with the Burlap API for choosing what
 * the bot will or could do.
 *
 * Valid options for Attack Actions:
 *      *   "what=%s"
 *      *      - "harass" = target workers
 *      *      - "base" = target a base/expansion
 *      *      - "army" = target enemy army
 *      *      - "defend" = defend our base
 *      *   "unit=%s"
 *      *      - one of the unit type names
 *      *      - "all"
 *
 *  How to add options:
 *      1) In the constructors, add a check to validate that option
 *      2) Add the option to the possibilities given to the bot in AttackActionType
 *      + add to tests and documentation
 */
public class AttackAction implements Action {
    /**
     * The name of the action.
     */
    String actionName;
    /**
     * The unique name at the start of every Attack Action that tells the bot
     * an action is an attack and not anything else.
     */
    private static final String BaseActionName = "AttackAction";

    /**
     * Creates an AttackAction just called it's base name with no other information.
     */
    public AttackAction(){
        actionName = BaseActionName;
    }


    /**
     * A constructor for an Attack Action that takes each argument
     * in an array. Currently unused (I think).
     *
     * Why this exists: For intuitive construction. An array of options
     * makes more sense than a string to me.
     * @param InputOptionsArray an array of actions
     *   "what=%s"
     *      - "harass" = target workers
     *      - "base" = target a base/expansion
     *      - "army" = target enemy army
     *      - "defend" = defend our base
     *   "unit=%s"
     *      - one of the unit type names
     *      - "all"
     *
     *  Example: new AttackAction({"what=army", "unit=all"});
     */
    public AttackAction(String[] InputOptionsArray){
        StringBuilder OptionsConcat = new StringBuilder();

        for(int i =0; i <InputOptionsArray.length; i++){
            OptionsConcat.append('_');
            OptionsConcat.append(InputOptionsArray[i]);
        }

        String options = OptionsConcat.toString();

        if(options == null || options.isEmpty()){
            actionName = BaseActionName;
        } else{
            String[] optionsArray = options.split("_");
            StringBuilder namebuilder = new StringBuilder(BaseActionName);

            int i = 0;
            if(optionsArray[i] == null || optionsArray[i].isEmpty() ||optionsArray[i].equals(BaseActionName)){
                i++;
            }

            boolean validArg = false;
            for(; i < optionsArray.length; i ++){

                if(optionsArray[i] != null && !optionsArray[i].isEmpty()){
                    validArg = false;

                    //confirm that the options are filled out properly.
                    if (optionsArray[i].startsWith("unit=") &&
                            optionsArray[i].length() > 5 /* temp check for a unit name provided */){
                        //TODO: confirm we were given a real unit, or "all".
                        //should probably be similar to the if else bellow but instead checking if there is
                        //such a unit in the bwapi
                        validArg = true;

                    }else if (optionsArray[i].startsWith("what=")){
                        if( optionsArray[i].endsWith("army") || optionsArray[i].endsWith("base")
                                || optionsArray[i].endsWith("harass") || optionsArray[i].endsWith("defend")  ){
                            validArg = false;

                        } else {
                            System.err.println("Error: improper argument for attack's what option. \n given: "+optionsArray[i]);
                        }
                    }

                    namebuilder.append('_');
                    namebuilder.append(optionsArray[i]);
                }

            }


            this.actionName = namebuilder.toString();
        }
    }


    /**
     * Constructor for an Attack Action using a single string.
     * @param options A string that may include the AttackAction's base name and the options
     *                listed bellow all separated by underscores
     *   "what=%s"
     *      - "harass" = target workers
     *      - "base" = target a base/expansion
     *      - "army" = target enemy army
     *      - "defend" = defend our base
     *   "unit=%s"
     *      - one of the unit type names
     *      - "all"
     *
     *   Example: new AttackAction(what=army_unit=all"});
     *   See test's for more.
     */
    public AttackAction(String options) {
        if(options == null || options.isEmpty()){
            actionName = BaseActionName;
        } else{
            String[] optionsArray = options.split("_");
            StringBuilder namebuilder = new StringBuilder(BaseActionName);

            int i = 0;
            if(optionsArray[i] == null || optionsArray[i].isEmpty() ||optionsArray[i].equals(BaseActionName)){
                i++;
            }

            boolean validArg = false;
            for(; i < optionsArray.length; i ++){

                if(optionsArray[i] != null && !optionsArray[i].isEmpty()){
                    validArg = false;

                    //confirm that the options are filled out properly.
                    if (optionsArray[i].startsWith("unit=") &&
                            optionsArray[i].length() > 5 /* temp check for a unit name provided */){
                        //TODO: confirm we were given a real unit, or "all".
                        //should probably be similar to the if else bellow but instead checking if there is
                        //such a unit in the bwapi
                        validArg = true;

                    }else if (optionsArray[i].startsWith("what=")){
                        if( optionsArray[i].endsWith("army") || optionsArray[i].endsWith("base")
                                || optionsArray[i].endsWith("harass") || optionsArray[i].endsWith("defend")  ){
                            validArg = false;

                        } else {
                            System.err.println("Error: improper argument for attack's what option. \n given: "+optionsArray[i]);
                        }
                    }

                    namebuilder.append('_');
                    namebuilder.append(optionsArray[i]);
                }
            }


            this.actionName = namebuilder.toString();
        }
    }

    //documentation should be inherited from bwapi??
    public String actionName() {
        return this.actionName;
    }


    //documentation should be inherited from bwapi
    public AttackAction copy() {
        return new AttackAction(this.actionName);
    }

    /**
     * This was autogenerated by intellij mostly.
     *
     * Compares two objects to see if they are the same.
     * If they are both AttackActions it compares the strings.
     *
     * @param o another object.
     * @return true if the objects are the same, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttackAction that = (AttackAction) o;
        return this.actionName.equals(that.actionName());
    }
}
