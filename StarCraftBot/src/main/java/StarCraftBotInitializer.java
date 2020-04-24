import listener.ListenerType;

public class StarCraftBotInitializer {
    private static boolean initialized;

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

    private static void initialize(ListenerType type) {
        StarCraftBot.INSTANCE.initialize(type);
        initialized = true;
    }
}
