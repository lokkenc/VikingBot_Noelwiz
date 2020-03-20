package Planning;

import Planning.Actions.QueueComparator;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.RewardFunction;
import bwapi.Race;
import src.main.java.IntelligenceAgent;

import java.util.PriorityQueue;

public class StarcraftEnvironment implements Environment {
    private Race PlayerRace;
    private Race EnemyRace;
    private RewardFunction rewardFunction;
    private double PreviousReward = 0;
    private IntelligenceAgent intelligenceAgent = null;
    private PriorityQueue<Action> ActionQueue;


    /**
     * Constructer for the enviorment. Takes a reward function to
     * use for calulating rewards.
     * @param rf
     */
    public StarcraftEnvironment(RewardFunction rf, IntelligenceAgent intelligenceAgent){
        this.intelligenceAgent = intelligenceAgent;
        rewardFunction = rf;
        ActionQueue = new PriorityQueue<Action>(new QueueComparator());
    }

    /**
     * Change the reward function used.
     * @param rf
     */
    public void UpdateRewardFunction(RewardFunction rf){
        rewardFunction = rf;
    }


    /**
     * Calls some unknown functions in the IntelligenceAgent to figure
     * out the current state of the game.
     * @return
     */
    public State currentObservation() {
        int numworkers = intelligenceAgent.getNumWorkers();
        int mineralProductionRate = Math.round(intelligenceAgent.getMineralProductionRate());
        int gasProductionRate = Math.round(intelligenceAgent.getGasProductionRate());
        int numBases = intelligenceAgent.getNumBases();
        int timeSinceLastScout = intelligenceAgent.getTimeSinceLastScout();
        return null;
    }

    /**
     * Intercepts the action given by executeAction(Action) and sends it to the bot to be done.
     * @param action
     * @return
     */
    public EnvironmentOutcome executeAction(Action action) {
        //TODO: interperate actions based on their name.
        String actionName = action.actionName();


        //TODO: SEND COMMANDS TO BOT BASED ON THE ACTION.

        //observe results
        State currentstate = currentObservation();
        State resultingstate = predictState(action);
        PreviousReward = rewardFunction.reward(currentstate,action,resultingstate);
        return new EnvironmentOutcome(currentstate, action, /*resulting state*/
                resultingstate,  /*reward*/ PreviousReward, isTerminalState(resultingstate));
    }

    public double lastReward() {
        return PreviousReward;
    }

    /**
     * Returns whether the environment is in a terminal state that prevents further action by the agent.
     *
     * @return true if the current environment is in a terminal state; false otherwise.
     */
    @Override
    public boolean isInTerminalState() {
        return isTerminalState(currentObservation());
    }

    /**
     * Check if we have no town centers, or if the
     * enemy has no town centers, or if the game is done.
     * @return
     */
    public boolean isTerminalState(State s) {
        boolean terminal = false;
        if((int) s.get("numBases") < 1 || (int) s.get("numEnemyBases") < 1){
            terminal = true;
        }
        return terminal;
    }


    /**
     * Bot conceads, the game ends, and then
     * maybe a new game is started.
     */
    public void resetEnvironment() {

    }

    /**
     * TODO: predict the resulting state based on an action.
     * @param action the action taken by the ai
     * */
    private State predictState(Action action){
        return null;
    }
}
