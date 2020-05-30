package planning.actions;

import burlap.mdp.core.action.Action;

import java.util.Objects;
import java.util.Scanner;

/**
 * An Action that involves training a new unit. Currently cannot tell what
 * units it's able to train from the state.
 *
 * Why this exists: So Burlap can plan what units to train.
 *
 * Current options:
 *      "what=%s", unit type (worker or combatUnit)
 *      "amount=%i", the amount to train, integer > 0
 *
 * How to add an option:
 *      1) add validation of that option to the constructors
 *      2) add the option to the tain action type
 *      + add tests and documentation
 *
 * How to add a new Unit:
 *      1) go to the possibleUnits array
 *      2) add the string that identifys that unit.
 *      3) add that to the TrainActionType as well.
 */
public class TrainAction implements Action {
    /**
     * The name of the action.
     */
    private String actionName;
    /**
     * The unique name at the start of every Train Action that tells the bot
     * an action is an attack and not anything else.
     */
    private static final String BaseActionName = "TrainAction";
    /**
     * List of currently supported units that can be trained.
     */
    private static final String[] possibleUnits = new String[]{"worker","combatUnit"};
    /**
     * Name of the unit to be trained. defaults to the empty string if no valid unit was given.
     */
    private String unitToTrain = "";
    /**
     * The amount of the unit to train.
     */
    private int amount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainAction)) return false;
        TrainAction that = (TrainAction) o;
        return Objects.equals(actionName, that.actionName);
    }


    /**
     * Constructor for a new Training action.
     * @param options string of options separated by '_'
     *          "what=%s", unit type (worker or combatUnit)
     *          "amount=%i", the amount to train, integer > 0
     *
     * How it works: if options are provided, checks all of them to make sure they are
     *               valid given the rules above.
     */
    public TrainAction(String options ){
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
                            unitToTrain = possibleUnits[j];
                            validOption = true;
                        }
                    }

                //option is how much to build
                } else if(currentOptionComponents[0].equals("amount") && currentOptionComponents.length == 2 ){
                    Scanner numScann = new Scanner(currentOptionComponents[1]);
                    if(numScann.hasNextInt()){
                        int num = numScann.nextInt();
                        validOption = num > 0;
                        if (validOption)
                            amount = num;
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

    /**
     * Get's the name of the unit to train. Currently unused because it would
     * require casting an action to a train action.
     * @return a string, being the name of the unit, that's valid according to
     * rules, or the empty string.
     */
    public String getUnitToTrain() {
        return unitToTrain;
    }

    /**
     * Get's the number of the unit to train. Currently unused because it would
     * require casting an action to a train action.
     * @return the # of a unit to train.
     */
    public int getAmount() {
        return amount;
    }
}
