package Planning;

import Planning.Actions.ActionParserHelper;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.TransitionProb;
import bwapi.Race;
import bwapi.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarcraftModel implements FullModel {
    Random rng = new Random();
    RewardFunction rewardFunction = null;

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
        State baseNextState;
        List<TransitionProb> AllProbabilities = new ArrayList<TransitionProb>();
        //TODO: make the baseNextState a list of states because there could be multiple possible states
        //example: if we scout while the enemy race is unknown, 1/3 chance for the enemy to be
        //a given race, maybe 1% we fail to do anything.


        int[][] capacity = (int[][]) state.get("gameStatus");
        Race ourrace = (Race) state.get("playerRace");


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

        EnvironmentOutcome defaultoutcome = new EnvironmentOutcome(state, action,baseNextState, rewardFunction.reward(state,action,baseNextState),false);
        AllProbabilities.add(new TransitionProb(1, defaultoutcome));
        /* * * end account for action specific changes * * */

        /* * * start account for general changes * * */
        /* general notes: works by looping through current possibilities
         * and adding new ones based on them. So, the most effecient way is
         * to start with the things that add the least number of new options
         * and end with the ones that add the most.
         */

        //possibility of being attacked
        if(! ((boolean) state.get("attackingEnemyBase"))) {
            //TODO: Make this probability more reasonable. Check estimated # oponent units + game stage.
            AllProbabilities.addAll(attackedTransitions(state, action, AllProbabilities));
        }

        //distro of likelyhood of a new enemy base

        //distribution of enemy workers likely to currently be owned


        //posibility of units finishing training

        /* * * end account for general changes * * */

        return AllProbabilities;
    }


    /**
     * Helper function to provide possible states if attacked.
     * @param Currentstate
     * @param action

     * @param allProbabilities
     * @return
     */
    private List<TransitionProb> attackedTransitions(State Currentstate, Action action, List<TransitionProb> allProbabilities) {
        double attackprob = 0.05;
        double noattackprob = 1-attackprob;
        TransitionProb currentprob;
        State alternateState;

        List<TransitionProb> newProbabilities = new ArrayList<TransitionProb>();

        for(int i = 0; i < allProbabilities.size(); i++){
            currentprob = allProbabilities.get(i);
            currentprob.p = currentprob.p * noattackprob;

            alternateState = new PlanningState( (int) currentprob.eo.op.get("numWorkers"), (int) currentprob.eo.op.get("mineralProductionRate"),
                    (int) currentprob.eo.op.get("gasProductionRate"), (int) currentprob.eo.op.get("numBases"), (int) currentprob.eo.op.get("timeSinceLastScout"),
                    (ArrayList<CombatUnitStatus>)currentprob.eo.op.get("combatUnitStatuses"), (int) currentprob.eo.op.get("numEnemyWorkers"),
                    (int)currentprob.eo.op.get("numEnemyBases"), (Unit)currentprob.eo.op.get("mostCommonCombatUnit"),
                    !(boolean) currentprob.eo.op.get("attackingEnemyBase"),(Race) currentprob.eo.op.get("playerRace"),(Race) currentprob.eo.op.get("enemyRace"),(GameStatus)currentprob.eo.op.get("gameStatus"),
                    (int[][])currentprob.eo.op.get("trainingCapacity"));

            newProbabilities.add(new TransitionProb(attackprob * currentprob.p, new EnvironmentOutcome(Currentstate, action,alternateState, rewardFunction.reward(Currentstate,action,alternateState),false)));
        }

        return newProbabilities;
    }







    private  List<TransitionProb> trainingCompleteProbabilities(State Currentstate, Action action, List<TransitionProb> allProbabilities){
        List<TransitionProb> probabilities = new ArrayList<TransitionProb>();
        State alternateState;
        Race ourrace = (Race) Currentstate.get("playerRace");

        TransitionProb currentprob;
        List<CapacityProbibilityPair> capacities;


        CapacityProbibilityPair currentCapProbPair;

        double remainingprobability = 1;

        for(int i = 0; i < allProbabilities.size(); i++){
            currentprob = allProbabilities.get(i);
            List<CapacityProbibilityPair> currentPosibleCapacities = PosibleCapacities(ourrace, currentprob.eo.op);

            for( int j = 0; j < currentPosibleCapacities.size(); j++){
                currentCapProbPair = currentPosibleCapacities.get(j);
                remainingprobability -= currentCapProbPair.prob;

                alternateState = new PlanningState( (int) currentprob.eo.op.get("numWorkers"), (int) currentprob.eo.op.get("mineralProductionRate"),
                        (int) currentprob.eo.op.get("gasProductionRate"), (int) currentprob.eo.op.get("numBases"), (int) currentprob.eo.op.get("timeSinceLastScout"),
                        (ArrayList<CombatUnitStatus>)currentprob.eo.op.get("combatUnitStatuses"), (int) currentprob.eo.op.get("numEnemyWorkers"),
                        (int)currentprob.eo.op.get("numEnemyBases"), (Unit)currentprob.eo.op.get("mostCommonCombatUnit"),
                        (boolean) currentprob.eo.op.get("attackingEnemyBase"),(Race) currentprob.eo.op.get("playerRace"),(Race) currentprob.eo.op.get("enemyRace"),(GameStatus)currentprob.eo.op.get("gameStatus"),
                        (int[][])currentprob.eo.op.get("trainingCapacity"));

                //TODO: FIX THIS
                probabilities.add(new TransitionProb(currentCapProbPair.prob * currentprob.p, new EnvironmentOutcome(Currentstate, action,alternateState, rewardFunction.reward(Currentstate,action,alternateState),false)));
            }

            //should be equal to p * (1 - AverageUnitTrainingTime/1800)
            //I think, accept that's probably wrong
            currentprob.p = currentprob.p * remainingprobability;
            if(remainingprobability < 0){
                System.err.println("ERROR: NEGATIVE PROBABILITY IS NOT REAL MATH.");
                System.err.println("Daniel messed up the probability distribution for the chance " +
                        "of units finishing training. someone should come into this code at" +
                        "the end of the traniningCompleteProbabilities function in StarcraftModel.java");
            }
        }



        return probabilities;
    }


    //TODO: this is missing more than a few possibilites, from different combinations of units finishing
    //training, and not finishing training.
    private List<CapacityProbibilityPair> PosibleCapacities(Race ourrace, State possibleState){
        //deviding by 1800 to make this the chance of the unit finishing
        //at a given time in a single minute.
        //TODO: someone who's smart with probability should adjust this chance here, and for protoss below
        double finishprob = AverageUnitTrainingTime/1800;
        double notfinishprob = 1 - finishprob;
        double probabilityNotAccountedFor = finishprob;
        double currentSituationProbab;
        int[][] capacity = (int[][]) possibleState.get("gameStatus");

        List<CapacityProbibilityPair> possiblecapacities = new ArrayList<CapacityProbibilityPair>();


        if(ourrace==Race.Zerg){
            //if there's something training, flip a coin to see if it completes
            if(capacity[0][0] > 0){
                for(int unit = 0; unit<capacity[0][0]; unit++){
                    capacity[0][0] -= 1;
                    capacity[0][1] += 1;
                    capacity[1][0] -= 1;
                    capacity[1][1] += 1;
                    capacity[2][0] -= 1;
                    capacity[2][1] += 1;
                    capacity[3][0] -= 1;
                    capacity[3][1] += 1;

                    currentSituationProbab = Math.pow(finishprob, unit+1);
                    probabilityNotAccountedFor -= currentSituationProbab;
                    //TODO: figure out how to make the probability correct
                    //this adds the probability that all the units finished training so far.
                    possiblecapacities.add( new CapacityProbibilityPair(capacity, currentSituationProbab) );
                }
            }

        } else {
            int power = 1;
            //TODO: THIS!
            for(int catagory = 0; catagory<capacity.length; catagory++){
                if(capacity[catagory][0] > 0){
                    for(int tainingcapacityslot = 0; tainingcapacityslot< capacity[catagory][0];tainingcapacityslot++){

                        capacity[catagory][0] -= 1;
                        capacity[catagory][1] += 1;

                        power++;
                        currentSituationProbab = Math.pow(finishprob, power);
                        probabilityNotAccountedFor -= currentSituationProbab;
                        possiblecapacities.add( new CapacityProbibilityPair(capacity, currentSituationProbab) );
                    }
                }
            }
        }

        return possiblecapacities;
    }


    @Override
    public EnvironmentOutcome sample(State state, Action action) {
        return null;
    }

    @Override
    public boolean terminal(State state) {
        return false;
    }


    private static class CapacityProbibilityPair{
        int[][] cap;
        double prob;

        CapacityProbibilityPair(int[][] capacity, double probability){
            cap=capacity;
            prob=probability;
        }
    }
}



