package Planning.Actions;

import burlap.mdp.core.action.Action;

import java.util.Objects;
import java.util.Scanner;

public class TrainAction implements Action {
    private String actionName;
    private static final String BaseActionName = "TrainAction";
    private static final String[] possibleUnits = new String[]{"worker","combatUnit"};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainAction)) return false;
        TrainAction that = (TrainAction) o;
        return Objects.equals(actionName, that.actionName);
    }


    /**
     * Constructer for a new Training action.
     * @param options stirng of options seperated by '_'
     *          "what=%s", unit type (worker or combatUnit)
     *          "amount=%i", the amount to train, integer > 0
     */
    public TrainAction(String options){
        if(options == null || options.isEmpty()){
            actionName = BaseActionName.concat(options);

        } else {
            String[] inputOptionsList = options.split("_");

            int i = 0;
            if(inputOptionsList[i] == null || inputOptionsList[i].isEmpty()
                    ||inputOptionsList[i].equals(BaseActionName)){
                i++;
            }

            StringBuilder name = new StringBuilder(BaseActionName);
            boolean validOption;

            for(; i < inputOptionsList.length; i++){
                validOption = false;
                String[] currentOptionComponents = inputOptionsList[i].split("=");

                //options is what to build
                if(currentOptionComponents[0].equals("what") && currentOptionComponents.length == 2){
                    for (int j = 0; j < possibleUnits.length; j++) {
                        if(currentOptionComponents[1].equals(possibleUnits[j])){
                            validOption = true;
                        }
                    }

                //option is how much to build
                } else if(currentOptionComponents[0].equals("amount") && currentOptionComponents.length == 2 ){
                    Scanner numScann = new Scanner(currentOptionComponents[1]);
                    if(numScann.hasNextInt()){
                        validOption = numScann.nextInt() > 0;
                    }
                }

                if(validOption){
                    name.append('_');
                    name.append(inputOptionsList[i]);
                }
                //else don't add it because the option is invalid.
            }

            this.actionName = name.toString();
        }

    }

    /**
     * Create a train action with no arguments. ... consider not allowing.
     */
    public TrainAction(){
        actionName = BaseActionName;
    }

    @Override
    public String actionName() {
        return actionName;
    }

    @Override
    public Action copy() {
        return new TrainAction(actionName.substring(BaseActionName.length()));
    }
}
