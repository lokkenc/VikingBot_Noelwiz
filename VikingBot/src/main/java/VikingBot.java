import listener.GameListener;
import listener.ListenerType;
import listeners.MLTrainingListener;

/**
 * The main instance of the VikingBot which will handle setting up the agents between games.
 */
public enum VikingBot {
    INSTANCE;

    public static final String NAME = "VikingBot";
    public static final String VERSION = "1.0.0";
    public static final String SC_VERSION = "1.16.1";

    private GameListener listener;

    /**
     * Initializes the listener to be used based on the specified ListenerType.
     * @param type the type of GameListener for handling in-game events.
     */
    public void initialize(ListenerType type) {
        if(type == ListenerType.TRAINING) {
            listener = new MLTrainingListener(type);
        }
        else {
            listener = new VikingBotListener(type);
        }
    }

    /**
     * Get's the current GameListener being used to handle in-game events.
     * @return The GameListener being used by the VikingBot instance.
     */
    public GameListener getListener() {
        return listener;
    }
}
