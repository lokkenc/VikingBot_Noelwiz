import listener.ListenerType;

/**
 * Parses command line arguments and initializes the main VikingBot instance.
 */
public class VikingBotInitializer {
    private static boolean initialized;

    /**
     * Main method for parsing command line arguments and initializing the bot with the specified ListenerType.
     * @param args command line arguments to be parsed.
     */
    public static void main(String[] args) {
        ListenerType type = ListenerType.NORMAL;

        if(args != null) {
            for(String arg : args) {
                if(arg.equalsIgnoreCase(ListenerType.TRAINING.getName())) {
                    type = ListenerType.TRAINING;
                    break;
                }
            }
        }

        if(initialized) {
            throw new RuntimeException("VikingBotInitializer.initialize() ran twice!");
        }

        initialize(type);
    }

    /**
     * Initializes the main VikingBot instance with the specified ListenerType.
     * @param type
     */
    private static void initialize(ListenerType type) {
        VikingBot.INSTANCE.initialize(type);
        initialized = true;
    }
}
