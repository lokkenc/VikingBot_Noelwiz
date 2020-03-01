package Planning;

import Planning.Actions.ActionParserHelper;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;
import bwapi.Race;
import bwapi.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarcraftModel implements FullModel {
    Random rng = new Random();

    /*calculates the average time to train for randomizing the chance of a
     * unit finishing it's training. the units in the paranthesies are in
     * seconds, taken from the wiki.
     * TODO: incorperate more units the bot is likely to use in the furute.
     */
    private static final float AverageUnitTrainingTime = 30 * /* conversion from seconds to frames */
    (40 /* zelot*/ + 20 /* probe */ + 28 /*zergling */ + 40 /* overlord */ + 20 /* drone */)/5;

    /**
     * Possible transitions from the current state
     * @param state current state
     * @param action action being considered
     * @return a list of most possible states that could be transitioned to.
     */
    @Override
    public List<TransitionProb> transitions(State state, Action action) {
        double TotalProbablity = 1;
        State baseNextState;
        //TODO: make the baseNextState a list of states because there could be multiple possible states
        //example: if we scout while the enemy race is unknown, 1/3 chance for the enemy to be
        //a given race, maybe 1% we fail to do anything.

        /* * * start account for general changes * * */
        int[][] capacity = (int[][]) state.get("gameStatus");
        Race ourrace = (Race) state.get("playerRace");


        if(ourrace==Race.Zerg){
            //if there's something training, flip a coin to see if it completes
            if(capacity[0][0] > 0){
                for(int unit = 0; unit<capacity[0][0]; unit++){
                    //deviding by 1800 to make this the chance of the unit finishing
                    //at a given time in a single minute.
                    //TODO: someone who's smart with probability should adjust this chance here, and for protoss below
                    if(rng.nextFloat() > (AverageUnitTrainingTime/1800)){
                        capacity[0][0] -= 1;
                        capacity[0][1] += 1;

                        capacity[1][0] -= 1;
                        capacity[1][1] += 1;

                        capacity[2][0] -= 1;
                        capacity[2][1] += 1;

                        capacity[3][0] -= 1;
                        capacity[3][1] += 1;
                    }
                }
            }

        } else {
            for(int catagory = 0; catagory<capacity.length; catagory++){
                if(capacity[catagory][0] > 0){
                    for(int tainingcapacityslot = 0; tainingcapacityslot< capacity[catagory][0];tainingcapacityslot++){
                        if(rng.nextFloat() > (AverageUnitTrainingTime/1800)){
                            capacity[catagory][0] -= 1;
                            capacity[catagory][1] += 1;
                        }
                    }
                }
            }
        }
        /* * * end account for general changes * * */


        String actionstr = action.actionName();
        String[] arguments = actionstr.split("_");

        System.out.println("Action taken: "+ arguments[0]);


        /* * * start account for action specific changes * * */
        ActionParserHelper.ActionEnum actiontype = ActionParserHelper.GetActionType(action);
        switch (actiontype){
            case SCOUT:
                //TODO: consider giving the model accsess to the game for better time estimation
                //to ask what time it is?
                int newtimesincelastscout = (int) state.get("timeSinceLastScout");

                newtimesincelastscout += Math.round(rng.nextFloat() * 30 * 1000);
                baseNextState = new PlanningState( (int) state.get("numWorkers"), (int) state.get("mineralProductionRate"),
                        (int) state.get("gasProductionRate"), (int) state.get("numBases"), newtimesincelastscout,
                        (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                        (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                        (boolean) state.get("attackingEnemyBase"),(Race) state.get("playerRace"),(Race) state.get("enemyRace"),(GameStatus) state.get("gameStatus"),
                        capacity);

                break;
            case EXPAND:
                int numworkers = (int) state.get("numWorkers");
                int numBases = (int) state.get("numBases");
                int mineralproduction = (int) state.get("mineralProductionRate");
                numBases++;

                if(ourrace == Race.Zerg){
                    capacity[0][1] += 3;
                    capacity[1][1] += 3;
                    capacity[2][1] += 3;
                    capacity[3][1] += 3;

                    baseNextState = new PlanningState( numworkers-1, mineralproduction-57,
                            (int) state.get("gasProductionRate"), numBases, (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),ourrace,(Race) state.get("enemyRace"),(GameStatus) state.get("gameStatus"),
                            capacity);
                } else {
                    baseNextState = new PlanningState( numworkers, mineralproduction,
                            (int) state.get("gasProductionRate"), numBases, (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),(Race) state.get("playerRace"),(Race) state.get("enemyRace"),(GameStatus)state.get("gameStatus"),
                            capacity);
                }
                break;

            case BUILD:
                numworkers = (int) state.get("numWorkers");
                mineralproduction = (int) state.get("mineralProductionRate");

                //TODO: also consider the reduction in resource production temporarily
                if(ourrace == Race.Zerg){
                    baseNextState = new PlanningState( numworkers-1, mineralproduction-57,
                            (int) state.get("gasProductionRate"), (int) state.get("numBases"), (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),ourrace,(Race) state.get("enemyRace"),(GameStatus) state.get("gameStatus"),
                            capacity);
                } else {
                    baseNextState = new PlanningState( numworkers, mineralproduction,
                            (int) state.get("gasProductionRate"), (int) state.get("numBases"), (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),(Race) state.get("playerRace"),(Race) state.get("enemyRace"),(GameStatus)state.get("gameStatus"),
                            capacity);
                }
                break;

            case ATTACK:
                boolean attackingenemybase = true;
                for( int i  = 0; i < arguments.length; i++) {
                    if(arguments[i] != null && !arguments[i].isEmpty() && arguments[i].startsWith("what=")){
                        String target = arguments[i].substring("what=".length());
                        if(target.equals("army") || target.equals("defend")){
                            attackingenemybase = false;
                        }
                    }
                }

                baseNextState = new PlanningState( (int) state.get("numWorkers"), (int) state.get("mineralProductionRate"),
                        (int) state.get("gasProductionRate"), (int) state.get("numBases"), (int) state.get("timeSinceLastScout"),
                        (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                        (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                        attackingenemybase,(Race) state.get("playerRace"),(Race) state.get("enemyRace"),(GameStatus)state.get("gameStatus"),
                        capacity);
                break;

            //TODO: GET A MORE DETAILED LOOK AT THE TRAINING ARGUMENTS TO ADJUST, especially for protoss
            case TRAIN:

                if(ourrace == Race.Zerg){
                    capacity[0][0] += 1;
                    capacity[0][1] -= 1;

                    capacity[1][0] += 1;
                    capacity[1][1] -= 1;

                    capacity[2][0] += 1;
                    capacity[2][1] -= 1;

                    capacity[3][0] += 1;
                    capacity[3][1] -= 1;


                    baseNextState = new PlanningState( (int) state.get("numWorkers"), (int) state.get("mineralProductionRate"),
                            (int) state.get("gasProductionRate"), (int) state.get("numBases"), (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),ourrace,(Race) state.get("enemyRace"),(GameStatus) state.get("gameStatus"),
                            capacity);
                } else {
                    //if we can train combat units, and rng thiks we're training combat units
                    //say we are
                    if(capacity[1][1] > 0 && rng.nextBoolean()){
                        capacity[1][0] += 1;
                        capacity[1][1] -= 1;
                    } else {
                        capacity[0][0] += 1;
                        capacity[0][1] -= 1;
                    }

                    baseNextState = new PlanningState( (int) state.get("numWorkers"), (int) state.get("mineralProductionRate"),
                            (int) state.get("gasProductionRate"), (int) state.get("numBases"), (int) state.get("timeSinceLastScout"),
                            (ArrayList<CombatUnitStatus>)state.get("combatUnitStatuses"), (int) state.get("numEnemyWorkers"),
                            (int)state.get("numEnemyBases"), (Unit)state.get("mostCommonCombatUnit"),
                            (boolean) state.get("attackingEnemyBase"),(Race) state.get("playerRace"),(Race) state.get("enemyRace"),(GameStatus)state.get("gameStatus"),
                            capacity);
                }
                break;

            //TODO: probably take a more detailed look at these
            case UPGRADE:
                baseNextState = state.copy();
                break;
            case UNKNOWN:
                baseNextState = state.copy();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actiontype);
        }
        /* * * end account for action specific changes * * */




        List<TransitionProb> AllProbabilities = new ArrayList<TransitionProb>();

        //possibility of being attacked
        if(! ((boolean) state.get("attackingEnemyBase"))) {
            //TODO: Make this probability more reasonable. Check estimated # oponent units + game stage.
            TotalProbablity -= 0.05 * TotalProbablity;
            AllProbabilities.addAll(attackedTransitions(state, action, baseNextState, 0.05));
        }



        return AllProbabilities;
    }


    /**
     * Helper function to provide possible states if attacked.
     * @param Currentstate
     * @param action
     * @param nextState
     * @param probability
     * @return
     */
    private List<TransitionProb> attackedTransitions(State Currentstate, Action action, State nextState, double probability) {
        List<TransitionProb> probabilities = new ArrayList<TransitionProb>();

        //possibility of being attacked


        return probabilities;
    }



    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        return null;
    }

    @Override
    public boolean terminal(State state) {
        return false;
    }
}
