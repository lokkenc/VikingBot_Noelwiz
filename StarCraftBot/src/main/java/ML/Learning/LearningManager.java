package ML.Learning;

import ML.Actions.Action;
import ML.Data.DataManager;
import bwapi.UnitType;
import ML.States.*;

import java.io.File;
import java.io.Serializable;

public class LearningManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private UnitType type;
    private StateSpaceManager spaceManager;
    private SARSA sarsa;
    private GreedyActionChooser greedyActionChooser;

    public LearningManager(UnitType type) {
        this.type = type;
        spaceManager = new StateSpaceManager();
        sarsa = new SARSA(type, spaceManager);
        greedyActionChooser = new GreedyActionChooser(sarsa.getQTable());
    }

    public void loadQTable() {
        File f = new File("TrainingFiles/Tables/" + type.toString() + "Table.ser");
        if(f.exists()) {
            sarsa.loadQTable();
        }
    }

    public void storeQTable() {
        sarsa.storeQTable();
    }

    public void loadDataManager() {
        File f = new File("TrainingFiles/Tables/" + type.toString() + "Data.ser");
        if(f.exists()) {
            sarsa.loadDataManager();
        }
    }

    public void storeDataManager() {
        sarsa.storeDataManager();
    }

    public void updateState(State current, Action action, State next) {
        sarsa.updateQTable(current, action, next);
    }

    public Action getNextAction(State state) {
        return greedyActionChooser.chooseAction(spaceManager, state);
    }

    public UnitType getUnitType() {
        return this.type;
    }

    public DataManager getDataManager() {
        return sarsa.getDataManager();
    }
}
