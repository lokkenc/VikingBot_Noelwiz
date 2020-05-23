import agents.CombatAgent;
import agents.EconomyAgent;
import agents.IntelligenceAgent;
import agents.StrategyAgent;
import bwapi.*;
import bwta.BWTA;
import listener.GameListener;
import listener.ListenerType;

/**
 * The main GameListener used for initializing the agents and updating them every frame.
 */
public class VikingBotListener extends GameListener {
    private Game game;
    private Player self;

    private CombatAgent combat;
    private EconomyAgent economy;
    private IntelligenceAgent intel;
    private StrategyAgent strategy;

    /**
     * Creates a new VikingBotListener given a specified ListenerType.
     * @param type the type of GameListener to be used.
     */
    public VikingBotListener(ListenerType type) {
        super(type);
    }

    /**
     * Analyzes the map, initializes the agents, and prepares the agents to start a new game.
     */
    @Override
    public void onStart() {
        // Initialize the game
        game = bwClient.getGame();
        self = game.self();

        // Analyze the map
        System.out.println("Analyzing map...");
        BWTA.readMap(game);
        BWTA.analyze();
        System.out.println("Map data ready");

        // Initialize the Agents
        intel = IntelligenceAgent.getInstance(game);
        intel.tabulateUnits(self);
        combat = CombatAgent.getInstance(game);
        economy = new EconomyAgent(game);
        strategy = new StrategyAgent(game);

        // Prepare the Agents
        combat.addUnitTypeToModel(combat.getUnitClassification(UnitType.Protoss_Zealot));
        combat.loadModels();
    }

    /**
     * Stores the machine learning combat models at the end of the game.
     * @param isWinner a boolean which is true if the bot won the game.
     */
    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    /**
     * Renders general information on screen and updates the agents.
     */
    @Override
    public void onFrame() {
        // Display information on screen
        game.drawTextScreen(10, 10, VikingBot.NAME + "v" + VikingBot.VERSION + " SCv" + VikingBot.SC_VERSION);

        // Update the Agents
        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory();

        strategy.update();
    }

    /**
     * Updates the IntelligenceAgent whenever a unit is destroyed.
     * @param unit the Unit that is destroyed.
     */
    @Override
    public void onUnitDestroy(Unit unit) {
        intel.onUnitDestroy(unit);
    }

    /**
     * Updates the IntelligenceAgent whenever a new unit comes into vision.
     * @param unit the Unit that comes into vision.
     */
    @Override
    public void onUnitShow(Unit unit) {
        intel.onUnitShow(unit);

        //could send the scout back once we show the enemy base
    }

    @Override
    public void onNukeDetect(Position position) {
        super.onNukeDetect(position);
        game.drawTextScreen(100,100,"nuke detected, no response programmed.");
    }


    @Override
    public void onUnitComplete(Unit unit) {
        super.onUnitComplete(unit);
        strategy.useCompletedUnit(unit);
    }


    /**
     * Draws various information about the bot on screen.
     */
    private void renderStats() {

    }
}
