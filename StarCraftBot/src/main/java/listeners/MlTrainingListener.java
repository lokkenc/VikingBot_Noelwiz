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
import java.util.List;

/**
 * This class is a skeleton of StrategyAgent that allows the ML model to train separately from the AI planning
 */
public class MlTrainingListener extends GameListener {
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private CombatAgent combat;

    private int ML_Epoch = 14;
    private int frameCount = 0;
    private int Epoch_Cycles = 0;

    public MlTrainingListener(ListenerType type) {
        super(type);
    }

    @Override
    public void onStart() {
        game = bwClient.getGame();
        self = game.self();
        intel = IntelligenceAgent.getInstance(game);
        combat = new CombatAgent(intel);
        
        combat.addUnitTypeToModel(UnitClassification.MELEE);
        combat.addUnitTypeToModel(UnitClassification.RANGED);
        combat.loadModels();
        combat.setSkirmish(true);
    }

    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    @Override
    public void onFrame() {
        intel.tabulateUnits(self);
        intel.updateEnemyBuildingMemory(game);

        DataManager dm = combat.getDataManagers().get(0);
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        if(dm != null) {
            game.drawTextScreen(10, 50, "Type: " + dm.getClassification());
            game.drawTextScreen(10, 60, "Data Points: " + dm.getDataPoints().size());
            game.drawTextScreen(10, 70, "Unique States: " + dm.getStateFrequency().keySet().size());
            game.drawTextScreen(10, 80, "Average Reward: " + dm.getAverageReward());
            game.drawTextScreen(10, 90, "Avg. Attack Reward: " + dm.getAverageReward(ActionType.valueOf("Attack")));
            game.drawTextScreen(10, 100, "Avg. GoHome Reward: " + dm.getAverageReward(ActionType.valueOf("GoHome")));
            game.drawTextScreen(10, 110, "Avg. MoveTowards Reward: " + dm.getAverageReward(ActionType.valueOf("MoveTowards")));
            game.drawTextScreen(10, 120, "Avg. Retreat Reward: " + dm.getAverageReward(ActionType.valueOf("Retreat")));
        }
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);
        if(frameCount % ML_Epoch == 0) {
            frameCount = 0;
            combat.controlArmy(game, new ArrayList<>(intel.getUnitsListOfType(UnitType.Protoss_Zealot)));
        }
        frameCount++;
    }
}
