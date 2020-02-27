package Planning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.FullModel;
import burlap.mdp.singleagent.model.TransitionProb;

import java.util.ArrayList;
import java.util.List;

public class StarcraftModel implements FullModel {

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
        //TODO: make a state with what we know will happen
        //example, if we build a worker, there will be a worker training right now.
        String actionstr = action.actionName();
        String[] arguments = actionstr.split("_");

        //TODO: Make sure the first element isn't the empty string if the action has not "_" in it (aka has no args)
        System.out.println("Action taken: "+ arguments[0]);

        switch(arguments[1]){
            default:
                baseNextState = state.copy();
                break;
        }

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
