package src.main.java.ML;

import bwapi.*;
import src.main.java.CombatAgent;
import src.main.java.IntelligenceAgent;

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
        intel = new IntelligenceAgent(self);
        combat = new CombatAgent(intel);
        
        combat.addUnitTypeToModel(UnitType.Protoss_Zealot);
        combat.loadModels();
    }

    @Override
    public void onFrame() {
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        game.drawTextScreen(10, 230, "Resources: " + self.minerals() + " minerals,  " + self.gas() + " gas, " + (self.supplyUsed() / 2) + "/" + (self.supplyTotal() / 2) + " psi");

        intel.tabulateUnits(self);
        frameCount++;
        if(frameCount % ML_Epoch == 0) {
            frameCount = 0;
            combat.controlArmy(game, intel.getUnitsListOfType(self, UnitType.Protoss_Zealot));
        }
    }

    @Override
    public void onEnd(boolean isWinner) {
        combat.storeModels();
    }

    public static void main(String[] args) {
        new src.main.java.ML.Training().run();
    }
}
