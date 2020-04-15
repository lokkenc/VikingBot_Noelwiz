package ML.Learning;

import ML.Actions.Action;
import ML.Data.DataManager;
import bwapi.UnitType;
import ML.States.*;

import java.io.File;
import java.io.Serializable;

public class LearningManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UnitType type;
    private final StateSpaceManager spaceManager;
    private final SARSA sarsa;
    private final GreedyActionChooser greedyActionChooser;

    /**
     * Initialize the LearningManager, set the type of unit it's managing, create the state space, create the learning
     * algorithm, and create the GreedyActionChooser.
     * @param type the type of unit the LearningManager will be controlling.
     */
    public LearningManager(UnitType type) {
        this.type = type;
        spaceManager = new StateSpaceManager();
        sarsa = new SARSA(type, spaceManager);
        greedyActionChooser = new GreedyActionChooser(sarsa.getQTable());
    }

    /**
     * Load the QTable from disk if it exists.
     */
    public void loadQTable() {
        File f = new File("TrainingFiles/Tables/" + type.toString() + "Table.ser");
        if(f.exists()) {
            sarsa.loadQTable();
        }
    }

    /**
     * Store the QTable to disk.
     */
    public void storeQTable() {
        sarsa.storeQTable();
    }

    /**
     * Load the DataManager from disk if it exists.
     */
    public void loadDataManager() {
        File f = new File("TrainingFiles/Tables/" + type.toString() + "Data.ser");
        if(f.exists()) {
            sarsa.loadDataManager();
        }
    }

    /**
     * Store the DataManager.
     */
    public void storeDataManager() {
        sarsa.storeDataManager();
    }

    /**
     * Update the learning algorithm with the current State, the Action executed, and the resulting next State.
     * @param current the current State.
     * @param action the Action executed in the current State.
     * @param next the resulting next State from the (current, action) pair.
     */
    public void updateState(State current, Action action, State next) {
        sarsa.updateQTable(current, action, next);
    }

    /**
     * Uses the GreedyActionChooser to get the action that should be executed in the current State.
     * @param state the current State.
     * @return returns the Action that should be executed in the current State.
     */
    public Action getNextAction(State state) {
        return greedyActionChooser.chooseAction(spaceManager, state);
    }

    /**
     * Gets the type of unit the LearningManager is managing.
     * @return returns the type of unit being managed.
     */
    public UnitType getUnitType() {
        return this.type;
    }

    /**
     * Gets the DataManager being used by the LearningManager for collecting data points.
     * @return returns the DataManager.
     */
    public DataManager getDataManager() {
        return sarsa.getDataManager();
    }
}
