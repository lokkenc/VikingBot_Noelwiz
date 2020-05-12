package listeners;

import agents.CombatAgent;
import agents.IntelligenceAgent;
import listener.GameListener;
import listener.ListenerType;
import ml.actions.ActionType;
import ml.data.DataManager;
import ml.model.UnitClassification;
import bwapi.*;

import java.util.ArrayList;

/**
 * This class is a skeleton of VikingBotListener that allows the ML model to train separately from the AI planning
 */
public class MLTrainingListener extends GameListener {
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private CombatAgent combat;

    private int epoch = 14;
    private int frameCount = 0;
    private int Epoch_Cycles = 0;
    private boolean skirmVal = false;

    /**
     * Creates a new MlTrainingListener with the specified ListenerType.
     * @param type the ListenerType used to create the listener.
     */
    public MLTrainingListener(ListenerType type) {
        super(type);
    }

    /**
     * Prepares the minimum number of agents needed to train the CombatAgent (IntelligenceAgent and CombatAgent).
     */
    @Override
    public void onStart() {
        game = bwClient.getGame();
        self = game.self();
        intel = IntelligenceAgent.getInstance(game);
        combat = CombatAgent.getInstance(game);
        
        combat.addUnitTypeToModel(UnitClassification.MELEE);
        combat.addUnitTypeToModel(UnitClassification.RANGED);
        combat.loadModels();
        combat.setSkirmish(true);

        epoch = 14;
        frameCount = 0;
        Epoch_Cycles = 0;
        skirmVal = false;
    }

    /**
     * Stores the machine learning models at the end of the game.
     * @param isWinner is true if the bot won the game.
     */
    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    /**
     * Updates the IntelligenceAgent, draws stats on screen, and has the CombatAgent train.
     */
    @Override
    public void onFrame() {
        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory(game);

        DataManager dm = combat.getDataManagers().get(0);
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        if(dm != null) {
            game.drawTextScreen(10, 50, "Type: " + dm.getClassification());
            game.drawTextScreen(10, 60, "Skirmish: " + !skirmVal);
            game.drawTextScreen(10, 70, "Data Points: " + dm.getDataPoints().size());
            game.drawTextScreen(10, 80, "Unique States: " + dm.getStateFrequency().keySet().size());
            game.drawTextScreen(10, 90, "Average Reward: " + dm.getAverageReward());
            game.drawTextScreen(10, 100, "Avg. Attack Reward: " + dm.getAverageReward(ActionType.valueOf("ATTACK")));
            game.drawTextScreen(10, 110, "Avg. GoHome Reward: " + dm.getAverageReward(ActionType.valueOf("GOHOME")));
            game.drawTextScreen(10, 120, "Avg. MoveTowards Reward: " + dm.getAverageReward(ActionType.valueOf("MOVETOWARDS")));
            game.drawTextScreen(10, 130, "Avg. Retreat Reward: " + dm.getAverageReward(ActionType.valueOf("RETREAT")));
        }
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);

        if(frameCount % epoch == 0) {
            frameCount = 0;
            combat.controlArmy(game, new ArrayList<>(intel.getUnitsListOfType(UnitType.Protoss_Zealot)));
            Epoch_Cycles++;
        }
        if(Epoch_Cycles % 400 == 0) {
            Epoch_Cycles = 1;
            combat.setSkirmish(skirmVal);
            skirmVal = !skirmVal;
        }
        frameCount++;
    }

}
