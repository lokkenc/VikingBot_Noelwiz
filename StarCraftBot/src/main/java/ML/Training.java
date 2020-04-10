package ML;

import Agents.CombatAgent;
import Agents.IntelligenceAgent;
import ML.Data.DataManager;
import bwapi.*;

import java.util.ArrayList;

public class Training extends DefaultBWListener{

    private BWClient bwClient;
    private Game game;
    private Player self;

    private IntelligenceAgent intel;
    private CombatAgent combat;

    private int ML_Epoch = 14;
    private int frameCount = 0;

    public void run() {
        bwClient = new BWClient(this);
        bwClient.startGame();
    }

    @Override
    public void onStart() {
        game = bwClient.getGame();
        self = game.self();
        intel = new IntelligenceAgent(self, game);
        combat = new CombatAgent(intel);
        
        combat.addUnitTypeToModel(UnitType.Protoss_Zealot);
        combat.loadModels();
    }

    @Override
    public void onFrame() {
        DataManager dm = combat.getDataManagers().get(0);
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        if(dm != null) {
            game.drawTextScreen(10, 50, "Type: " + dm.getType());
            game.drawTextScreen(10, 60, "Data Points: " + dm.getDataPoints().size());
            game.drawTextScreen(10, 70, "Unique States: " + dm.getStateFrequency().keySet().size());
        }
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);
        frameCount++;
        if(frameCount % ML_Epoch == 0) {
            frameCount = 0;
            combat.controlArmy(game, new ArrayList<>(intel.getUnitsListOfType(UnitType.Protoss_Zealot)));
        }
    }

    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    public static void main(String[] args) {
        new ML.Training().run();
    }
}
