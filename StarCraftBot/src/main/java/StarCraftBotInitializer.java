import listener.ListenerType;

/**
 * Parses command line arguments and initializes the main StarCraftBot instance.
 */
public class StarCraftBotInitializer {
    private static boolean initialized;

    /**
     * Main method for parsing command line arguments and initializing the bot with the specified ListenerType.
     * @param args command line arguments to be parsed.
     */
    public static void main(String[] args) {
        ListenerType type = ListenerType.NORMAL;

        if(args != null) {
            for(String string : args) {
                if(string.equalsIgnoreCase(ListenerType.TRAINING.getName())) {
                    type = ListenerType.TRAINING;
                    break;
                }
            }
        }

        /*
         * TODO: Process args to get change modes (train, play, etc.)
         */
        if(initialized) {
            throw new RuntimeException("StarCraftBotInitializer.initialize() ran twice!");
        }

        initialize(type);
    }

    /**
     * Initializes the main StarCraftBot instance with the specified ListenerType.
     * @param type
     */
    private static void initialize(ListenerType type) {
        StarCraftBot.INSTANCE.initialize(type);
        initialized = true;
    }
}
