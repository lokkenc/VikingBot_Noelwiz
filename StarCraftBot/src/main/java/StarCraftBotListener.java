import agents.CombatAgent;
import agents.EconomyAgent;
import agents.IntelligenceAgent;
import agents.StrategyAgent;
import bwapi.*;
import bwta.BWTA;
import listener.GameListener;
import listener.ListenerType;

public class StarCraftBotListener extends GameListener {
    private Game game;
    private Player self;

    private CombatAgent combat;
    private EconomyAgent economy;
    private IntelligenceAgent intel;
    private StrategyAgent strategy;

    public StarCraftBotListener(ListenerType type) {
        super(type);
    }

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
        combat = new CombatAgent(intel);
        economy = new EconomyAgent(intel);
        strategy = new StrategyAgent(game, intel);

        // Prepare the Agents
        combat.addUnitTypeToModel(combat.getUnitClassification(UnitType.Protoss_Zealot));
        combat.loadModels();
    }

    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    @Override
    public void onFrame() {
        // Display information on screen
        game.drawTextScreen(10, 10, StarCraftBot.NAME + "v" + StarCraftBot.VERSION + " SCv" + StarCraftBot.SC_VERSION);

        // Update the Agents
        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory(game);

        strategy.update();
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        intel.onUnitDestroy(unit);
    }

    @Override
    public void onUnitShow(Unit unit) {
        intel.onUnitShow(unit);

        //could send the scout back once we show the enemy base
    }

    private void renderStats() {

    }
}
