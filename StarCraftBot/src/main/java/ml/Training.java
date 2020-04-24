package ml;

import agents.CombatAgent;
import agents.IntelligenceAgent;
import ml.actions.ActionType;
import ml.data.DataManager;
import ml.model.UnitClassification;
import bwapi.*;

import java.util.ArrayList;

/**
 * This class is a skeleton of StrategyAgent that allows the ML model to train separately from the AI planning
 */
public class Training extends DefaultBWListener{

    private BWClient bwClient;
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private CombatAgent combat;

    private int ML_Epoch = 14;
    private int frameCount = 0;
    private int Epoch_Cycles = 0;
    private boolean skirmVal = false;

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
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

        if(frameCount % ML_Epoch == 0) {
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

    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    public static void main(String[] args) {
        new ml.Training().run();
    }
}
