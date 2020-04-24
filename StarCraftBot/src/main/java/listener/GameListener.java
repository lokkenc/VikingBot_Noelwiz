package listener;

import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Player;

public abstract class GameListener extends DefaultBWListener {
    private ListenerType type;
    public BWClient bwClient;

    public GameListener(ListenerType type) {
        this.type = type;
        this.bwClient = new BWClient(this);
        this.bwClient.startGame();
    }

    @Override
    public abstract void onStart();

    @Override
    public abstract void onEnd(boolean isWinner);

    @Override
    public abstract void onFrame();

    public ListenerType getType() {
        return this.type;
    }
}
