package listener;

import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Player;

/**
 * Provides the bare minimum to define a BWListener.
 */
public abstract class GameListener extends DefaultBWListener {
    private ListenerType type;
    public BWClient bwClient;

    /**
     * Sets the ListenerType of the GameListener, creates a new instance of a BWClient, and starts the game.
     * @param type
     */
    public GameListener(ListenerType type) {
        this.type = type;
        this.bwClient = new BWClient(this);
        this.bwClient.startGame();
    }

    /**
     * The BWListener's onStart method to override with anything that should execute at the start of the game.
     */
    @Override
    public abstract void onStart();

    /**
     * The BWListener's onEnd method to override with anything that should execute at the end of the game.
     * @param isWinner is true if the bot won the game.
     */
    @Override
    public abstract void onEnd(boolean isWinner);

    /**
     * The BWListener's onFrame method to override with anything that should execute every time there's a new frame.
     */
    @Override
    public abstract void onFrame();

    /**
     * Get's the ListenerType of the GameListener.
     * @return The GameListener's ListenerType.
     */
    public ListenerType getType() {
        return this.type;
    }
}
