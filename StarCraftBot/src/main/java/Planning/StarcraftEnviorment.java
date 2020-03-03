package Planning;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment; //burlap.mdp.singleagent.environment
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import bwapi.Race;

import java.util.PriorityQueue;

public class StarcraftEnviorment implements Environment {
    private Race PlayerRace;
    private Race EnemyRace;

    //TODO: QUEUE FOR ACTIONS. ... maybe.
    //Would add an action to the queue in executeAction,
    //then the bot would use the queue to decide what to do
    //once a previous action was finished or something
    private PriorityQueue<Action> ActionQueue;

    /**
     * Calls some unknown functions in the IntelligenceAgent to figure
     * out the current state of the game.
     * @return
     */
    public State currentObservation() {
        return null;
    }

    /**
     * Intercepts the action given by executeAction(Action) and sends it to the bot to be done.
     *
     * TODO: probably will then delay outcome until some time, and then send
     * back enviorment reward
     * @param action
     * @return
     */
    public EnvironmentOutcome executeAction(Action action) {
        //TODO: interperate actions based on their name.
        String actionName = action.actionName();

        //TODO: SEND COMMANDS TO BOT BASED ON THE ACTION.
        return new EnvironmentOutcome(currentObservation(), action, /*resulting state*/
                predictState(action),  /*reward*/ 0, false);
    }

    public double lastReward() {
        return 0;
    }

    /**
     * Check if we have no town centers, or if the
     * enemy has no town centers, or if the game is done.
     * @return
     */
    public boolean isInTerminalState() {
        return false;
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
