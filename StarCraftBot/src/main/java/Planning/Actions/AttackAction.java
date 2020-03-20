package Planning.Actions;

import burlap.mdp.core.action.Action;
import bwem.Base;

public class AttackAction implements Action {
    String actionName;
    private static final String BaseActionName = "AttackAction";

    public AttackAction(){
        actionName = BaseActionName;
    }


    public AttackAction(String[] InputOptionsArray){
        StringBuilder OptionsConcat = new StringBuilder();

        for(int i =0; i <InputOptionsArray.length; i++){
            OptionsConcat.append('_');
            OptionsConcat.append(InputOptionsArray[i]);
        }

        String options = OptionsConcat.toString();

        //copy pasted from other constructer
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
                        //TODO: FIll to confirm we were given a real unit. see below for example once we know our unit generalizations.
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
     *
     * @param options
     *   "what=%s"
     *      - "harass" = target workers
     *      - "base" = target a base/expansion
     *      - "army" = target enemy army
     *      - "defend" = defend our base
     *   "unit=%s"
     *      - one of the unit type names
     *      - "all"
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
                        //TODO: FIll to confirm we were given a real unit. see below for example once we know our unit generalizations.
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


    public String actionName() {
        return this.actionName;
    }


    public AttackAction copy() {
        return new AttackAction(this.actionName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttackAction that = (AttackAction) o;
        return this.actionName.equals(that.actionName());
    }
}
