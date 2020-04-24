import listener.GameListener;
import listener.ListenerType;
import listeners.MlTrainingListener;

public enum StarCraftBot {
    INSTANCE;

    public static final String NAME = "StarCraftBot";
    public static final String VERSION = "1.0.0";
    public static final String SC_VERSION = "1.16.1";

    private GameListener listener;

    public void initialize(ListenerType type) {
        if(type == ListenerType.TRAINING) {
            listener = new MlTrainingListener(type);
        }
        else {
            listener = new StarCraftBotListener(type);
        }
    }

    public GameListener getListener() {
        return listener;
    }
}