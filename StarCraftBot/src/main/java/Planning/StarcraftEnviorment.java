package Planning;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.Environment; //burlap.mdp.singleagent.environment
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import bwapi.Race;

public class StarcraftEnviorment implements Environment {

    private Race PlayerRace;
    private Race EnemyRace;

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
        //TODO: SEND COMMANDS TO BOT BASED ON THE ACTION.
        return null;
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
}
